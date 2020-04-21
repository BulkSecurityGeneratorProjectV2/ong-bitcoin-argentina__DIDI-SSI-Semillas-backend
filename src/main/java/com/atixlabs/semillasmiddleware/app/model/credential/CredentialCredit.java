package com.atixlabs.semillasmiddleware.app.model.credential;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;


@Getter
@Setter
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(referencedColumnName="id")
public class CredentialCredit extends Credential {

    private Long idCredit;

    private String creditName;

    private Long idGroup;

    private String groupName;

    private String rol;

    private String currentCycle;

    private String creditState;

    private Double amount;

    private Long dniBeneficiary;

    @Transient
    private String credentialType = "CredentialCredit";


    @Override
    public String toString() {
        return "CredentialCredit{" +
                "idCredit=" + idCredit +
                ", creditName='" + creditName + '\'' +
                ", idGroup=" + idGroup +
                ", groupName='" + groupName + '\'' +
                ", rol='" + rol + '\'' +
                ", currentCycle='" + currentCycle + '\'' +
                ", creditState='" + creditState + '\'' +
                ", amount=" + amount +
                ", dniBeneficiary=" + dniBeneficiary +
                ", credentialType=" + credentialType +
                '}' + super.toString();
    }
}
