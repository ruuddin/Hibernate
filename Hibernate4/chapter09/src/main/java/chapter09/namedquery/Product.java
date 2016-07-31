
package chapter09.namedquery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    @Column(unique = true, nullable = false)
    String name;
    @Column(unique = true, nullable = false)
    String description;
    @Column(unique = true, nullable = false)
    Double price;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    Supplier supplier;

    public Product(){}

    public Product(
            String name){
        this.name = name;
    }

    public Product(
            String name, String description, Double price, Supplier supplier){
        this.name = name;
        this.description = description;
        this.price = price;
        this.supplier = supplier;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((description == null)? 0 : description.hashCode());
        result = prime * result + ((id == null)? 0 : id.hashCode());
        result = prime * result + ((name == null)? 0 : name.hashCode());
        result = prime * result + ((price == null)? 0 : price.hashCode());
        result = prime * result + ((supplier == null)? 0 : supplier.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product)obj;
        if (description == null) {
            if (other.description != null)
                return false;
        }
        else if (!description.equals(other.description))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        }
        else if (!name.equals(other.name))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        }
        else if (!price.equals(other.price))
            return false;
        if (supplier == null) {
            if (other.supplier != null)
                return false;
        }
        else if (!supplier.equals(other.supplier))
            return false;
        return true;
    }
}
