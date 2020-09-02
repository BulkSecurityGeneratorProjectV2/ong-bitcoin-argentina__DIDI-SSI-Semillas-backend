package com.atixlabs.semillasmiddleware.excelparser.app.categories;

import com.atixlabs.semillasmiddleware.excelparser.app.constants.Categories;
import com.atixlabs.semillasmiddleware.excelparser.app.constants.DidiSyncStatus;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerDto;
import com.atixlabs.semillasmiddleware.excelparser.app.dto.AnswerRow;
import com.atixlabs.semillasmiddleware.excelparser.dto.ProcessExcelFileResult;
import com.atixlabs.semillasmiddleware.util.StringUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class DwellingCategory implements Category {

    String categoryOriginalName;
    private Categories categoryName = Categories.DWELLING_CATEGORY_NAME;

    AnswerDto brick; //ladrillo
    AnswerDto lock; //chapa
    AnswerDto wood; //madera
    AnswerDto paperBoard; //carton
    AnswerDto district; //distrito de residencia
    AnswerDto dwellingCondition; //condiciones de vivienda
    AnswerDto holdingType; //tipo de tenencia
    AnswerDto dwellingType; //vivienda
    AnswerDto lightInstallation; //instalacion de luz
    AnswerDto generalConditions; //condiciones generales
    AnswerDto neighborhoodType; //tipo de barrio
    AnswerDto basicServicies; //servicios basicos
    AnswerDto gas; //red de gas
    AnswerDto carafe; //garrafa
    AnswerDto water; //red de agua
    AnswerDto watterWell; //pozo / bomba
    AnswerDto antiquity; //antiguedad
    AnswerDto numberOfEnvironments; //cantidad de ambientes
    AnswerDto rental; //monto alquiler

    public DwellingCategory(String categoryOriginalName){
        this.brick= new AnswerDto(DidiSyncStatus.BRICK);
        this.lock= new AnswerDto(DidiSyncStatus.LOCK);
        this.wood= new AnswerDto(DidiSyncStatus.WOOD);
        this.paperBoard= new AnswerDto(DidiSyncStatus.PAPERBOARD);
        this.district= new AnswerDto(DidiSyncStatus.DISTRICT);
        this.dwellingCondition= new AnswerDto(DidiSyncStatus.DWELLING_CONDITION);
        this.holdingType= new AnswerDto(DidiSyncStatus.HOLDING_TYPE);
        this.dwellingType= new AnswerDto(DidiSyncStatus.DWELLING_TYPE);
        this.lightInstallation= new AnswerDto(DidiSyncStatus.LIGHT_INSTALLATION);
        this.generalConditions= new AnswerDto(DidiSyncStatus.GENERAL_CONDITIONS);
        this.neighborhoodType= new AnswerDto(DidiSyncStatus.NEIGHBORHOOD_TYPE);
        this.basicServicies= new AnswerDto(DidiSyncStatus.BASIC_SERVICES);
        this.gas= new AnswerDto(DidiSyncStatus.GAS);
        this.carafe= new AnswerDto(DidiSyncStatus.CARAFE);
        this.water= new AnswerDto(DidiSyncStatus.WATER);
        this.watterWell= new AnswerDto(DidiSyncStatus.WATTER_WELL);
        this.antiquity= new AnswerDto(DidiSyncStatus.ANTIQUITY);
        this.numberOfEnvironments= new AnswerDto(DidiSyncStatus.NUMBER_OF_ENVIRONMENTS);
        this.rental= new AnswerDto(DidiSyncStatus.RENTAL);

        this.categoryOriginalName = categoryOriginalName;
    }

    public void loadData(AnswerRow answerRow, ProcessExcelFileResult processExcelFileResult) {
        String question = StringUtil.toUpperCaseTrimAndRemoveAccents(answerRow.getQuestion());

        DidiSyncStatus questionMatch = DidiSyncStatus.getEnumByStringValue(question);

        if(questionMatch==null)
            return;

        switch (questionMatch){
            case BRICK:
                this.brick.setAnswer(answerRow, processExcelFileResult);
                break;
            case LOCK:
                this.lock.setAnswer(answerRow, processExcelFileResult);
                break;
            case WOOD:
                this.wood.setAnswer(answerRow, processExcelFileResult);
                break;
            case PAPERBOARD:
                this.paperBoard.setAnswer(answerRow, processExcelFileResult);
                break;
            case DISTRICT:
                this.district.setAnswer(answerRow, processExcelFileResult);
                break;
            case DWELLING_CONDITION:
                this.dwellingCondition.setAnswer(answerRow, processExcelFileResult);
                break;
            case HOLDING_TYPE:
                this.holdingType.setAnswer(answerRow, processExcelFileResult);
                break;
            case DWELLING_TYPE:
                this.dwellingType.setAnswer(answerRow, processExcelFileResult);
                break;
            case LIGHT_INSTALLATION:
                this.lightInstallation.setAnswer(answerRow, processExcelFileResult);
                break;
            case GENERAL_CONDITIONS:
                this.generalConditions.setAnswer(answerRow, processExcelFileResult);
                break;
            case NEIGHBORHOOD_TYPE:
                this.neighborhoodType.setAnswer(answerRow, processExcelFileResult);
                break;
            case BASIC_SERVICES:
                this.basicServicies.setAnswer(answerRow, processExcelFileResult);
                break;
            case GAS:
                this.gas.setAnswer(answerRow, processExcelFileResult);
                break;
            case CARAFE:
                this.carafe.setAnswer(answerRow, processExcelFileResult);
                break;
            case WATER:
                this.water.setAnswer(answerRow, processExcelFileResult);
                break;
            case WATTER_WELL:
                this.watterWell.setAnswer(answerRow, processExcelFileResult);
                break;
            case ANTIQUITY:
                this.antiquity.setAnswer(answerRow, processExcelFileResult);
                break;
            case NUMBER_OF_ENVIRONMENTS:
                this.numberOfEnvironments.setAnswer(answerRow, processExcelFileResult);
                break;
            case RENTAL:
                this.rental.setAnswer(answerRow, processExcelFileResult);

        }
    }

    @Override
    public  String getCategoryUniqueName(){
        return categoryOriginalName;
    }

    @Override
    public Categories getCategoryName(){return categoryName;}

    @Override
    public boolean isValid(ProcessExcelFileResult processExcelFileResult) {
        List<AnswerDto> answers = new LinkedList<>();
        answers.add(brick);
        answers.add(this.lock);
        answers.add(this.wood);
        answers.add(this.paperBoard);
        answers.add(this.district);
        answers.add(this.dwellingCondition);
        answers.add(this.holdingType);
        answers.add(this.dwellingType);
        answers.add(this.lightInstallation);
        answers.add(this.generalConditions);
        answers.add(this.neighborhoodType);
        answers.add(this.basicServicies);
        answers.add(this.gas);
        answers.add(this.carafe);
        answers.add(this.water);
        answers.add(this.watterWell);
        answers.add(this.antiquity);
        answers.add(this.numberOfEnvironments);
        answers.add(this.rental);


        List<Boolean> validations = answers.stream().map(answerDto -> answerDto.isValid(processExcelFileResult, categoryOriginalName)).collect(Collectors.toList());
        return validations.stream().allMatch(v->v);
    }

    @Override
    public boolean isEmpty(){
        return brick.answerIsEmpty() && lock.answerIsEmpty() && wood.answerIsEmpty() && paperBoard.answerIsEmpty()
                && district.answerIsEmpty() && dwellingCondition.answerIsEmpty()
                && holdingType.answerIsEmpty() && dwellingType.answerIsEmpty()
                && lightInstallation.answerIsEmpty() && generalConditions.answerIsEmpty()
                && neighborhoodType.answerIsEmpty() && basicServicies.answerIsEmpty()
                && gas.answerIsEmpty() && carafe.answerIsEmpty() && water.answerIsEmpty()
                && watterWell.answerIsEmpty() && antiquity.answerIsEmpty()
                && numberOfEnvironments.answerIsEmpty() && rental.answerIsEmpty();
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    public String getDwellingType(){
        return (String) this.dwellingType.getAnswer();
    }

    public String getHoldingType(){
        return (String) this.holdingType.getAnswer();
    }

    public String getDistrict(){
        return (String) this.district.getAnswer();
    }

    @Override
    public String toString() {
        return "DwellingCategory{" +
                "categoryOriginalName='" + categoryOriginalName + '\'' +
                ", categoryName=" + categoryName +
                ", brick=" + brick +
                ", lock=" + lock +
                ", wood=" + wood +
                ", paperBoard=" + paperBoard +
                ", district=" + district +
                ", dwellingCondition=" + dwellingCondition +
                ", holdingType=" + holdingType +
                ", dwellingType=" + dwellingType +
                ", lightInstallation=" + lightInstallation +
                ", generalConditions=" + generalConditions +
                ", neighborhoodType=" + neighborhoodType +
                ", basicServicies=" + basicServicies +
                ", gas=" + gas +
                ", carafe=" + carafe +
                ", water=" + water +
                ", watterWell=" + watterWell +
                ", antiquity=" + antiquity +
                ", numberOfEnvironments=" + numberOfEnvironments +
                ", rental=" + rental +
                '}';
    }
}
