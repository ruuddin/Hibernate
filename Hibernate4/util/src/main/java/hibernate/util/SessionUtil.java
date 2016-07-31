package hibernate.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionUtil {

    private final static SessionUtil instance = new SessionUtil();
    private final SessionFactory factory;
    
    private SessionUtil(){
        Configuration configuration = new Configuration();
        configuration.configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();
        factory = configuration.buildSessionFactory(registry);
    }
    
    public static Session getSession(){
        return getInstance().factory.openSession();
    }
    
    private static SessionUtil getInstance(){
        return instance;
    }
}
