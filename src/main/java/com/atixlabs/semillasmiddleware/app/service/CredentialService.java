package com.atixlabs.semillasmiddleware.app.service;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStateCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.app.bondarea.service.LoanService;
import com.atixlabs.semillasmiddleware.app.didi.service.DidiService;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialException;
import com.atixlabs.semillasmiddleware.app.dto.CredentialPage;
import com.atixlabs.semillasmiddleware.app.exceptions.CredentialNotExistsException;
import com.atixlabs.semillasmiddleware.app.exceptions.PersonDoesNotExistsException;
import com.atixlabs.semillasmiddleware.app.dto.CredentialDto;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.configuration.ParameterConfiguration;
import com.atixlabs.semillasmiddleware.app.model.configuration.constants.ConfigurationCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.*;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialCategoriesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialStatesCodes;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.CredentialTypesCodes;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.app.model.credentialState.RevocationReason;
import com.atixlabs.semillasmiddleware.app.model.credentialState.constants.RevocationReasonsCodes;
import com.atixlabs.semillasmiddleware.app.processControl.exception.InvalidProcessException;
import com.atixlabs.semillasmiddleware.app.processControl.model.ProcessControl;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessControlStatusCodes;
import com.atixlabs.semillasmiddleware.app.processControl.model.constant.ProcessNamesCodes;
import com.atixlabs.semillasmiddleware.app.processControl.service.ProcessControlService;
import com.atixlabs.semillasmiddleware.app.repository.*;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.DwellingCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.EntrepreneurshipCategory;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialBenefits;
import com.atixlabs.semillasmiddleware.app.model.credential.CredentialCredit;
import com.atixlabs.semillasmiddleware.app.repository.CredentialCreditRepository;
import com.atixlabs.semillasmiddleware.app.repository.PersonRepository;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.app.model.credential.constants.*;
import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class CredentialService {

    private CredentialRepository credentialRepository;
    private CredentialCreditRepository credentialCreditRepository;
    private CredentialIdentityRepository credentialIdentityRepository;
    private CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository;
    private CredentialDwellingRepository credentialDwellingRepository;
    private PersonRepository personRepository;
    private LoanRepository loanRepository;
    private CredentialBenefitsRepository credentialBenefitsRepository;
    private DIDHistoricRepository didHistoricRepository;
    private CredentialStateRepository credentialStateRepository;
    private ParameterConfigurationRepository parameterConfigurationRepository;
    private AnswerCategoryFactory answerCategoryFactory;
    private DidiService didiService;
    private RevocationReasonRepository revocationReasonRepository;
    private LoanService loanService;
    private ProcessControlService processControlService;
    private PersonService personService;
    private CredentialBenefitService credentialBenefitService;
    private CredentialStateService credentialStateService;

    @Value("${credentials.pageSize}")
    private String size;


    @Autowired
    public CredentialService(
            CredentialCreditRepository credentialCreditRepository,
            CredentialRepository credentialRepository,
            PersonRepository personRepository,
            LoanRepository loanRepository,
            CredentialBenefitsRepository credentialBenefitsRepository,
            DIDHistoricRepository didHistoricRepository,
            CredentialStateRepository credentialStateRepository,
            AnswerCategoryFactory answerCategoryFactory,
            CredentialIdentityRepository credentialIdentityRepository,
            CredentialEntrepreneurshipRepository credentialEntrepreneurshipRepository,
            CredentialDwellingRepository credentialDwellingRepository,
            ParameterConfigurationRepository parameterConfigurationRepository,
            DidiService didiService,
            RevocationReasonRepository revocationReasonRepository, LoanService loanService, ProcessControlService processControlService,PersonService personService, CredentialBenefitService credentialBenefitService, CredentialStateService credentialStateService) {
        this.credentialCreditRepository = credentialCreditRepository;
        this.credentialRepository = credentialRepository;
        this.personRepository = personRepository;
        this.loanRepository = loanRepository;
        this.credentialBenefitsRepository = credentialBenefitsRepository;
        this.didHistoricRepository = didHistoricRepository;
        this.credentialStateRepository = credentialStateRepository;
        this.parameterConfigurationRepository = parameterConfigurationRepository;
        this.answerCategoryFactory = answerCategoryFactory;
        this.credentialIdentityRepository = credentialIdentityRepository;
        this.credentialEntrepreneurshipRepository = credentialEntrepreneurshipRepository;
        this.credentialDwellingRepository = credentialDwellingRepository;
        this.didiService = didiService;
        this.revocationReasonRepository = revocationReasonRepository;
        this.loanService = loanService;
        this.processControlService = processControlService;
        this.personService = personService;
        this.credentialBenefitService = credentialBenefitService;
        this.credentialStateService = credentialStateService;
    }

    /**
     * Generate and update the credentials credit.
     * Checking for defaulters, and revoking or activate credentials (credit and benefit)
     */
    public void generateCreditAndBenefitsCredentialsByLoans() throws InvalidProcessException, PersonDoesNotExistsException {
        //check if process in credentials is not running
        if (!processControlService.isProcessRunning(ProcessNamesCodes.BONDAREA) && !processControlService.isProcessRunning(ProcessNamesCodes.CHECK_DEFAULTERS)) {

            LocalDateTime lastTimeProcessRun = processControlService.getProcessTimeByProcessCode(ProcessNamesCodes.CREDENTIALS.getCode());

            ProcessControl processCrendentialControl = processControlService.setStatusToProcess(ProcessNamesCodes.CREDENTIALS, ProcessControlStatusCodes.RUNNING);

            log.info(String.format("Generate Credential and Benefits Credential form %s", lastTimeProcessRun.toString()));

            try {
                List<Loan> loansDefaultToReview = this.handleDefaultCredits(lastTimeProcessRun);
                List<Loan> loansActiveToReview = this.handleActiveCredits(lastTimeProcessRun);
                List<Loan> loansFinalizedToReview = this.handleFinalizeCredits(lastTimeProcessRun);
                List<Loan> loansCancelledToReview = this.handleCancelledCredits(lastTimeProcessRun);
                List<Loan> loansNewToReview = this.handleNewCredits();

            } catch (PersonDoesNotExistsException ex) {
                log.error(ex.getMessage());
            } catch (Exception ex) {
                log.error("Error updating credentials credit ! " + ex.getMessage(), ex);
                processControlService.setStatusToProcess(ProcessNamesCodes.CREDENTIALS.getCode(), ProcessControlStatusCodes.FAIL.getCode());
            }


            //finish process
            processControlService.setStatusToProcess(ProcessNamesCodes.CREDENTIALS.getCode(), ProcessControlStatusCodes.OK.getCode());

        } else {
            log.info("Generate credentials can't run ! Process " + ProcessNamesCodes.BONDAREA.getCode() + " or " + ProcessNamesCodes.CHECK_DEFAULTERS.getCode() + " is still running");
        }

    }

    /**
     * Create a new credential only if
     * - The loan is active
     * - The loan is not in default
     * - The Holddr is not in default
     */
    private List<Loan> handleNewCredits() {
        //create credentials
        List<Loan> newLoans = loanService.findActiveAndOkLoansWithoutCredential();

        log.info(String.format("Generate Credential and Benefits Credential for %d new loans", newLoans.size()));

        List<Loan> loansForReview = new ArrayList<Loan>();

        for (Loan newLoan : newLoans) {
            try {
                CredentialCredit credentialCredit = this.createNewCreditCredential(newLoan);
                credentialBenefitService.createCredentialsBenefitsForNewLoan(newLoan);
            } catch (PersonDoesNotExistsException | CredentialException ex) {
                log.error("Error creating new credential credit for loan " + newLoan.getIdBondareaLoan() + " " + ex.getMessage());
                loansForReview.add(newLoan);
            }
        }

        return loansForReview;
    }

    /**
     * Create a new credential only if
     * - The loan is active
     * - The loan is not in default
     * - The holder is not in default
     */
    public CredentialCredit createNewCreditCredential(Loan loan) throws CredentialException, PersonDoesNotExistsException {

        log.info("Creating New Credential Credit for loan " + loan.getIdBondareaLoan());

        if (!this.isLoanValidForCredentialCredit(loan)) {
            log.error("Loan " + loan.getIdBondareaLoan() + " is not valid for credential credit");
            return null;
        }


        Optional<CredentialCredit> opCreditExistence = this.getCredentialCreditForLoan(loan);

        if (opCreditExistence.isEmpty()) {

            Optional<Person> opBeneficiary = personRepository.findByDocumentNumber(loan.getDniPerson());

            if (opBeneficiary.isPresent()) {

                Person holder = opBeneficiary.get();

                if (!holder.isInDefault()) {
                    CredentialCredit credit = this.buildCreditCredential(loan, opBeneficiary.get(), null);
                    loan.setHasCredential(true);

                    credit = credentialCreditRepository.save(credit);
                    //get the new id and save it on id historic
                    credit.setIdHistorical(credit.getId());
                    credentialCreditRepository.save(credit);
                    loanRepository.save(loan);
                    log.info("Credential Credit created for loan: " + loan.getIdBondareaLoan() + " dni: " + opBeneficiary.get().getDocumentNumber());
                    return credit;
                } else {
                    log.error("The holder " + holder.getDocumentNumber() + " is in default, credentials credit for loan " + loan.getIdBondareaLoan() + " not created");
                    return null;
                }
            } else {
                log.error("Person with dni " + loan.getDniPerson() + " has not been created. The loan exists but the survey with this person has not been loaded");
                throw new PersonDoesNotExistsException("Person with dni " + loan.getDniPerson() + " has not been created. The loan exists but the survey with this person has not been loaded");
            }
        } else {
            loan.setHasCredential(true);
            loanRepository.save(loan);
            log.error("The credit with idBondarea " + loan.getIdBondareaLoan() + " has an existent credential");
            return opCreditExistence.get();
        }
    }






    public Optional<Credential> getCredentialById(Long id) {
        //validate credential is in bd
        return credentialRepository.findById(id);
    }

    public Page<CredentialDto> findCredentials(String credentialType, String name, String dniBeneficiary, String dniHolder, String
            idDidiCredential, String lastUpdate, List<String> credentialState, Integer pageNumber) {
        Page<Credential> credentials;
        Pageable pageable = null;
        if (pageNumber != null && pageNumber >= 0 && this.size != null)
            pageable = PageRequest.of(pageNumber, Integer.parseInt(size), Sort.by(Sort.Direction.ASC, "updated"));

        credentials = credentialRepository.findCredentialsWithFilter(credentialType, name, dniBeneficiary, dniHolder, idDidiCredential, lastUpdate, credentialState, pageable);
        //total amount of elements using the same filters
        //Long totalAmountOfItems = credentialRepository.getTotalCountWithFilters(credentialType, name, dniBeneficiary, dniHolder, idDidiCredential, lastUpdate, credentialState);

        Page<CredentialDto> pageDto = credentials.map(CredentialDto::constructBasedOnCredentialType);

        CredentialPage credentialSet = new CredentialPage(pageDto, credentials.getNumberOfElements());

        return pageDto;
    }

    public Map<Long, String> getRevocationReasonsForUser() {
        Map<Long, String> revocationReasons = new HashMap<>();
        Optional<RevocationReason> expiredReason = revocationReasonRepository.findByReason(RevocationReasonsCodes.EXPIRED_INFO.getCode());
        if (expiredReason.isPresent())
            revocationReasons.put(expiredReason.get().getId(), expiredReason.get().getReason());
        else
            log.error("Error getting expired reason of revocation");

        Optional<RevocationReason> unlinkingReason = revocationReasonRepository.findByReason(RevocationReasonsCodes.UNLINKING.getCode());
        if (unlinkingReason.isPresent())
            revocationReasons.put(unlinkingReason.get().getId(), unlinkingReason.get().getReason());
        else
            log.error("Error getting unlinking reason of revocation");

        return revocationReasons;
    }

    public Optional<String> getReasonFromId(Long idReason) {
        if (idReason != null) {
            Optional<RevocationReason> reason = revocationReasonRepository.findById(idReason);
            if (reason.isPresent()) {
                //validate if the reason could be one allowed to the user.
                Map<Long, String> reasonsForUser = getRevocationReasonsForUser();
                if (reasonsForUser.containsValue(reason.get().getReason()))
                    return Optional.of(reason.get().getReason());
            }
        }
        return Optional.empty();
    }


    /**
     * Si existe el titular como persona registrada
     * Set holder in default and
     * Credit Credential
     * If exists and is emitted, revoke complete
     * If exists and is Pending Didi, revoke only local
     * If exists and is revoked, do nothing
     * If not exists, do nothing
     * Credencial de Beneficio
     * Titular
     * Si existe y esta activa y emitida, revoco la crendencial en Didi y revoco la credencial localmente,
     * Si existe y esta pendiente de didi, revoco la credencial localmente
     * si existe y esta revocada, no hago nada
     * si no existe, no hago nada
     * Familiar
     * Credencial de Beneficio Familiar-Titular con Id Didi del Titular
     * Si existe y esta activa y emitida, revoco la crendencial en Didi y revoco la credencial localmente,
     * Si existe y esta pendiente de didi, revoco la credencial localmente
     * si existe y esta revocada, no hago nada
     * si no existe, no hago nada
     * Credendial de Beneficio Familiar-Titular con Id Didi del Familiar
     * Si existe y esta activa y emitida, revoco la crendencial en Didi y revoco la credencial localmente,
     * Si existe y esta pendiente de didi, revoco la credencial localmente
     * si existe y esta revocada, no hago nada
     * si no existe, no hago nada
     *
     * @param lastTimeProcessRun
     * @return
     */
    private List<Loan> handleDefaultCredits(LocalDateTime lastTimeProcessRun) {

        List<Loan> loansModifiedInDefault = loanService.findLastLoansModifiedInDefault(lastTimeProcessRun);

        log.info(String.format(" %d Loans in default founded", (loansModifiedInDefault != null ? loansModifiedInDefault.size() : 0)));

        List<Loan> loansToReview = new ArrayList<Loan>();

        for (Loan defaultLoan : loansModifiedInDefault) {

            Optional<Person> opHolder = personRepository.findByDocumentNumber(defaultLoan.getDniPerson());

            if (opHolder.isPresent()) {

                Person holder = opHolder.get();

                try {
                    if (!this.revokeCredentialCredit(defaultLoan, holder)) {
                        loansToReview.add(defaultLoan);
                    }

                    credentialBenefitService.revokeHolderCredentialsBenefitsForLoan(holder);
                    credentialBenefitService.revokeFamilyCredentialsBenefitsForLoan(holder);

                } catch (CredentialException ex) {
                    log.error("Error creating new credential credit for loan " + defaultLoan.getIdBondareaLoan() + " " + ex.getMessage());
                    loansToReview.add(defaultLoan);
                }
            } else { //Holder not exists
                String message = String.format("Can't evaluate default for Credit and Benefit Credential, Holder dni %d not exists", defaultLoan.getDniPerson());
                log.error(message);
                loansToReview.add(defaultLoan);
            }
        }

        return loansToReview;
    }

    /**
     *     Si existe el titular como persona registrada y el credito posee credenciales previas creadas (sino pasa al flujo de creditos nuevos)
     *     Si el credito actual esta en la lista de moras del Titular,  lo quito
     *     Verifico el estado del Titular
     *         Si esta en Mora
     *             Credencial Crediticia
     *                 Si la credencial crediticia esta activa y emitida
     *                     Revoco la credencial anterior Tanto en Didi como localmente y creo una nueva en estado pendiente de Didi
     *                 Si la credencial creditica esta pendiente de didi
     *                     Revoco la credencial anterior localmente, creo una nueva credencial pendiente de Didi
     *                 Si la credencial credicitia esta revocada
     *                     Creo una nueva credencial crediticia en estado Pendiente de Didi
     *             Credencial de Beneficio
     *                 No modifico el estado de ninguna de las credenciales de beneficio asociadas al credito
     *          Si el Titular NO esta en Mora
     *             Credencial Crediticia
     *                 Si la credencial crediticia esta activa y emitida
     *                     Revoco la credencial anterior tanto en Didi como localmente y creo una nueva en estado pendiente de Didi
     *                 Si la credencial creditica esta pendiente de Didi
     *                     Revoco la credencial anterior localmente, creo una nueva credencial pendiente de Didi
     *                 Si la credencial credicitia esta revocada
     *                     Creo una nueva credencial crediticia en estado Pendiente de Didi
     *             Credencial de Beneficio
     *                 Titular
     *                     Si la Crendencial desta Activa y Emitida, no hago nada
     *                     Si la Credencial esta Pendiende de Didi, no hago nada
     *                     Si la Credencial esta revocada, creo una nueva credencial en estado pendiente de Didi
     *                 Familiar
     *                     Si la Crendencial desta Activa y Emitida, no hago nada
     *                     Si la Credencial esta Pendiende de Didi, no hago nada
     *                     Si la Credencial esta revocada, creo una nueva credencial en estado pendiente de Didi
     *
     * @param lastTimeProcessRun
     * @return
     */
    private List<Loan> handleActiveCredits(LocalDateTime lastTimeProcessRun) {

        List<Loan> loansModifiedActive = loanService.findLastLoansModifiedActiveWithCredential(lastTimeProcessRun);

        log.info(String.format(" %d active credits found for evaluate credentials ", (loansModifiedActive != null ? loansModifiedActive.size() : 0)));

        List<Loan> loansToreview = new ArrayList<Loan>();

        for (Loan loan : loansModifiedActive) {
            try {
                this.updateOrCreateCredentialsCreditsAndCredentialBenefits(loan);
            } catch (CredentialException  e) {
                loansToreview.add(loan);
            }
        }

        return loansToreview;

    }


    /**
     * Si finalizó un credito
     * Si el Credito estaba Activo
     * Revoco beneficios asociados al titular y familiares, solo si es el unico credito activo para el titular, queda credencial crediticia vigente
     * Si el Credito estaba en Mora (se supone credenciales revocadas)
     * Doy de baja el credito en Mora parael Titular
     * Verifico el estado del Titular
     * Reactivo sus beneficios de otros creditos activos de los que sea Titular en estado Pendiente de Didi
     * Reactivo los beneficios de los familiares relacionados con el Titular para ese Credito en estado Pendiente de Didi
     *
     * @param lastTimeProcessRun
     * @return
     * @throws PersonDoesNotExistsException
     */
    private List<Loan> handleFinalizeCredits(LocalDateTime lastTimeProcessRun) throws PersonDoesNotExistsException {

        List<Loan> loansModifiedFinalized = loanService.findLastLoansModifiedFinalized(lastTimeProcessRun);
        List<Loan> loansToreview = new ArrayList<Loan>();

        List<CredentialState> pendingAndActiveState = credentialStateRepository.findByStateNameIn(List.of(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), CredentialStatesCodes.PENDING_DIDI.getCode()));

        for (Loan loan : loansModifiedFinalized) {

            Optional<CredentialCredit> opCredit = credentialCreditRepository.findFirstByIdBondareaCreditOrderByDateOfIssueDesc(loan.getIdBondareaLoan());
            if (opCredit.isPresent()) {
                try {
                    this.closeCredit(opCredit.get(), loan);
                    log.info("Credential Credit is set to FINALIZE, for credential id historic" + opCredit.get().getIdHistorical());


                    //TODO manejar beneficios
                    List<CredentialBenefits> benefitsHolder = credentialBenefitsRepository.findByCreditHolderDniAndCredentialStateInAndBeneficiaryType(loan.getDniPerson(), pendingAndActiveState, PersonTypesCodes.HOLDER.getCode());
                    //there have to be only 1. The holder only have at max 1 holder benefits.
                    if (benefitsHolder.size() > 0) {
                        if (this.revokeCredential(benefitsHolder.get(0).getId(), RevocationReasonsCodes.CANCELLED.getCode()))
                            log.info("The credential has been set to finish successfully");
                        else
                            log.error("The credential was not set to finish");
                    } else {
                        log.info("THERE IS NO ACTIVE OR PENDING BENEFITS OF THE HOLDER TO BE REVOKED");
                    }
                } catch (Exception e) {
                    loansToreview.add(loan);
                }
            } else {
                log.info("Loan " + loan.getIdBondareaLoan() + " dont have credential, nothing to do");
            }
        }

        return loansToreview;

    }

    private void closeCredit(CredentialCredit credentialCredit, Loan loan) {

        credentialCredit.setFinishDate(DateUtil.getLocalDateTimeNow().toLocalDate());
        credentialCreditRepository.save(credentialCredit);
        log.info("Credential Credit is set to Finish Date, for credential id historic" + credentialCredit.getIdHistorical());

        Optional<Person> holder = personRepository.findByDocumentNumber(credentialCredit.getCreditHolderDni());

        if (holder.isPresent()) {
            if (holder.get().isInDefault()) {
                if (holder.get().removeLoanInDefault(loan)) {
                    personRepository.save(holder.get());
                    log.info("Loan " + loan.getIdBondareaLoan() + "in default remove for holder " + holder.get().getDocumentNumber());
                }
            }
        }

    }

    /**
     * Si cancelo un credito
     * Si el Credito estaba Activo
     * Revoco beneficios asociados al titular y familiares, solo si es el unico credito activo para el titular, queda credencial crediticia vigente (referencia a la pregunta que te contesté en mail anterior)
     * Si el Credito estaba en Mora (se supone credenciales revocadas)
     * Doy de baja el credito en Mora para el Titular
     * Verifico el estado del Titular
     * Reactivo sus beneficios de otros creditos activos de los que sea Titular en estado Pendiente de Didi  (Idem, casos anteriores:  La credencial de beneficio a nombre del titular es 1 sóla, NO tiene una por cada crédito. Por lo tanto sería:  Reactivo credencial de Beneficios sí el titular tiene otros créditos activos)
     * Reactivo los beneficios de los familiares relacionados con el Titular para ese Credito en estado Pendiente de Didi SI SOLO SI el titular tiene otros créditos activos
     */
    private List<Loan> handleCancelledCredits(LocalDateTime lastTimeProcessRun) {

        List<Loan> loansModifiedFinalized = loanService.findLastLoansModifiedCancelled(lastTimeProcessRun);

        List<Loan> loansToreview = new ArrayList<Loan>();

        List<CredentialState> pendingAndActiveState = credentialStateRepository.findByStateNameIn(List.of(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), CredentialStatesCodes.PENDING_DIDI.getCode()));

        for (Loan loan : loansModifiedFinalized) {

            Optional<CredentialCredit> opCredit = credentialCreditRepository.findFirstByIdBondareaCreditOrderByDateOfIssueDesc(loan.getIdBondareaLoan());
            CredentialCredit updateCredit = opCredit.get();
            try {
                if (loan.getStatus().equals(LoanStatusCodes.CANCELLED.getCode())) {
                    this.closeCredit(opCredit.get(), loan);
                    log.info("Credential Credit is set to CANCEL, for credential id historic" + updateCredit.getIdHistorical());

                    //Revoke credential credit
                    boolean haveRevokeOk = this.revokeComplete(updateCredit, RevocationReasonsCodes.CANCELLED.getCode());

                    List<CredentialBenefits> benefitsHolder = credentialBenefitsRepository.findByCreditHolderDniAndCredentialStateInAndBeneficiaryType(updateCredit.getBeneficiaryDni(), pendingAndActiveState, PersonTypesCodes.HOLDER.getCode());

                    //there have to be only 1. The holder only have at max 1 holder benefits.
                    if (benefitsHolder.size() > 0) {
                        //revoke only the benefits if the holder does not have another credit. And revoke the familiar benefits given by this credit.
                        boolean result = this.revokeCredential(benefitsHolder.get(0).getId(), RevocationReasonsCodes.CANCELLED.getCode());
                        haveRevokeOk = haveRevokeOk && result;
                    } else
                        log.info("There is no active or pending benefits of the holder to be revoked");

                    //check results
                    if (haveRevokeOk)
                        log.info("The credential has been set to cancel successfully");
                    else
                        log.error("The credential was not set to cancel");

                }
            } catch (CredentialException e) {
                log.error("Error to handle credential exception", e);
                loansToreview.add(loan);
            }
        }
        return loansToreview;
    }


    /**
     *     Si existe el titular como persona registrada y el credito posee credenciales previas creadas (sino pasa al flujo de creditos nuevos)
     *     Si el credito actual esta en la lista de moras del Titular,  lo quito
     *     Verifico el estado del Titular
     *         Si esta en Mora
     *             Credencial Crediticia
     *                 Si la credencial crediticia esta activa y emitida
     *                     Revoco la credencial anterior Tanto en Didi como localmente y creo una nueva en estado pendiente de Didi
     *                 Si la credencial creditica esta pendiente de didi
     *                     Revoco la credencial anterior localmente, creo una nueva credencial pendiente de Didi
     *                 Si la credencial credicitia esta revocada
     *                     Creo una nueva credencial crediticia en estado Pendiente de Didi
     *             Credencial de Beneficio
     *                 No modifico el estado de ninguna de las credenciales de beneficio asociadas al credito
     *          Si el Titular NO esta en Mora
     *             Credencial Crediticia
     *                 Si la credencial crediticia esta activa y emitida
     *                     Revoco la credencial anterior tanto en Didi como localmente y creo una nueva en estado pendiente de Didi
     *                 Si la credencial creditica esta pendiente de Didi
     *                     Revoco la credencial anterior localmente, creo una nueva credencial pendiente de Didi
     *                 Si la credencial credicitia esta revocada
     *                     Creo una nueva credencial crediticia en estado Pendiente de Didi
     *             Credencial de Beneficio
     *                 Titular
     *                     Si la Crendencial desta Activa y Emitida, no hago nada
     *                     Si la Credencial esta Pendiende de Didi, no hago nada
     *                     Si la Credencial esta revocada, creo una nueva credencial en estado pendiente de Didi
     *                 Familiar
     *                     Si la Crendencial desta Activa y Emitida, no hago nada
     *                     Si la Credencial esta Pendiende de Didi, no hago nada
     *                     Si la Credencial esta revocada, creo una nueva credencial en estado pendiente de Didi
     * @param loan
     * @throws PersonDoesNotExistsException
     * @throws CredentialException
     */
    private void updateOrCreateCredentialsCreditsAndCredentialBenefits(Loan loan) throws CredentialNotExistsException, CredentialException {

        log.info("Updating credential credit for loan " + loan.getIdBondareaLoan());

        Optional<Person> opHolder = personRepository.findByDocumentNumber(loan.getDniPerson());

        if (opHolder.isPresent()) {

            Person holder = opHolder.get();

            if(holder.removeLoanInDefault(loan)){
                holder = personRepository.save(holder);
            }

            if (!holder.isInDefault()) {
                    this.updateCredentialCreditForActiveLoan(loan, holder);

                    credentialBenefitService.updateCredentialBenefitForActiveLoan(loan,holder);

            } else {
                this.updateCredentialCreditForActiveLoan(loan, holder);
            }
        } else { //Holder not exists
            String message = String.format("Can't handle loan %s, Holder dni %d not exists", loan.getIdBondareaLoan(),loan.getDniPerson());
            log.error(message);
            throw new CredentialException(message);
        }

    }

    /**
     *      Credencial Crediticia
     *          If Credit Credential is Active and Emmited
     *              Revoke current and build new in state Pending Didi
     *          If Credit Credential is pending didi
     *              Revoke current and build new in state Pending Didi
     *          If Credit Credential is Revoke
     *              build new in state Pending Didi
     * @param loan
     * @param holder
     */
    //TODO validar si es necesario revocar y crear de neuvo la entidad, porque si falla en beneficio va a retomar el proceso de nuevo
    private void updateCredentialCreditForActiveLoan(Loan loan, Person holder) throws CredentialNotExistsException,CredentialException {

        Optional<CredentialCredit> opCredit = credentialCreditRepository.findFirstByIdBondareaCreditOrderByDateOfIssueDesc(loan.getIdBondareaLoan());

        if(opCredit.isPresent()) {
            CredentialCredit currentCredentialCredit = opCredit.get();

            if ((currentCredentialCredit.isEmitted() && this.isCredentialActive(currentCredentialCredit)) || (this.isCredentialPendingDidi(currentCredentialCredit))) {

                if(this.credentialCreditMustBeenUpdate(currentCredentialCredit, loan)) {
                    this.revokeComplete(currentCredentialCredit, RevocationReasonsCodes.UPDATE_INTERNAL);
                    CredentialCredit newCredentialCredit = this.buildCreditCredential(loan, holder, currentCredentialCredit);
                    credentialCreditRepository.save(newCredentialCredit);
                }else {
                    log.info(String.format("The Credential credit %d not need be upodated for loan %s",currentCredentialCredit.getId(), loan.getIdBondareaLoan()));
                }

            } else {
                if (this.isCredentialRevoked(currentCredentialCredit)) {

                    CredentialCredit newCredentialCredit = this.buildCreditCredential(loan, holder, currentCredentialCredit);
                    credentialCreditRepository.save(newCredentialCredit);

                } else {
                    String message = String.format("Current credential credit id %d is not in valid state for modifications");
                    log.error(message);
                    throw new CredentialException(message);
                }

            }
        }else {
            String message = String.format("Credential credit for loan  %s is not present",loan.getIdBondareaLoan());
            throw new CredentialNotExistsException(message);
        }
    }


