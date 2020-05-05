package com.atixlabs.semillasmiddleware.app.repository;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface CredentialStateRepository extends JpaRepository<CredentialState, Long>{

    Optional<CredentialState> findByStateName(String state);


    ArrayList<CredentialState> findByStateNameIn(ArrayList<String> statesCodesArrayList);
}
