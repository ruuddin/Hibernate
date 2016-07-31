
package chapter06;

import hibernate.util.SessionUtil;

import java.util.Date;
import java.util.LinkedList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class Chapter06Test {

    private SessionFactory factory;

    @BeforeSuite
    public void suite() {
        Configuration configuration = new Configuration();
        configuration.configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();
        factory = configuration.buildSessionFactory(registry);
    }

    @AfterSuite
    public void afterSuite() {
        factory.close();
    }

    @Test
    public void transientVariableTest() {
        Book book = new Book();
        book.setPages(100);
        book.setTitle("MyBook");
        book.setPublicationDate(new Date());

        Session s = SessionUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.save(book);
        int id = book.getId();
        tx.commit();

        s = SessionUtil.getSession();
        Book b = (Book)s.get(Book.class, id);
        s.close();

        Assert.assertNull(b.getPublicationDate());
    }

    @Test
    public void embeddableKey() {
        BookEmbeddableKey bek = new BookEmbeddableKey();
        EmbeddableKey key = new EmbeddableKey();
        key.setCheckdigit(10);
        key.setGroup(5);
        key.setPublisher(0);
        key.setTitle(7);
        bek.setId(key);
        bek.setName("Riaz");

        Session s = SessionUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.save(bek);
        EmbeddableKey id = bek.getId();
        tx.commit();

        Assert.assertNotNull(id);
    }

    /*
     * OneToOneRelationship is the owner. both Address and OneToOneRelationship must be saved, but only one setXXX must be called.
     * setXXX called by a non owner is not respected.
     */
    @Test
    public void oneToOne() {
        Address a = new Address();
        a.setStreet("Lincoln St");
        a.setZipCode(95050);

        OneToOneRelationship rel = new OneToOneRelationship();
        rel.setAddress(a);
        rel.setName("Owner");

        Session s = SessionUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.save(a);
        s.save(rel);
        int id = rel.getId();
        tx.commit();

        OneToOneRelationship newObj = (OneToOneRelationship)s.get(OneToOneRelationship.class, id);
        s.close();

        Assert.assertNotNull(newObj);
    }

    @Test
    public void cascadeTest() {
        AddressCascade a = new AddressCascade();
        a.setStreet("Harrison St");
        a.setZipCode(95050);

        OneToOneCascade rel = new OneToOneCascade();
        rel.setAddress(a);
        rel.setName("OwnerCascade");

        Session s = SessionUtil.getSession();
        Transaction tx = s.beginTransaction();
        s.save(rel);
        int id = rel.getId();
        tx.commit();

        OneToOneCascade newObj = (OneToOneCascade)s.get(OneToOneCascade.class, id);
        s.close();

        Assert.assertNotNull(newObj);
    }

    @Test
    public void one2Many() {
        One2ManyAddress a1 = new One2ManyAddress();
        a1.setName("One2ManyAddress1");
        One2ManyAddress a2 = new One2ManyAddress();
        a2.setName("One2ManyAddress2");
        Session s = SessionUtil.getSession();
        Transaction tx = s.beginTransaction();
        One2ManyOwner rel = new One2ManyOwner();
        rel.setName("One2ManyOwner");
        rel.setAddresses(new LinkedList<One2ManyAddress>());
        rel.getAddresses().add(a1);
        rel.getAddresses().add(a2);
        s.save(rel);
        int id = rel.getId();
        tx.commit();

        One2ManyOwner result = (One2ManyOwner)s.get(One2ManyOwner.class, id);
        s.close();

        Assert.assertNotNull(result);
    }
}
