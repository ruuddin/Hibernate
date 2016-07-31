
package chapter06;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/*
 * Stategy1: EmbeddableKey as a property
 */
@Entity
public class BookEmbeddableKey {

    @Id
    private EmbeddableKey id;
    @Column
    private String name;

    public EmbeddableKey getId() {
        return id;
    }

    public void setId(EmbeddableKey id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
