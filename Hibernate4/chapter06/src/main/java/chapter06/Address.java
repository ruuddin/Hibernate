
package chapter06;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Address {

    @Id
    private int id;

    @Column
    private String street;

    @OneToOne(mappedBy = "address")
    private OneToOneRelationship parent;

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

    public OneToOneRelationship getParent() {
        return parent;
    }

    public void setParent(OneToOneRelationship parent) {
        this.parent = parent;
    }

    @Column
    private int zipCode;
}
