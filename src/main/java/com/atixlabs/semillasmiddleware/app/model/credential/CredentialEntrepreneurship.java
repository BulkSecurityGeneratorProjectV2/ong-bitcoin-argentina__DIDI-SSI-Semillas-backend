package com.atixlabs.semillasmiddleware.app.model.credential;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialEntrepreneurship extends Credential {

    // Comercio, Producción,Servicio
    private String entrepreneurshipType; //TODO enum or new class?

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startActivity;

    private String mainActivity;

    private String entrepreneurshipName;

    private String entrepreneurshipAddress;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endActivity;
}
