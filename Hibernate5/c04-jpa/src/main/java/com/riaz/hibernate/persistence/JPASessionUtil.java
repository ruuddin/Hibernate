
package com.riaz.hibernate.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.Session;

public class JPASessionUtil {

    static Map<String, EntityManagerFactory> persistenceUnits = new HashMap<>();

    public static synchronized EntityManager getEntityManager(String persistenceUnitName) {
        if (persistenceUnits.get(persistenceUnitName) == null)
            persistenceUnits.put(persistenceUnitName,
                    Persistence.createEntityManagerFactory(persistenceUnitName));

        return persistenceUnits.get(persistenceUnitName).createEntityManager();
    }

    // Provides access to underlying implementation of EntityManager. if the actual implementation
    // is not Hibernate runtime exception is thrown
    public static Session getSession(String persistenceUnitName) {
        return getEntityManager(persistenceUnitName).unwrap(Session.class);
    }
}
