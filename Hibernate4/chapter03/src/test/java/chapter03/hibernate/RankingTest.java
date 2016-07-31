
package chapter03.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import chapter03.hibernate.Person;
import chapter03.hibernate.Ranking;
import chapter03.hibernate.Skill;

public class RankingTest {

    SessionFactory factory;
    Session session;
    Transaction tx;

    @BeforeSuite
    public void suite() {
        Configuration configuration = new Configuration();
        configuration.configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry registry = builder.build();
        factory = configuration.buildSessionFactory(registry);
    }

    @BeforeMethod
    public void setup() {
        session = factory.openSession();
        tx = session.beginTransaction();
    }

    @AfterMethod
    public void shutdown() {
        tx.commit();
        session.close();
    }

    @AfterSuite
    public void afterSuite() {
        factory.close();
    }

    @Test
    public void savePerson() {
        savePerson("Smell");
    }

    private Person savePerson(String name) {
        Person person = findPerson(name);
        if (person == null) {
            person = new Person();
            person.setName(name);
            session.save(person);
        }
        return person;
    }

    @Test
    public void saveRanking() {
        populateRankingData();

        int avg = getAverage("Smell", "Java");
        Assert.assertEquals(avg, 7);
    }

    private int getAverage(String subjectName, String skillName) {
        Query query = session
                .createQuery("from Ranking r where r.subject.name = :name and r.skill.name = :skill");
        query.setString("name", subjectName);
        query.setString("skill", skillName);

        int sum = 0;
        int count = 0;
        for (Ranking r : (List<Ranking>)query.list()) {
            count++;
            sum += r.getRanking();
            System.out.println(r);
        }

        int avg = sum / count;
        return avg;
    }

    private void populateRankingData() {
        addRankDataForPerson("Smell", "Drew", "Java", 6);
        addRankDataForPerson("Smell", "Most", "Java", 7);
        addRankDataForPerson("Smell", "Scott", "Java", 8);
    }

    @Test
    public void changeRanking() {
        populateRankingData();

        Ranking r = findRanking("Smell", "Drew", "Java");
        Assert.assertNotNull(r, "could not find matching ranking");
        r.setRanking(9);
        Assert.assertEquals(getAverage("Smell", "Java"), 8);
    }
    
    @Test
    public void removeRanking(){
        populateRankingData();
        Ranking r = findRanking("Smell", "Drew", "Java");
        Assert.assertNotNull(r, "Ranking not found");
        session.delete(r);
        Assert.assertEquals(getAverage("Smell", "Java"), 7);
    }

    private Ranking findRanking(String subjectName, String oName, String sName) {
        Query q = session
                .createQuery("from Ranking r where r.subject.name=:subject and r.observer.name=:observer and r.skill.name=:skill");
        q.setString("subject", subjectName);
        q.setString("observer", oName);
        q.setString("skill", sName);
        q.setMaxResults(1);
        
        Ranking r = (Ranking)q.list().get(0);
        return r;
    }

    private void addRankDataForPerson(String person, String observerName, String skillName,
            Integer rank) {
        Person subject = savePerson(person);
        Person observer = savePerson(observerName);
        Skill skill = saveSkill(skillName);

        Ranking ranking = new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(rank);
        session.save(ranking);
    }

    private Skill saveSkill(String name) {
        Query query = session.createQuery("from Skill s where s.name=:name");
        query.setParameter("name", name);
        Skill skill = (Skill)query.uniqueResult();
        if (skill == null) {
            skill = new Skill();
            skill.setName(name);
            session.save(skill);
        }
        return skill;
    }

    private Person findPerson(String name) {
        Query query = session.createQuery("from Person p where p.name=:name");
        query.setParameter("name", name);
        Person person = (Person)query.uniqueResult();
        return person;
    }
}
