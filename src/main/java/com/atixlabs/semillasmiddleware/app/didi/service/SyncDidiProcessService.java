package com.atixlabs.semillasmiddleware.app.didi.service;

import com.atixlabs.semillasmiddleware.app.didi.model.DidiAppUser;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialDwelling;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialIdentity;
import com.atixlabs.semillasmiddleware.app.service.CredentialBenefitService;
import com.atixlabs.semillasmiddleware.app.service.CredentialCreditService;
import com.atixlabs.semillasmiddleware.app.service.CredentialDwellingService;
import com.atixlabs.semillasmiddleware.app.service.CredentialIdentityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SyncDidiProcessService {

    private CredentialCreditService credentialCreditService;

    private CredentialBenefitService credentialBenefitService;

    private CredentialIdentityService credentialIdentityService;

    private CredentialDwellingService credentialDwellingService;

    private DidiAppUserService didiAppUserService;

    private DidiService didiService;

    @Autowired
    public SyncDidiProcessService(CredentialCreditService credentialCreditService, DidiAppUserService didiAppUserService, DidiService didiService, CredentialBenefitService credentialBenefitService, CredentialIdentityService credentialIdentityService, CredentialDwellingService credentialDwellingService){
        this.credentialCreditService = credentialCreditService;
        this.didiAppUserService = didiAppUserService;
        this.didiService = didiService;
        this.credentialBenefitService = credentialBenefitService;
        this.credentialIdentityService = credentialIdentityService;
        this.credentialDwellingService = credentialDwellingService;
    }

    public void emmitCredentialCredits() throws CredentialException {

        List<CredentialCredit> credentialCreditsToEmmit = this.credentialCreditService.getCredentialCreditsOnPendindDidiState();

        if(credentialCreditsToEmmit==null || credentialCreditsToEmmit.isEmpty()){
            log.info("No credit credentials to emmit were found");
        }else{

            log.info(" {} Credential Credits to emmit", credentialCreditsToEmmit.size());

            for(CredentialCredit credentialCredit : credentialCreditsToEmmit){
                this.emmitCredentialCredit(credentialCredit);
            }

        }
    }

    public void emmitCredentialsBenefit() throws CredentialException {

        List<CredentialBenefits> credentialBenefitsToEmmit = this.credentialBenefitService.getCredentialBenefitsOnPendindDidiState();

        if(credentialBenefitsToEmmit==null || credentialBenefitsToEmmit.isEmpty()){
            log.info("No benefits credentials to emmit were found");
        }else{

            log.info(" {} Credential Benefits to emmit", credentialBenefitsToEmmit.size());

            for(CredentialBenefits credentialBenefits : credentialBenefitsToEmmit){
                this.emmitCredentialBenefit(credentialBenefits);
            }

        }
    }


    public void emmitCredentialsIdentity() throws CredentialException {

        List<CredentialIdentity> credentialsIdentityToEmmit = this.credentialIdentityService.getCredentialIdentityOnPendindDidiState();

        if(credentialsIdentityToEmmit==null || credentialsIdentityToEmmit.isEmpty()){
            log.info("No Identity credentials to emmit were found");
        }else{

            log.info(" {} Credential Identity to emmit", credentialsIdentityToEmmit.size());

            for(CredentialIdentity credentialIdentity : credentialsIdentityToEmmit){
                this.emmitCredentialIdentity(credentialIdentity);
            }

        }
    }

    public void emmitCredentialsDwelling() throws CredentialException {

        List<CredentialDwelling> credentialDwellingToEmmit = this.credentialDwellingService.getCredentialDwellingOnPendindDidiState();

        if(credentialDwellingToEmmit==null || credentialDwellingToEmmit.isEmpty()){
            log.info("No Dwelling credentials to emmit were found");
        }else{

            log.info(" {} Credential IdenDwellingtity to emmit", credentialDwellingToEmmit.size());

            for(CredentialDwelling credentialDwelling : credentialDwellingToEmmit){
                this.emmitCredentialDwelling(credentialDwelling);
            }

        }
    }


    /**
     * get current Did for holder and emmit credential
     * @param credentialCredit
     */
    public void emmitCredentialCredit(CredentialCredit credentialCredit){

        log.info("Emmiting Credential Credit id {} idBondarea {} holder {}",credentialCredit.getId(), credentialCredit.getIdBondareaCredit(), credentialCredit.getCreditHolderDni());

        DidiAppUser didiAppUser = this.didiAppUserService.getDidiAppUserByDni(credentialCredit.getCreditHolderDni());

        if(didiAppUser!=null) {
            credentialCredit.setIdDidiReceptor(didiAppUser.getDid());
            credentialCredit = credentialCreditService.save(credentialCredit);

            didiService.createAndEmmitCertificateDidi(credentialCredit);

        }else{
            log.info("Id Didi for Holder {} not exist, Credential Credit for loan {} not emmited", credentialCredit.getCreditHolderDni(), credentialCredit.getIdBondareaCredit());
        }

    }

    public void emmitCredentialBenefit(CredentialBenefits credentialBenefit){

        log.info("Emmiting Credential Benefit id {} holder {} beneficiary {}",credentialBenefit.getId(), credentialBenefit.getCreditHolderDni(), credentialBenefit.getBeneficiaryDni());

        DidiAppUser didiAppUser = this.didiAppUserService.getDidiAppUserByDni(credentialBenefit.getBeneficiaryDni());

        if(didiAppUser!=null) {
            credentialBenefit.setIdDidiReceptor(didiAppUser.getDid());
            credentialBenefit = credentialBenefitService.save(credentialBenefit);

            didiService.createAndEmmitCertificateDidi(credentialBenefit);

        }else{
            log.info("Id Didi for Benefociary {} not exist, Credential Benefit {} not emmited", credentialBenefit.getCreditHolderDni(), credentialBenefit.getId());
        }

    }

    public void emmitCredentialIdentity(CredentialIdentity credentialIdentity){

        log.info("Emmiting Credential identity id {} holder {} beneficiary {}",credentialIdentity.getId(), credentialIdentity.getCreditHolderDni(), credentialIdentity.getBeneficiaryDni());

        DidiAppUser didiAppUser = this.didiAppUserService.getDidiAppUserByDni(credentialIdentity.getBeneficiaryDni());

        if(didiAppUser!=null) {
            credentialIdentity.setIdDidiReceptor(didiAppUser.getDid());
            credentialIdentity = credentialIdentityService.save(credentialIdentity);

            didiService.createAndEmmitCertificateDidi(credentialIdentity);

        }else{
            log.info("Id Didi for Beneficiary {} not exist, Credential Identity {} not emmited", credentialIdentity.getCreditHolderDni(), credentialIdentity.getId());
        }

    }

    public void emmitCredentialDwelling(CredentialDwelling credentialDwelling){

        log.info("Emmiting Credential Dwelling id {} holder {} beneficiary {}",credentialDwelling.getId(), credentialDwelling.getCreditHolderDni(), credentialDwelling.getBeneficiaryDni());

        DidiAppUser didiAppUser = this.didiAppUserService.getDidiAppUserByDni(credentialDwelling.getBeneficiaryDni());

        if(didiAppUser!=null) {
            credentialDwelling.setIdDidiReceptor(didiAppUser.getDid());
            credentialDwelling = credentialDwellingService.save(credentialDwelling);

            didiService.createAndEmmitCertificateDidi(credentialDwelling);

        }else{
            log.info("Id Didi for Beneficiary {} not exist, Credential Dwelling {} not emmited", credentialDwelling.getCreditHolderDni(), credentialDwelling.getId());
        }

    }





}
