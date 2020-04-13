package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialTraining extends Credential{

    //Data ?
    //titular y familiar
    /*
    Nombre de capacitacion
    Tema
    Fecha
    Hs de capacitacion (duracion)
    */
}
