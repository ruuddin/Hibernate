
package chapter06;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class AddressCascade {

    @Id
    private int id;
    @Column
    private String street;

    @OneToOne(mappedBy = "address")
    private OneToOneCascade parentCascade;

    public OneToOneCascade getParentCascade() {
        return parentCascade;
    }

    public void setParentCascade(OneToOneCascade parentCascade) {
        this.parentCascade = parentCascade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    @Column
    private int zipCode;
}
