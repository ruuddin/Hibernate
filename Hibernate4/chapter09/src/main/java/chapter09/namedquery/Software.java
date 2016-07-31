
package chapter09.namedquery;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Software extends Product implements Serializable {
    @Column
    String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((version == null)? 0 : version.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Software other = (Software)obj;
        if (version == null) {
            if (other.version != null)
                return false;
        }
        else if (!version.equals(other.version))
            return false;
        return true;
    }

    public Software(){
        super();
    }

    public Software(
            String name){
        super(name);
    }

    public Software(
            String name, String description, Double price, String version, Supplier supplier){
        super(name, description, price, supplier);
        this.version = version;
    }
}
