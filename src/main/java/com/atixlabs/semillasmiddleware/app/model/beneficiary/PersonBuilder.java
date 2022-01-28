package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import com.atixlabs.semillasmiddleware.app.model.excel.Form;

public class PersonBuilder {

    private Person person;

    public PersonBuilder fromForm(Form form){
        person = new Person();
        person.setDocumentNumber(form.getNumeroDniBeneficiario());
        person.setFirstName(form.getNombreBeneficiario());
        person.setLastName(form.getApellidoBeneficiario());
        person.setBirthDate(form.getFechaNacimientoBeneficiario());
        person.setGender(form.getGeneroBeneficiario());

        return this;
    }

    public Person build(){
        return person;
    }
}
