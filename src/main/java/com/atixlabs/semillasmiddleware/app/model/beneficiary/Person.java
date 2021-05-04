package com.atixlabs.semillasmiddleware.app.model.beneficiary;

import com.atixlabs.semillasmiddleware.app.bondarea.model.Loan;
import com.atixlabs.semillasmiddleware.app.model.DIDHistoric.DIDHisotoric;
import com.atixlabs.semillasmiddleware.excelparser.app.categories.PersonCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table
public class Person {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long documentNumber;

    private String firstName;
    private String lastName;

    private LocalDate birthDate;

    private String gender;

    @OneToMany
    private List<DIDHisotoric> DIDIsHisotoric;

    @ManyToMany(fetch=FetchType.EAGER)
    protected List<Loan> defaults; //TODO must be a HashSet


    //TODO user this
    public boolean equalsIgnoreId(Person person1, Person person2) {
        return person1.getDocumentNumber().equals(person2.getDocumentNumber()) &&
                person1.getFirstName().equals(person2.getFirstName()) &&
                person1.getLastName().equals(person2.getLastName()) &&
                person1.getBirthDate().isEqual(person2.getBirthDate());
    }

    //TODO review (delete static and use this)
    public static Person getPersonFromPersonCategory(PersonCategory personCategory) {
        Person person = new Person();
        person.setDocumentNumber(personCategory.getDocumentNumber());
        person.setFirstName(personCategory.getName());
        person.setLastName(personCategory.getSurname());
        person.setBirthDate(personCategory.getBirthDate());
        person.setGender(personCategory.getGender());
        return person;
    }

    public boolean isInDefault(){return (this.defaults!=null ?  defaults.size()>0 : false);}


    public boolean removeLoanInDefault(Loan loan){
        if(this.isInDefault()){
            if(this.getDefaults().contains(loan)){
                return this.getDefaults().remove(loan);
            }
        }

        return false;
    }

    public void addLoanInDefault(Loan loan){
        if(this.getDefaults()==null){
            this.setDefaults(new ArrayList<Loan>());
        }
        if(!this.getDefaults().contains(loan)){
            this.getDefaults().add(loan);
        }
    }

    /*
    @JoinColumn(name = "ID_CREDENTIAL")
    @OneToMany
    private List<Credential> credentials;

*/

/*

    @OneToMany
    private List<Application> applications;
*/


    /*
    kinsman (pariente), p1,p2, tiporelacion (kind of kinship)

    tipo de relacion
            hijo
    conyugue
            familiar*/


}
