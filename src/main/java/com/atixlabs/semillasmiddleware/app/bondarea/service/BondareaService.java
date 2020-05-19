package com.atixlabs.semillasmiddleware.app.bondarea.service;

import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanDto;
import com.atixlabs.semillasmiddleware.app.bondarea.dto.BondareaLoanResponse;
import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.BondareaLoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.model.constants.LoanStatusCodes;
import com.atixlabs.semillasmiddleware.app.bondarea.repository.LoanRepository;
import com.atixlabs.semillasmiddleware.util.DateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BondareaService {


    private LoanRepository loanRepository;

    @Autowired
    public BondareaService (LoanRepository loanRepository){
        this.loanRepository = loanRepository;
    }

    @Value("${bondarea.base_url}")
    private String serviceURL;

    @Value("${bondarea.access_key}")
    private String access_key;

    @Value("${bondarea.access_token}")
    private String access_token;

    @Value("${bondarea.idm}")
    private String idm;

    // ID de la cuenta representado
    //private String idAccount;

    // Valores separados por "|" (pipe) de las columnas a mostrar para cada registro. (Ej. sta|t|id|staInt|id_pg|pg|fOt|fPri|sv|usr|cuentasTag|dni|pp|ppt|m)
    private String loanColumns;

    // Valores separados por "|" (pipe) de los estados de préstamos a listar.  (Ej. 55|60)
   // private String loanstate;



    //contains json converter and date format configuration
    private Gson gson = null;

    //contains service link with factory
    private Retrofit retrofit = null;

    private BondareaEndpoint bondareaEndpoint = null;



    public void initializeBondareaApi() {
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();


        retrofit = new Retrofit.Builder()
                .baseUrl(serviceURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        bondareaEndpoint = retrofit.create(BondareaEndpoint.class);


        log.info("initialize bondarea api:");

    }

    /**
     * MOCK PURPOSE
     * @return BpndareaLoanDto
     */
    private BondareaLoanDto getMockBondareaLoan(){
        BondareaLoanDto loan = new BondareaLoanDto();
        loan.setIdBondareaLoan("1L");
        loan.setDni(24580963L);
        loan.setStatusFullDescription("Activo");
        loan.setIdGroup("group1");
        loan.setAmount((float) 1000);
        loan.setExpiredAmount((float) 0);
        loan.setCycle("Ciclo 1");
        loan.setCreationDate("27/04/2020");
        loan.setStatusDescription("");
        loan.setStatus(55);

        return loan;
    }


    /**
     * MOCK PURPOSE:
     * Synchronize new loans mock. It can be used to create loans for the 1st time.
     * Then to test the creation of credit credential and benefits credentials
     *
     * @param loanState
     * @param idBondareaLoan
     * @param date
     * @return
     */
    public List<BondareaLoanDto> getLoansMock(String loanState, String idBondareaLoan, String date){
        List<BondareaLoanDto> loans = new ArrayList<>();

        BondareaLoanDto loan = getMockBondareaLoan();
        loans.add(loan);

        BondareaLoanDto loan2 = getMockBondareaLoan();
        loan2.setIdBondareaLoan("2L");
        loans.add(loan2);

        BondareaLoanDto loan3 = getMockBondareaLoan();
        loan3.setIdBondareaLoan("3L");
        loans.add(loan3);

        BondareaLoanDto loan4 = getMockBondareaLoan();
        loan4.setIdBondareaLoan("4L");
        loans.add(loan4);

        return loans;
    }

    /**
     * MOCK PURPOSE:
     * Synchronize loans. This is used the 2nd time to update the loans that have already been saved.
     *
     * @param loanState
     * @param idBondareaLoan
     * @param date
     * @return
     */
    public List<BondareaLoanDto> getLoansMockSecond(String loanState, String idBondareaLoan, String date){
        List<BondareaLoanDto> loans = new ArrayList<>();

        //id 1 is not with state 55

        //id 2 is not with state 55

        //loan 3 modified cycle
        BondareaLoanDto loan3 = getMockBondareaLoan();
        loan3.setIdBondareaLoan("3L");
        loan3.setCycle("Ciclo 2");
        loans.add(loan3);

        //loan 4 is in default
        BondareaLoanDto loan4 = getMockBondareaLoan();
        loan4.setIdBondareaLoan("4L");
        loan4.setExpiredAmount((float) 10500);
        loans.add(loan4);

        //new loan
        BondareaLoanDto loan5 = getMockBondareaLoan();
        loan5.setIdBondareaLoan("5L");
        loans.add(loan5);

        return loans;
    }

    private List<BondareaLoanDto> getLoansFinalizedMock(String code, String idBondareaLoan, String date){
        List<BondareaLoanDto> loans = new ArrayList<>();
        //loan 2 is finalize
        BondareaLoanDto loan2 = getMockBondareaLoan();
        loan2.setIdBondareaLoan("2L");
        loan2.setStatusDescription("Finalizado");
        loan2.setStatus(60);
        loans.add(loan2);

        return loans;
    }



    /**
     * Productive Version !
     * Bondarea url documentation: https://docs.google.com/document/d/1eb46Rr67EZwolSzNlg4muwHyXEvbHpRldtttWZSnpBU/edit
     *
     * @param loanState
     * @param idBondareaLoan
     * @param date
     * @return List<BondareaLoanDto>
     * @throws Exception
     */
    public List<BondareaLoanDto> getLoans(String loanState, String idBondareaLoan, String date) throws Exception {

        initializeBondareaApi();
        log.info("getBondareaLoans:");

        //Fill url params
        loanColumns = "sta|t|id|staInt|id_pg|pg|fOt|fPri|sv|usr|cuentasTag|dni|pp|ppt|m";


        Call<BondareaLoanResponse> callSync = bondareaEndpoint.getLoans("comunidad","wspre", "prerepprestamos", access_key, access_token, idm, loanColumns, loanState, idBondareaLoan, date);

        BondareaLoanResponse bondareaLoanResponse;
        try {
            Response<BondareaLoanResponse> response = callSync.execute();

            if (response.code() == 200) {
                bondareaLoanResponse = response.body();
                log.info("Bondarea get loans has been successfully executed " + response.body());
            }
            else
            {
                return Collections.emptyList();
            }

        }catch(JsonSyntaxException  ex){
            log.error(" Bondarea retrieved object does not match with model ", ex);
            throw new Exception("retrived object does not match with model ");
        }
        catch (SocketTimeoutException ex){
                log.error(" Bondarea timeout ", ex);
             throw new Exception("Tiemout, please try again");
            }

        catch(Exception ex){
            log.error("Bondarea error ", ex);
            throw new Exception("Error, please try again");
        }

        return bondareaLoanResponse.getLoans();

    }


    /**
     * Getting the new loans from synchronize, for each loan -> find if exists on DB:
     *      if not -> create.
     *      if exists -> merge with the new one.
     * 2nd step -> get from DB the ones that has not been modified -> set them to pending.
     * @param newLoans
     */
    public void updateExistingLoans(List<Loan> newLoans) {
        LocalDateTime updateTime = DateUtil.getLocalDateTimeNowWithFormat("yyyy-MM-dd HH:mm");

        //update or create loans
        for (Loan loanToSave : newLoans) {
            loanToSave.setModifiedTime(updateTime);
            //set the loan on active, whether is a new one or not. The one that not came would be set on pending
            loanToSave.setStatus(LoanStatusCodes.ACTIVE.getCode());
            //if the newLoan existed previously -> update. Else create.
            Optional<Loan> opLoanToUpdate = loanRepository.findByIdBondareaLoan(loanToSave.getIdBondareaLoan());
            //There is a previous loan
            if (opLoanToUpdate.isPresent()) {
                //update
                Loan loanToUpdate = opLoanToUpdate.get();
                loanToUpdate.merge(loanToSave);
                loanRepository.save(loanToUpdate);
            } else {
                //create
                loanRepository.save(loanToSave);
            }
        }

        List<Loan> loansToReview = loanRepository.findAllByModifiedTimeNotAndModifiedTimeNotNull(updateTime);
        for (Loan existingLoan : loansToReview) {
            //this loan has been deleted on the new list, so this loan needs to be checked (is eliminated or has finished)
            existingLoan.setStatus(LoanStatusCodes.PENDING.getCode());
            loanRepository.save(existingLoan);
        }

        log.info("Synchronize Ended Successfully");
    }

    /**
     * Determinate for each loan in pending state whether it has been canceled or has finished.
     */
    public void setPendingLoansFinalStatus(){
        List<Loan> pendingLoans = loanRepository.findAllByStatus(LoanStatusCodes.PENDING.getCode());

        for (Loan pendingLoan : pendingLoans) {
            try {
                List<BondareaLoanDto> loansDto = this.getLoans(BondareaLoanStatusCodes.FINALIZED.getCode(), pendingLoan.getIdBondareaLoan(), "");

                if (loansDto.size() > 0) {
                    // if there is a loan will be the one we filtered with the same id and status finalized
                    pendingLoan.setStatus(LoanStatusCodes.FINALIZED.getCode());
                    loanRepository.save(pendingLoan);
                } else {
                    // if there is no loan, is because has been cancelled
                    pendingLoan.setStatus(LoanStatusCodes.CANCELLED.getCode());
                    loanRepository.save(pendingLoan);
                }
            } catch (Exception ex) {
                log.error("Error determining pending loans " + ex.getMessage());
            }
        }
    }


    /**
     * Determinate for each loan in pending state whether it has been canceled or has finished.
     */
    public void setPendingLoansFinalStatusMock(){
        List<Loan> pendingLoans = loanRepository.findAllByStatus(LoanStatusCodes.PENDING.getCode());

        for (Loan pendingLoan : pendingLoans) {
            try {

                List<BondareaLoanDto> loansDto = this.getLoansFinalizedMock(BondareaLoanStatusCodes.FINALIZED.getCode(), pendingLoan.getIdBondareaLoan(), "");
                if (loansDto.size() > 0) {
                    // if there is a loan will be the one we filtered with the same id and status finalized
                    pendingLoan.setStatus(LoanStatusCodes.FINALIZED.getCode());
                    loanRepository.save(pendingLoan);
                } else {
                    // if there is no loan, is because has been cancelled
                    pendingLoan.setStatus(LoanStatusCodes.CANCELLED.getCode());
                    loanRepository.save(pendingLoan);
                }
            } catch (Exception ex) {
                log.error("Error determining pending loans " + ex.getMessage());
            }
        }
    }




}
