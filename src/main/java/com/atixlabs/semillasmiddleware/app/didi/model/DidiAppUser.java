package com.atixlabs.semillasmiddleware.app.didi.model;

import com.atixlabs.semillasmiddleware.app.didi.constant.DidiSyncStatus;
import com.atixlabs.semillasmiddleware.app.didi.dto.DidiAppUserDto;
import com.atixlabs.semillasmiddleware.app.model.beneficiary.Person;
import com.atixlabs.semillasmiddleware.app.model.credentialState.CredentialState;
import com.atixlabs.semillasmiddleware.security.model.AuditableEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Getter
@Setter
@Table(name = "didi_app_user")
@Entity
@ToString
public class DidiAppUser extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected Long dni;
    protected String did;

    protected String syncStatus;

    public DidiAppUser() {
        this.syncStatus = DidiSyncStatus.SYNC_MISSING.getCode();
    }
    public void loadFromDto(DidiAppUserDto didiAppUserDto) {
        this.dni = didiAppUserDto.getDni();
        this.did = didiAppUserDto.getDid();
        this.syncStatus = DidiSyncStatus.SYNC_MISSING.getCode();
    }


}
