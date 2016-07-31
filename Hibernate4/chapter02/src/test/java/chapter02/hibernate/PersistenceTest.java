package chapter02.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class PersistenceTest {

    SessionFactory factory;
    
    @BeforeSuite
    public void setup(){
        Configuration configuration = new Configuration();
        configuration.configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();
        factory = configuration.buildSessionFactory(registry);
    }
    
    @Test
    public void saveMessage(){
        Message message = new Message("Hello, world");
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(message);
        tx.commit();
        session.close();
    }
    
    @Test(dependsOnMethods = "saveMessage")
    public void readMessage(){
        Session session = factory.openSession();
        @SuppressWarnings("unchecked")
        List<Message> list = (List<Message>) session.createQuery("from Message").list();
        
        if(list.size() > 1){
            Assert.fail("Message config in error; "
                    + "table should contain only one. Set ddl to drop-create");
        }
        
        for(Message m : list){
            System.out.println(m);
        }
        session.close();
    }
}
