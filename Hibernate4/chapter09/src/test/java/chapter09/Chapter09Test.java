
package chapter09;

import hibernate.util.SessionUtil;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import chapter09.namedquery.Product;
import chapter09.namedquery.Software;
import chapter09.namedquery.Supplier;

public class Chapter09Test {
    private Session session;
    private Transaction tx;

    @BeforeMethod
    public void populateData() {
        session = SessionUtil.getSession();
        tx = session.beginTransaction();

        Supplier s = new Supplier();
        s.setName("Hardware, Inc.");
        Product p = new Product("Optical Wheel Mouse", "Mouse", 5.00, s);
        p.setSupplier(s);
        s.getProducts().add(p);
        session.save(s);

        s = new Supplier();
        s.setName("Supplier 2");
        s.getProducts().add(new Software("SuperDetect", "AntiVirus", 14.95, "1.0", s));
        s.getProducts().add(new Software("WildCat", "Browser", 19.95, "2.2", s));
        s.getProducts().add(new Software("Axegrinder", "GamingMouse", 42.00, "1.0", s));

        session.save(s);
    }

    @AfterMethod
    public void closeSession() {
        session.createQuery("delete from Product").executeUpdate();
        session.createQuery("delete from Supplier").executeUpdate();
        if (tx.isActive())
            tx.commit();
        if (session.isOpen())
            session.close();
    }

    @Test
    public void namedQuery() {
        Query q = session.getNamedQuery("supplier.findAll");
        List<Supplier> suppliers = q.list();
        Assert.assertEquals(suppliers.size(), 2);
    }
}
