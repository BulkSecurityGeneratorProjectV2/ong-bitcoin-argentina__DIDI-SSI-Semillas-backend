package com.atixlabs.semillasmiddleware.excelparser.app.service;

import com.atixlabs.semillasmiddleware.app.service.CredentialService;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.AnswerCategoryFactory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.Category;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.SurveyForm;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidCategoryException;
import com.atixlabs.semillasmiddleware.excelparser.exception.InvalidRowException;
import com.atixlabs.semillasmiddleware.excelparser.service.ExcelParseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SurveyExcelParseService extends ExcelParseService {

    /**
     * Levanta el archivo y lee linea por linea
     *
     * @param row
     * @return
     * @throws FileNotFoundException
     */

    @Autowired
    private AnswerCategoryFactory answerCategoryFactory;

    @Autowired
    CredentialService credentialService;

    /**
     * currentForm is shared between the whole Excel file.
     * it must be @Autowired to keep state of previous row key data,
     * in order to allow currentForm.isRowFromSameForm() to work properly.
     */
    @Autowired
    private SurveyForm currentForm;

    private List<SurveyForm> surveyFormList = new ArrayList<>();

    @Override
    public ProcessExcelFileResult processRow(Row currentRow, boolean hasNext, ProcessExcelFileResult processExcelFileResult){

        AnswerRow answerRow = null;
        try {
            answerRow = new AnswerRow(currentRow);
        } catch (InvalidRowException e) {
            processExcelFileResult.addRowError("("+currentRow.getRowNum()+"): "+ e.toString());
        }

        processExcelFileResult.addTotalRow();

        if (answerRow.isValid()){
            processExcelFileResult.addValidRows();
            processExcelFileResult.addInsertedRows();

            if (currentForm.isEmpty())
                currentForm.initialize(answerRow);

            if (!currentForm.isRowFromSameForm(answerRow)) {
                endOfFormHandler();
                currentForm.clearForm();
                currentForm.initialize(answerRow);
            }

            addCategoryDataIntoForm(answerRow, processExcelFileResult);
            log.info("OK:"+answerRow.toString());
        }
        else{
            //processExcelFileResult.addRowError("["+answerRow.getRowNum()+"]"+ answerRow.getErrorMessage());
            processExcelFileResult.addRowError(answerRow.getStringError());
            log.error("ERROR:"+answerRow.toString());
        }



        if(!hasNext)
            endOfFileHandler();

        return processExcelFileResult;
    }

    private void addCategoryDataIntoForm(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult){

        try {
            Category category = answerCategoryFactory.get(answerRow.getCategory());
            category.loadData(answerRow);
            currentForm.addCategory(category);
        }
        catch (Exception | InvalidCategoryException e) {
            processExcelFileResult.addRowError("("+answerRow.getRowNum()+"): "+ e.toString());
        }
    }

    private void endOfFormHandler(){
        log.info("endOfFormHandler -> adding form to surveyFormList");
        surveyFormList.add(currentForm);
    }
    private void endOfFileHandler(){
        log.info("endOfFileHandler -> checking errors and building credentials");
        //CHECK IF ALL FORMS AR OK:
        for (SurveyForm surveyForm : surveyFormList) {
            log.info(surveyForm.toString());
            credentialService.buildAllCredentialsFromForm(surveyForm);
        }
    }
}