/*
    private void createCredentialCredit(Loan loan) throws PersonDoesNotExistsException {
        this.createNewCreditCredentials(loan);//TODO <-refactor this, one type of credential for method, and create de familiy benefits
        //TODO EMMIT DIDI
    }*/


    public void buildAllCredentialsFromForm(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
        log.info("buildAllCredentialsFromForm: " + this.toString());
        if (validateAllCredentialsFromForm(surveyForm, processExcelFileResult))
            saveAllCredentialsFromForm(surveyForm);
    }


    /**
     * The following are non-public methods, isolating functionality.
     * to make public methods easier to read.
     *
     * @param surveyForm
     */
    private boolean validateAllCredentialsFromForm(SurveyForm surveyForm, ProcessExcelFileResult processExcelFileResult) {
        log.info("  validateIdentityCredentialFromForm");

        //1-get all people data from form, creditHolder will be a beneficiary as well.
        ArrayList<Category> categoryArrayList = surveyForm.getAllCompletedCategories();

        //2-get creditHolder Data
        PersonCategory creditHolderPersonCategory = (PersonCategory) surveyForm.getCategoryByUniqueName(Categories.BENEFICIARY_CATEGORY_NAME.getCode(), null);
        Person creditHolder = Person.getPersonFromPersonCategory(creditHolderPersonCategory);

        //2-verify each person is new, or his data has not changed.
        boolean allCredentialsNewOrInactive = true;
        for (Category category : categoryArrayList) {
            switch (category.getCategoryName()) {
                case BENEFICIARY_CATEGORY_NAME:
                case SPOUSE_CATEGORY_NAME:
                case CHILD_CATEGORY_NAME:
                case KINSMAN_CATEGORY_NAME:
                    PersonCategory beneficiaryPersonCategory = (PersonCategory) category;
                    Person beneficiary = Person.getPersonFromPersonCategory(beneficiaryPersonCategory);
                    if (isCredentialAlreadyExistent(beneficiary.getDocumentNumber(), CredentialCategoriesCodes.IDENTITY.getCode(), processExcelFileResult))
                        allCredentialsNewOrInactive = false;
                    break;
                case ENTREPRENEURSHIP_CATEGORY_NAME:
                    if (isCredentialAlreadyExistent(creditHolder.getDocumentNumber(), CredentialCategoriesCodes.ENTREPRENEURSHIP.getCode(), processExcelFileResult))
                        allCredentialsNewOrInactive = false;
                    break;
                case DWELLING_CATEGORY_NAME:
                    if (isCredentialAlreadyExistent(creditHolder.getDocumentNumber(), CredentialCategoriesCodes.DWELLING.getCode(), processExcelFileResult))
                        allCredentialsNewOrInactive = false;
                    break;
            }
        }
        return allCredentialsNewOrInactive;
    }

    private boolean isCredentialAlreadyExistent(Long beneficiaryDni, String credentialCategoryCode, ProcessExcelFileResult processExcelFileResult) {

        List<String> statesCodesToFind = new ArrayList<>();
        statesCodesToFind.add(CredentialStatesCodes.PENDING_DIDI.getCode());
        statesCodesToFind.add(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode());

        List<CredentialState> credentialStateActivePending = credentialStateRepository.findByStateNameIn(statesCodesToFind);

        Optional<Credential> credentialOptional = credentialRepository.findByBeneficiaryDniAndCredentialCategoryAndCredentialStateIn(
                beneficiaryDni,
                credentialCategoryCode,
                credentialStateActivePending
        );
        if (credentialOptional.isEmpty())
            return false;
        else
            processExcelFileResult.addRowError(
                    "Warning CREDENCIAL DUPLICADA",
                    "Ya existe una credencial de tipo " + credentialCategoryCode +
                            " en estado " + credentialOptional.get().getCredentialState().getStateName() +
                            " para el DNI " + beneficiaryDni + " si desea continuar debe revocarlas manualmente"
            );
        return true;
    }

    private void saveAllCredentialsFromForm(SurveyForm surveyForm) {
        //1-get creditHolder Data
        PersonCategory creditHolderPersonCategory = (PersonCategory) surveyForm.getCategoryByUniqueName(Categories.BENEFICIARY_CATEGORY_NAME.getCode(), null);
        Person creditHolder = Person.getPersonFromPersonCategory(creditHolderPersonCategory);

        //1-get all data from form
        ArrayList<Category> categoryArrayList = surveyForm.getAllCompletedCategories();

        //4-Now working with each beneficiary
        for (Category category : categoryArrayList) {
            saveCredential(category, creditHolder);
        }
    }


    private void saveCredential(Category category, Person creditHolder) {
        log.info("  saveCredential: " + category.getCategoryName());
        switch (category.getCategoryName()) {
            case BENEFICIARY_CATEGORY_NAME:
            case CHILD_CATEGORY_NAME:
            case SPOUSE_CATEGORY_NAME:
            case KINSMAN_CATEGORY_NAME:
                credentialIdentityRepository.save(buildIdentityCredential(category, creditHolder));
                break;
            case ENTREPRENEURSHIP_CATEGORY_NAME:
                credentialEntrepreneurshipRepository.save(buildEntrepreneurshipCredential(category, creditHolder));
                break;
            case DWELLING_CATEGORY_NAME:
                credentialDwellingRepository.save(buildDwellingCredential(category, creditHolder));
                break;
        }
    }


    private Person savePersonIfNew(Person person) {
        Optional<Person> personOptional = personRepository.findByDocumentNumber(person.getDocumentNumber());
        if (personOptional.isEmpty())
            return personRepository.save(person);
        if (!(person.equalsIgnoreId(person, personOptional.get()))) {
            person.setId(personOptional.get().getId());
            return personRepository.save(person);
        }
        return personOptional.get();
    }

    //todo move into credential class
    private void buildCredential(Person creditHolder, Credential credential) {
        creditHolder = savePersonIfNew(creditHolder);

        credential.setDateOfIssue(DateUtil.getLocalDateTimeNow());
        credential.setCreditHolder(creditHolder);
        credential.setCreditHolderDni(creditHolder.getDocumentNumber());
        credential.setCreditHolderFirstName(creditHolder.getFirstName());
        credential.setCreditHolderLastName(creditHolder.getLastName());

        //the beneficiary is the same as the credit holder for all credentials but identity
        //buildIdentityCredential overwrites this value with the different members.
        credential.setBeneficiary(creditHolder);
        credential.setBeneficiaryDni(creditHolder.getDocumentNumber());
        credential.setBeneficiaryFirstName(creditHolder.getFirstName());
        credential.setBeneficiaryLastName(creditHolder.getLastName());

        //credential.setCredentialStatus(CredentialStatusCodes.CREDENTIAL_PENDING_BONDAREA.getCode());
        Optional<CredentialState> credentialStateOptional = credentialStateRepository.findByStateName(CredentialStatesCodes.PENDING_DIDI.getCode());
        credentialStateOptional.ifPresent(credential::setCredentialState);
    }

    //todo move into credential type class
    private CredentialIdentity buildIdentityCredential(Category category, Person creditHolder) {
        PersonCategory beneficiaryPersonCategory = (PersonCategory) category;
        Person beneficiary = Person.getPersonFromPersonCategory(beneficiaryPersonCategory);
        beneficiary = savePersonIfNew(beneficiary);

        CredentialIdentity credentialIdentity = new CredentialIdentity();
        buildCredential(creditHolder, credentialIdentity);

        credentialIdentity.setBeneficiary(beneficiary);
        credentialIdentity.setBeneficiaryDni(beneficiary.getDocumentNumber());
        credentialIdentity.setBeneficiaryFirstName(beneficiary.getFirstName());
        credentialIdentity.setBeneficiaryLastName(beneficiary.getLastName());

        credentialIdentity.setCredentialCategory(CredentialCategoriesCodes.IDENTITY.getCode());


        credentialIdentity.setBeneficiaryGender(beneficiary.getGender());
        credentialIdentity.setBeneficiaryBirthDate(beneficiary.getBirthDate());

        switch (beneficiaryPersonCategory.getPersonType()) {
            case BENEFICIARY:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY.getCode());
                credentialIdentity.setRelationWithCreditHolder("titular");//todo parar a enum
                break;
            case SPOUSE:
            case CHILD:
            case OTHER_KINSMAN:
                credentialIdentity.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_IDENTITY_FAMILY.getCode());
                credentialIdentity.setRelationWithCreditHolder("familiar");//todo pasar a enum
                break;
        }

        return credentialIdentity;
    }

    //todo move into credential type class
    private CredentialEntrepreneurship buildEntrepreneurshipCredential(Category category, Person creditHolder) {
        EntrepreneurshipCategory entrepreneurshipCategory = (EntrepreneurshipCategory) category;

        CredentialEntrepreneurship credentialEntrepreneurship = new CredentialEntrepreneurship();
        buildCredential(creditHolder, credentialEntrepreneurship);
        credentialEntrepreneurship.setEntrepreneurshipType(entrepreneurshipCategory.getType());
        credentialEntrepreneurship.setStartActivity((entrepreneurshipCategory.getActivityStartDate() != null ? entrepreneurshipCategory.getActivityStartDate().getYear() : null));
        credentialEntrepreneurship.setMainActivity(entrepreneurshipCategory.getMainActivity());
        credentialEntrepreneurship.setEntrepreneurshipName(entrepreneurshipCategory.getName());
        credentialEntrepreneurship.setEntrepreneurshipAddress(entrepreneurshipCategory.getAddress());
        credentialEntrepreneurship.setEndActivity(entrepreneurshipCategory.getActivityEndingDate());

        credentialEntrepreneurship.setCredentialCategory(CredentialCategoriesCodes.ENTREPRENEURSHIP.getCode());
        credentialEntrepreneurship.setCredentialDescription(CredentialCategoriesCodes.ENTREPRENEURSHIP.getCode());

        return credentialEntrepreneurship;
    }

    //todo move into credential type class
    private CredentialDwelling buildDwellingCredential(Category category, Person creditHolder) {
        DwellingCategory entrepreneurshipCategory = (DwellingCategory) category;

        CredentialDwelling credentialDwelling = new CredentialDwelling();
        buildCredential(creditHolder, credentialDwelling);

        credentialDwelling.setDwellingType(entrepreneurshipCategory.getDwellingType());
        credentialDwelling.setDwellingAddress(entrepreneurshipCategory.getDistrict());
        credentialDwelling.setPossessionType(entrepreneurshipCategory.getHoldingType());

        credentialDwelling.setCredentialCategory(CredentialCategoriesCodes.DWELLING.getCode());
        credentialDwelling.setCredentialDescription(CredentialCategoriesCodes.DWELLING.getCode());

        return credentialDwelling;
    }

    public Boolean isLoanValidForCredentialCredit(Loan loan) {
        return loan.getState().equals(LoanStateCodes.OK.getCode()) && loan.getStatus().equals(LoanStatusCodes.ACTIVE.getCode());
    }


    public CredentialCredit buildCreditCredential(Loan loan, Person holder, CredentialCredit credentialCreditPrev) throws CredentialException {
        CredentialCredit credentialCredit = new CredentialCredit();

        Optional<ParameterConfiguration> config = parameterConfigurationRepository.findByConfigurationName(ConfigurationCodes.ID_DIDI_ISSUER.getCode());
        if (config.isPresent()) {
            credentialCredit.setIdDidiIssuer(config.get().getValue());
        } else {
            //
            throw new CredentialException("Id Didi Issuer Not exists, cant build credential");
        }

        this.setLoanInfoToCredential(credentialCredit,loan);

        credentialCredit.setBeneficiary(holder);

        credentialCredit.setCreditHolder(holder);

        //Credential Parent fields
        credentialCredit.setDateOfIssue(DateUtil.getLocalDateTimeNow());


        //The ID Didi Receptor leave blank for emmit step

        //Person do not have a DID yet -> set as pending didi
        Optional<CredentialState> opStateActive = credentialStateService.getCredentialPendingDidiState();
        credentialCredit.setCredentialState(opStateActive.get());


        //This depends of the type of loan from bondarea
        credentialCredit.setCredentialDescription(CredentialTypesCodes.CREDENTIAL_CREDIT.getCode());
        credentialCredit.setCredentialCategory(CredentialCategoriesCodes.CREDIT.getCode());// TODO this column will be no longer useful

        if(credentialCreditPrev!=null){
            credentialCredit.setIdHistorical(credentialCreditPrev.getIdHistorical());
        }

        return credentialCredit;
    }

    private CredentialCredit setLoanInfoToCredential(CredentialCredit credentialCredit, Loan loan){

        credentialCredit.setIdBondareaCredit(loan.getIdBondareaLoan());
        // TODO we need the type from bondarea - credentialCredit.setCreditType();
        credentialCredit.setIdGroup(loan.getIdGroup());
        credentialCredit.setCurrentCycle(loan.getCycleDescription()); // si cambia, se tomara como cambio de ciclo
        //TODO data for checking - credentialCredit.totalCycles;

        credentialCredit.setAmountExpiredCycles(0);
        credentialCredit.setCreditState(loan.getStatus());
        credentialCredit.setExpiredAmount(loan.getExpiredAmount());
        credentialCredit.setCreationDate(loan.getCreationDate());

        return credentialCredit;

    }

    private Boolean credentialCreditMustBeenUpdate(CredentialCredit credentialCredit, Loan loan){

        boolean areEquals = true;

        areEquals = credentialCredit.getIdBondareaCredit().equals(loan.getIdBondareaLoan());
        areEquals = areEquals && credentialCredit.getIdGroup().equals(loan.getIdGroup());
        areEquals = areEquals && credentialCredit.getCurrentCycle().equals(loan.getCycleDescription());
        areEquals = areEquals && credentialCredit.getCreditState().equals(loan.getStatus());
        areEquals = areEquals && credentialCredit.getExpiredAmount().equals(loan.getExpiredAmount());
        areEquals = areEquals && credentialCredit.getCreationDate().equals(loan.getCreationDate());
        areEquals = areEquals && credentialCredit.getBeneficiaryDni().equals(loan.getDniPerson());

        return areEquals;

    }

    /**
     * This will try to create a new benefit credential for the beneficiary.
     * The benefits familiar depends on if he has his OWN credential credit (so he download the app)
     */
   /* public void createNewBenefitsCredential(CredentialIdentity identity) {
        //check if person is valid to create a benefit of the type required.
        if (this.isValidPersonForNewBenefits(identity.getCreditHolder(), identity.getBeneficiary())) {

            log.info("Creating Credential Benefits");
            CredentialBenefits benefits = null;
            if (identity.getCreditHolderDni().equals(identity.getBeneficiaryDni()))
                benefits = this.buildBenefitsCredential(identity.getBeneficiary(), PersonTypesCodes.HOLDER);
            else
                benefits = this.buildBenefitsCredential(identity.getBeneficiary(), PersonTypesCodes.FAMILY);

            if (benefits != null) {
                credentialBenefitsRepository.save(benefits);
                log.info("Credential benefits created for dni: " + identity.getBeneficiary().getDocumentNumber());
            }
        }
    }*/
    private boolean isValidPersonForNewBenefits(Person holder, Person beneficiary) {
        List<CredentialState> pendingAndActiveState = credentialStateRepository.findByStateNameIn(List.of(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), CredentialStatesCodes.PENDING_DIDI.getCode()));

        Long holderDni = holder.getDocumentNumber();
        Long beneficiaryDni = beneficiary.getDocumentNumber();
        //is holder
        if (holderDni.equals(beneficiaryDni)) {
            //get actual benefits of the holder
            Optional<CredentialBenefits> opBenefits = credentialBenefitsRepository.findByBeneficiaryDniAndCredentialStateInAndBeneficiaryType(beneficiaryDni, pendingAndActiveState, PersonTypesCodes.HOLDER.getCode());

            //if he doesnt have a credential benefits, is valid to create
            if (opBenefits.isEmpty())
                return true;
        } else {
            //is familiar
            List<CredentialIdentity> identitiesFamiliar = credentialIdentityRepository.findByCreditHolderDniAndBeneficiaryDniAndCredentialStateIn(holderDni, beneficiaryDni, pendingAndActiveState);
            //this mean, the beneficiary familiar, have his own identity because he download the app, and the identity familiar created by the survey.
            if (identitiesFamiliar.size() == 2) {
                //validate if he doesnt have a benefit with this holder dni
                Optional<CredentialBenefits> opCredentialBenefit = credentialBenefitsRepository.findByCreditHolderDniAndBeneficiaryDniAndCredentialStateIn(holderDni, beneficiaryDni, pendingAndActiveState);
                return opCredentialBenefit.isEmpty();
            }
        }
        return false;
    }


    /**
     * Set holder in default and
     * Credit Credential
     * If exists and is emitted, revoke complete
     * If exists and is Pending Didi, revoke only local
     * If exists and is revoked, do nothing
     * If not exists, do nothing
     *
     * @param loan
     * @param holder
     * @return
     */
    public boolean revokeCredentialCredit(Loan loan, Person holder) throws CredentialException {

        log.info(String.format("Revoking Credentials credit for Loan %s and holder %d", loan.getIdBondareaLoan(), holder.getDocumentNumber()));

        holder.addLoanInDefault(loan);
        personRepository.save(holder);

        Optional<CredentialCredit> opCredit = credentialCreditRepository.findFirstByIdBondareaCreditOrderByDateOfIssueDesc(loan.getIdBondareaLoan());

        if (opCredit.isPresent()) {
            CredentialCredit credit = opCredit.get();

            //If exists and is revoked, do nothing
            if (credit.getCredentialState().getStateName().equals(CredentialStatesCodes.CREDENTIAL_REVOKE.getCode())) {
                log.info(String.format("Credential credit %d is revoked and in default, no need to update, credit %s for holder %d ", credit.getId(), credit.getIdBondareaCredit(), holder.getDocumentNumber()));
                return true;
            }

            //if credit is in default, only revoke.
            if (loan.getState().equals(LoanStateCodes.DEFAULT.getCode())) {
                if (this.revokeDefaultCredentialCredit(credit)) {
                    log.info(String.format("The credential for loan %s has been revoked for default successfully", credit.getIdBondareaCredit()));
                    return true;
                } else
                    log.error(String.format("The credential for loan %s was not set to default", credit.getIdBondareaCredit()));
                return false;
            }

        } else {//If not exists, do nothing
            log.info(String.format("No revoke Credentials credit for Loan %s not exists", loan.getIdBondareaLoan()));

        }

        return false;
    }


    /**
     * If exists and is emitted, revoke complete
     * If exists and is Pendiing Didi, revoke only local
     *
     * @param creditToRevoke
     * @return
     * @throws CredentialException
     */
    private boolean revokeDefaultCredentialCredit(CredentialCredit creditToRevoke) throws CredentialException {
        //revoke the previous credit and save the historic id
        log.info(String.format("Set Credential is in default for %s dni %d", creditToRevoke.getIdBondareaCredit(), creditToRevoke.getCreditHolderDni()));

        return this.revokeComplete(creditToRevoke, RevocationReasonsCodes.DEFAULT.getCode());

    }




    //TODO all of the methods of revocation, could be separated in a special service

    /**
     * Revocation with the business logic.
     * For particular revocations use, this.revokeComplete()
     *
     * @param id
     * @return
     */
    public boolean revokeCredential(Long id, String reasonCode) throws CredentialException {
        boolean haveRevokeOk = true;
        CredentialTypesCodes credentialType;

        log.info("Filtering credential with id: " + id);
        Optional<Credential> opCredentialToRevoke = getCredentialById(id);
        if (opCredentialToRevoke.isPresent()) {
            Credential credentialToRevoke = opCredentialToRevoke.get();

            //get the credential type
            try {
                credentialType = CredentialTypesCodes.getEnumByStringValue(credentialToRevoke.getCredentialDescription());
            } catch (IllegalArgumentException ex) {
                log.error("Impossible to revoke credential. There is no credential with type " + credentialToRevoke.getCredentialDescription());
                return false;
            }

            //todo rollback if fail
            log.info("credential type of : " + credentialType.getCode());
            switch (credentialType) {
                case CREDENTIAL_DWELLING:
                case CREDENTIAL_ENTREPRENEURSHIP:
                case CREDENTIAL_BENEFITS_FAMILY:
                    haveRevokeOk = this.revokeComplete(credentialToRevoke, reasonCode);
                    break;

                case CREDENTIAL_IDENTITY:
                    //find all the identities that the dni of the holder is into. (with state active or pending)
                    List<CredentialState> activePendingStates = credentialStateRepository.findByStateNameIn(List.of(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), CredentialStatesCodes.PENDING_DIDI.getCode()));
                    List<CredentialIdentity> holderIdentities = credentialIdentityRepository.findByCreditHolderDniAndCredentialStateIn(credentialToRevoke.getCreditHolderDni(), activePendingStates);

                    if (holderIdentities.size() == 0) {
                        log.info("There is no credential type " + credentialType.getCode() + " to revoke! The credentials are not in state pending or active");
                        haveRevokeOk = false;
                    }
                    for (Credential credential : holderIdentities) {
                        haveRevokeOk = this.revokeComplete(credential, reasonCode);
                    }

                    break;

                case CREDENTIAL_IDENTITY_FAMILY:
                    //revoke the identities of the familiar: the one created by the survey and if it exists, the one created because the person download the app. (with state active or pending)
                    activePendingStates = credentialStateRepository.findByStateNameIn(List.of(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), CredentialStatesCodes.PENDING_DIDI.getCode()));
                    List<CredentialIdentity> familiarIdentities = credentialIdentityRepository.findByCreditHolderDniAndBeneficiaryDniAndCredentialStateIn(credentialToRevoke.getCreditHolderDni(),
                            credentialToRevoke.getBeneficiaryDni(), activePendingStates);

                    if (familiarIdentities.size() == 0) {
                        log.info("There is no credential type " + credentialType.getCode() + " to revoke! The credentials are not in state pending or active");
                        haveRevokeOk = false;
                    }
                    for (Credential credential : familiarIdentities) {
                        haveRevokeOk = this.revokeComplete(credential, reasonCode);
                    }

                    break;

                case CREDENTIAL_BENEFITS:
                    //revoke benefit if the holder does not have another credit(active or pending, and did not finish) and also revoke benefits family .
                    activePendingStates = credentialStateRepository.findByStateNameIn(List.of(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), CredentialStatesCodes.PENDING_DIDI.getCode()));
                    List<CredentialCredit> creditsActivePending = credentialCreditRepository.findByCreditHolderDniAndCredentialStateIn(credentialToRevoke.getCreditHolderDni(), activePendingStates);
                    if (creditsActivePending.size() == 0) {
                        haveRevokeOk = this.revokeComplete(credentialToRevoke, reasonCode);

                        if (haveRevokeOk) {
                            //get the familiar benefits in which the holder is within
                            List<CredentialBenefits> familiarBenefits = credentialBenefitsRepository.findByCreditHolderDniAndCredentialStateInAndBeneficiaryType(credentialToRevoke.getCreditHolderDni(), activePendingStates, PersonTypesCodes.FAMILY.getCode());
                            for (CredentialBenefits familiarBenefit : familiarBenefits) {
                                haveRevokeOk = this.revokeComplete(familiarBenefit, reasonCode);
                            }
                        }
                    } else {
                        log.info("Impossible to revoke credential benefit. There are credits in state active or pending.");
                        haveRevokeOk = false;
                    }

                    break;

                case CREDENTIAL_CREDIT:
                    //get the credit credential to get the group
                    Optional<CredentialCredit> credentialCredit = credentialCreditRepository.findById(credentialToRevoke.getId());
                    if (credentialCredit.isPresent()) {
                        //get the group that is not revoked
                        List<CredentialCredit> creditsGroup = this.getCreditGroup(credentialCredit.get().getIdGroup());
                        //for each holder credit -> revoke credit -> revoke benefits -> revoke familiar benefits
                        for (CredentialCredit credit : creditsGroup) {
                            haveRevokeOk = this.revokeComplete(credit, reasonCode); //todo validate successful revocation to continue

                            //get benefits with holder dni (holder benefits and familiar benefits)
                            activePendingStates = credentialStateRepository.findByStateNameIn(List.of(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), CredentialStatesCodes.PENDING_DIDI.getCode()));
                            List<CredentialBenefits> benefits = credentialBenefitsRepository.findByCreditHolderDniAndCredentialStateIn(credit.getCreditHolderDni(), activePendingStates);

                            if (benefits.size() == 0) {
                                log.info("For the dni holder: " + credit.getCreditHolderDni() + " there is no benefits in active nor pending");
                            } else {
                                for (CredentialBenefits benefit : benefits) {
                                    haveRevokeOk = this.revokeComplete(benefit, reasonCode);
                                }
                            }
                        }
                    } else {
                        log.error("Error you are trying to revoke a credit that no longer exist, id: " + credentialToRevoke.getId());
                        haveRevokeOk = false;
                    }

                    break;
            }
        } else {
            //todo throw non-existent credential ?
            log.error("Error you are trying to revoke a non existent credential " + id);
            haveRevokeOk = false;
        }

        return haveRevokeOk;
    }

    /**
     * Get the credit group with the idGroup as long as the credits are not revoked.
     *
     * @param idGroup
     * @return List<CredentialCredit>
     */
    private List<CredentialCredit> getCreditGroup(String idGroup) {
        List<CredentialState> activePendingStates = credentialStateRepository.findByStateNameIn(List.of(CredentialStatesCodes.CREDENTIAL_ACTIVE.getCode(), CredentialStatesCodes.PENDING_DIDI.getCode()));
        //get the group if it is not revoked
        List<CredentialCredit> creditsGroup = credentialCreditRepository.findByIdGroupAndCredentialStateIn(idGroup, activePendingStates);

        return creditsGroup;
    }

    public boolean revokeComplete(Credential credentialToRevok, RevocationReasonsCodes revocationReasonsCodes) throws CredentialException {
        return this.revokeComplete(credentialToRevok, revocationReasonsCodes.getCode());
    }

    /**
     * If exists and is emitted, revoke complete
     * If exists and is Pendiing Didi, revoke only local
     *
     * @param credentialToRevoke
     * @return boolean
     */
    public boolean revokeComplete(Credential credentialToRevoke, String reasonCode) throws CredentialException {

        log.info("Starting complete revoking process for credential id: " + credentialToRevoke.getId() + " | credential type: " + credentialToRevoke.getCredentialDescription() + " holder " + credentialToRevoke.getCreditHolderDni() + " beneficiary " + credentialToRevoke.getBeneficiaryDni());
        //revoke on didi if credential was emitted
        if (credentialToRevoke.isEmitted()) {
            if (didiService.didiDeleteCertificate(credentialToRevoke.getIdDidiCredential())) {
                // if didi fail the credential need to know that is needed to be revoked (here think in the best resolution).
                // if this revoke came from the revocation business we will need to throw an error to rollback any change done before.
                return this.revokeCredentialOnlyOnSemillas(credentialToRevoke, reasonCode);
            } else {
                String message = String.format("Error to delete Certificate %d on Didi ", credentialToRevoke.getId());
                log.error(message);
                throw new CredentialException(message);
            }
        } else
            return this.revokeCredentialOnlyOnSemillas(credentialToRevoke, reasonCode);

    }


    /**
     * Revoke only for internal usage. Only revokes the credential on the DB.
     *
     * @param credentialToRevoke
     * @return boolean
     */

    public boolean revokeCredentialOnlyOnSemillas(Credential credentialToRevoke, String reasonCode) throws CredentialException {
        log.info("Revoking the credential " + credentialToRevoke.getId() + " with reason " + reasonCode);
        boolean haveRevoke = true;

        Optional<RevocationReason> reason = revocationReasonRepository.findByReason(reasonCode);
        if (reason.isPresent()) {
            //validate if the credential is in db
            Optional<Credential> opCredential = this.getCredentialById(credentialToRevoke.getId());
            if (opCredential.isEmpty()) {
                haveRevoke = false;
                log.error(String.format("The credential with id: %d is not in the database", credentialToRevoke.getId()));
            } else {

                Credential credential = opCredential.get();

                //revoke if the credential is not revoked yet
                if (this.isCredentialRevoked(credential)) {
                    log.info(String.format("The credential %d has already been revoked", credential.getId()));
                    haveRevoke = false;
                } else {
                    //revoke
                    Optional<CredentialState> opStateRevoke = credentialStateService.getCredentialRevokeState();
                    credentialToRevoke.setCredentialState(opStateRevoke.get());
                    credentialToRevoke.setRevocationReason(reason.get());
                    credentialToRevoke.setDateOfRevocation(DateUtil.getLocalDateTimeNow());
                    credentialRepository.save(credentialToRevoke);
                    log.info(String.format("Credential with id %d has been revoked!", credentialToRevoke.getId())); //then append also the reason
                }

            }

        } else {
            String message = String.format("Can't find reason with code: %s ", reasonCode);
            log.error(message);
            throw new CredentialException(message);
        }

        return haveRevoke;
    }

    public Optional<CredentialCredit> getCredentialCreditForLoan(Loan loan) {
        Optional<CredentialCredit> opCreditExistence = credentialCreditRepository.findFirstByIdBondareaCreditOrderByDateOfIssueDesc(loan.getIdBondareaLoan());
        return opCreditExistence;
    }


    public Boolean isCredentialRevoked(Credential credential) throws CredentialException {
        Optional<CredentialState> opStateRevoke = credentialStateService.getCredentialRevokeState();

         return (credential.getCredentialState().equals(opStateRevoke.get()));
    }

    public Boolean isCredentialActive(Credential credential) throws CredentialException {
        Optional<CredentialState> opStateActive = credentialStateService.getCredentialActiveState();

        return (credential.getCredentialState().equals(opStateActive.get()));
    }

    public Boolean isCredentialPendingDidi(Credential credential) throws CredentialException {
        Optional<CredentialState> opStatePendingDidi = credentialStateService.getCredentialPendingDidiState();

        return (credential.getCredentialState().equals(opStatePendingDidi.get()));
    }
}

