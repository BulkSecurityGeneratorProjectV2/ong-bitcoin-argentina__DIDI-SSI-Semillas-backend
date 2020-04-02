package com.atixlabs.semillasmiddleware.app.model.credential;

import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@Table
@Inheritance( strategy = InheritanceType.JOINED )
public abstract class Credential {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idDidiIssueer;

    private Long idDidiReceptor;

    @Column(unique = true)
    private Long idDidiCredential;

    private Long idHistorical;

    @Temporal(TemporalType.DATE)
    private Date dateOfIssue;

    @Temporal(TemporalType.DATE)
    private Date dateOfExpiry;

    /**
     *
     */
    private Long idRelatedCredential;

    @ManyToOne
    private Person beneficiary;

}
