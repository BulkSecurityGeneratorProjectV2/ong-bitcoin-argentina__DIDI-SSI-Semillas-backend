package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum EntrepreneurshipQuestion implements CategoryQuestion {
    TYPE("TIPO DE EMPRENDIMIENTO"),
    ACTIVITY_START_DATE("FECHA DE INICIO / REINICIO"),
    MAIN_ACTIVITY("ACTIVIDAD PRINCIPAL"),
    NAME("NOMBRE EMPRENDIMIENTO"),
    ADDRESS("DIRECCION"),
    ACTIVITY_ENDING_DATE("FIN DE LA ACTIVIDAD") {
        @Override
        public boolean isRequired() {
            return false;
        }
    };

    private String questionName;
    static final Map<String, EntrepreneurshipQuestion> questionsMap = Arrays.stream(values()).collect(Collectors.toMap(EntrepreneurshipQuestion::getQuestionName, p->p));

    EntrepreneurshipQuestion(String questionName) {
        this.questionName = questionName;
    }

    public static EntrepreneurshipQuestion get(String questionName){
        return questionsMap.get(questionName);
    }

    private String getQuestionName(){
        return this.questionName;
    }

    public boolean isRequired() {
        return true;
    }
}
