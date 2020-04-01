package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import com.atixlabs.semillasmiddleware.app.model.credential.Credential;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table
public class Person {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private String documentType; //TODO enum or class

    @Column(unique = true)
    private Long documentNumber;

    private String name;

    private LocalDate birthDate;

    @OneToMany
    private List<Credential> credentials;

    /*
    kinsman (pariente), p1,p2, tiporelacion (kind of kinship)

    tipo de relacion
            hijo
    conyugue
            familiar*/
}
