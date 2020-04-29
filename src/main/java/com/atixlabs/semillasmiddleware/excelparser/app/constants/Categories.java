package com.atixlabs.semillasmiddleware.excelparser.app.constants;

import com.atixlabs.semillasmiddleware.excelparser.app.categories.DwellingCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.EntrepreneurshipCategory;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Here the categories from the form must be identified.
 * code is mandatory
 * getAmount() if not Overridden will return 1
 * getLinkedClass() if not Overridden will return PersonCategory.class
 */

//Person AGRUPA BENEFICIARY/SPOUSE/CHILD/RELATIVE
//PERSON_CATEGORY_NAME("PERSONA"),
public enum Categories {
    BENEFICIARY_CATEGORY_NAME("DATOS DEL BENEFICIARIO"),
    SPOUSE_CATEGORY_NAME("DATOS DEL CONYUGE"),
    CHILD_CATEGORY_NAME("DATOS HIJO"){
        @Override
        public Integer getAmount() {
            return 11;
        }
    },
    KINSMAN_CATEGORY_NAME("OTRO MIEMBRO DE LA FAMILIA"){
        @Override
        public Integer getAmount() {
            return 3;
        }
    },
    ENTREPRENEURSHIP_CATEGORY_NAME("EMPRENDIMIENTO"){
        @Override
        public Class<?> getLinkedClass() {
            return EntrepreneurshipCategory.class;
        }
    },
    DWELLING_CATEGORY_NAME("VIVIENDA"){
        @Override
        public Class<?> getLinkedClass() {
            return DwellingCategory.class;
        }
    };

    private String code;
    Categories(String code) {this.code = code;}
    public String getCode(){return this.code;}
    static public List<Categories> getCodeList(){return Arrays.stream(values()).collect(Collectors.toList());}
    public Integer getAmount(){return 1;}
    public Class<?> getLinkedClass(){return PersonCategory.class;}
}
