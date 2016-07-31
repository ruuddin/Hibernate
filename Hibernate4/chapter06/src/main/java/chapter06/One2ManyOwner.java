
package chapter06;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class One2ManyOwner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    @OrderBy("name ASC")
    private List<One2ManyAddress> addresses;

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

    public List<One2ManyAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<One2ManyAddress> addresses) {
        this.addresses = addresses;
    }
}
