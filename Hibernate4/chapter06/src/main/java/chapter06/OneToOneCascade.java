
package chapter06;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class OneToOneCascade {
    @Id
    private int id;
    @Column
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    private AddressCascade address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressCascade getAddress() {
        return address;
    }

    public void setAddress(AddressCascade address) {
        this.address = address;
    }
}
