package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialIdentityRepository extends JpaRepository<CredentialIdentity, Long> {

    Optional<CredentialIdentity> findByBeneficiaryDni(Long beneficiaryDni);

    Optional<CredentialIdentity> findByBeneficiaryDniAndCredentialState(Long beneficiaryDni, CredentialState credentialStateActive);
}
