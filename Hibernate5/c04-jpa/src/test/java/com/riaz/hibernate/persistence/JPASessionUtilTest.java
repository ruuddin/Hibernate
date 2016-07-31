
package com.riaz.hibernate.persistence;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.testng.annotations.Test;

import com.riaz.hibernate.entity.LifecycleEventsExample;
import com.riaz.hibernate.entity.Thing;

public class JPASessionUtilTest {

    @Test
    public void getEntityManager() {
        EntityManager em = JPASessionUtil.getEntityManager("utiljpa");
        em.close();
    }

    @Test(expectedExceptions = { javax.persistence.PersistenceException.class })
    public void nonExistentEntityManagerName() {
        JPASessionUtil.getEntityManager("nonexistent");
        fail("We shouldn't be able to acquire an entityManager here");
    }

    @Test
    public void getSession() {
        Session s = JPASessionUtil.getSession("utiljpa");
        s.close();
    }

    @Test(expectedExceptions = { javax.persistence.PersistenceException.class })
    public void nonExistentSessionName() {
        JPASessionUtil.getSession("nonexistent");
        fail("We shouldn't be able to acquire an session here");
    }

    @Test
    public void testEntityManager() {
        EntityManager em = JPASessionUtil.getEntityManager("utiljpa");
        em.getTransaction().begin();
        Thing t = new Thing();
        t.setName("Thing 1");
        em.persist(t);
        em.getTransaction().commit();
        em.close();

        em = JPASessionUtil.getEntityManager("utiljpa");
        em.getTransaction().begin();
        Query q = em.createQuery("from Thing t where t.name=:name");
        q.setParameter("name", "Thing 1");
        Thing result = (Thing)q.getSingleResult();
        assertNotNull(result);
        assertEquals(result, t);
        em.remove(result);
        em.getTransaction().commit();
        em.close();
    }

    @Test
    public void testSession() {
        Session session = JPASessionUtil.getSession("utiljpa");
        Transaction tx = session.beginTransaction();
        Thing t = new Thing();
        t.setName("Thing 2");
        session.persist(t);
        tx.commit();
        session.close();

        session = JPASessionUtil.getSession("utiljpa");
        tx = session.beginTransaction();
        org.hibernate.Query q = session.createQuery("from Thing t where t.name=:name");
        q.setParameter("name", "Thing 2");
        Thing result = (Thing)q.uniqueResult();
        assertNotNull(result);
        assertEquals(result, t);
        session.delete(result);
        tx.commit();
        session.close();
    }

    @Test
    public void testLifecycle() {
        Integer id;
        Session session = JPASessionUtil.getSession("utiljpa");

        Transaction tx = session.beginTransaction();
        LifecycleEventsExample thing1 = new LifecycleEventsExample();
        thing1.setName("Thing 1");

        session.save(thing1);
        id = thing1.getId();

        tx.commit();
        session.close();

        session = JPASessionUtil.getSession("utiljpa");
        tx = session.beginTransaction();
        LifecycleEventsExample thing2 = (LifecycleEventsExample)session.byId(
                LifecycleEventsExample.class).load(-1);
        assertNull(thing2);

        thing2 = (LifecycleEventsExample)session.byId(LifecycleEventsExample.class)
                .getReference(id);
        assertNotNull(thing2);
        assertEquals(thing1.getId(), thing2.getId());

        thing2.setName("Thing 2");

        tx.commit();
        session.close();

        session = JPASessionUtil.getSession("utiljpa");
        tx = session.beginTransaction();
        LifecycleEventsExample thing3 = (LifecycleEventsExample)session.byId(
                LifecycleEventsExample.class).getReference(id);
        assertNotNull(thing3);
        assertEquals(thing2.getId(), thing3.getId());

        session.delete(thing3);

        tx.commit();
        session.close();
        assertEquals(LifecycleEventsExample.getLifecycleCalls().nextClearBit(0), 7);
    }
}
