package com.atixlabs.semillasmiddleware.app.model.provider.service;

import com.atixlabs.semillasmiddleware.app.model.provider.dto.ProviderCreateRequest;
import com.atixlabs.semillasmiddleware.app.model.provider.exception.InexistentCategoryException;
import com.atixlabs.semillasmiddleware.app.model.provider.model.Provider;
import com.atixlabs.semillasmiddleware.app.model.provider.model.ProviderCategory;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderCategoryRepository;
import com.atixlabs.semillasmiddleware.app.model.provider.repository.ProviderRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ProviderServiceTest {

    @Autowired
    ProviderService providerService;

    @Autowired
    ProviderRepository providerRepository;

    @Autowired
    ProviderCategoryService providerCategoryService;

    @Test
    void whenCreatingUserWitInvalidCategoryExpectToThrowInexistentCategoryException() {
        ProviderCreateRequest providerCreateRequest = this.getNewProviderRequest(Long.MAX_VALUE);
        assertThrows(InexistentCategoryException.class, () -> providerService.create(providerCreateRequest));
    }

    @Test
    void whenCreatingUserWitValidCategoryExpectToBeAbleToFindIt() {
        ProviderCreateRequest providerCreateRequest = this.getNewProviderRequest(1l);
        providerService.create(providerCreateRequest);
        Optional<Provider> provider = providerRepository.findByEmail(providerCreateRequest.getEmail());
        assertTrue(provider.isPresent());
        assertEquals(provider.get().getName(), providerCreateRequest.getName());
    }

    @Test
    void whenAddingInactiveProviderSizeDoesNotChange() {
        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("name").ascending());
        ProviderCategory providerCategory = null;
        try {
           providerCategory = providerCategoryService.findAll().get(0);
        }catch (Exception ex){
            //in case there are no categories in the repository
            fail();
        }

        Provider provider = new Provider(providerCategory, "Provider", "+541555555", "prov@at.com", 30, "Speciality", false);
        Long totalActives = providerService.findAll(true, pageRequest).getTotalElements();
        providerRepository.save(provider);
        assertEquals(providerService.findAll(true, pageRequest ).getTotalElements(), totalActives);

    }

    @Test
    void whenAddingActiveProviderSizeUppers1() {
        Pageable pageRequest = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("name").ascending());
        ProviderCategory providerCategory = null;
        try {
            providerCategory = providerCategoryService.findAll().get(0);
        }catch (Exception ex){
            //in case there are no categories in the repository
            fail();
        }

        Provider provider = new Provider(providerCategory, "Provider", "+541555555", "prov@at.com", 30, "Speciality", true);
        Long totalActives = providerService.findAll(true, pageRequest).getTotalElements();
        providerRepository.save(provider);
        assertEquals(providerService.findAll(true, pageRequest ).getTotalElements(), totalActives+1);

    }

    private ProviderCreateRequest getNewProviderRequest(Long providerCategory){
        return ProviderCreateRequest
                .builder()
                .email("provider@test.com")
                .benefit(59)
                .categoryId(providerCategory)
                .phone("+5411111111")
                .name("Provider")
                .speciality("Speciality")
                .build();
    }
}