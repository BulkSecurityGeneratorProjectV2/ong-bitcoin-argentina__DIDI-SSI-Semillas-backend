package com.atixlabs.semillasmiddleware.excelparser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the final result of the processing of a file
 */
@NoArgsConstructor
@Getter
@Setter
public class ProcessExcelFileResult {

    private int totalReadRows = 0;
    private int totalValidRows = 0;
    private int totalErrorsRows = 0;
    private int totalEmptyRows = 0;
    private int totalProcessedForms = 0;
    private String fileName = null;
    private String zipName;

    private List<ExcelErrorDetail> errorRows = new ArrayList<>();
    @JsonIgnore
    private List<ExcelErrorDetail> debugRows = new ArrayList<>();

    public void addRowError(Integer errorHeader, String errorBody){
        this.errorRows.add(new ExcelErrorDetail(String.valueOf(errorHeader), errorBody));
        this.totalErrorsRows++;
    }

    public void addRowError(String errorHeader, String errorBody){
        this.errorRows.add(new ExcelErrorDetail(errorHeader, errorBody));
        this.totalErrorsRows++;
    }

    public void addTotalReadRow(){
        this.totalReadRows++;
    }
    public void addTotalValidRows(){
        this.totalValidRows++;
    }
    public void addEmptyRow(){this.totalEmptyRows++;}
    public void addTotalProcessedForms(){ this.totalProcessedForms++;}

    public void addRowDebug(String errorHeader, String errorBody){
        this.debugRows.add(new ExcelErrorDetail(errorHeader, errorBody));
    }

    @Override
    public String toString() {
        return "ProcessExcelFileResult{" +
                "totalReadRows=" + totalReadRows +
                ", totalValidRows=" + totalValidRows +
                ", totalErrorsRows=" + totalErrorsRows +
                ", totalEmptyRows=" + totalEmptyRows +
                ", totalProcessedForms=" + totalProcessedForms +
                ", fileName='" + fileName + '\'' +
                ", errorRows=" + errorRows +
                '}';
    }
}
