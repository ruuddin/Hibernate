
package chapter08;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class Chapter08Test {
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
    public void cacheTest() {
        CacheExample o = new CacheExample();
        o.setName("I am new");

        Session s = factory.openSession();
        Transaction t = s.beginTransaction();
        s.save(o);
        int id = o.getId();
        t.commit();
        s.close();

        for (int i = 0; i < 10; i++) {
            s = factory.openSession();
            // this get() should not go to db bust instead read from L1 cache.
            // Rule: an object accessed again within the same session will be read from cache.
            s.get(CacheExample.class, id);
            s.close();
            System.out.print("\t " + i);
        }
    }
}
