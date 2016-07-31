
package chapter03.service;

import hibernate.util.SessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import chapter03.hibernate.Person;
import chapter03.hibernate.Ranking;
import chapter03.hibernate.Skill;

public class HibernateRankingService implements RankingService {

    public int getRankingFor(String subject, String skill) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();

        int average = getRankingFor(session, subject, skill);
        tx.commit();
        session.close();
        return average;
    }

    private int getRankingFor(Session session, String subject, String skill) {
        Query query = session.createQuery("from Ranking r " + "where r.subject.name=:name "
                + "and r.skill.name=:skill");
        query.setString("name", subject);
        query.setString("skill", skill);
        int sum = 0;
        int count = 0;
        for (Ranking r : (List<Ranking>)query.list()) {
            count++;
            sum += r.getRanking();
            System.out.println(r);
        }
        return count == 0? 0 : sum / count;
    }

    public void addRanking(String subject, String observer, String skill, int ranking) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();

        addRanking(session, subject, observer, skill, ranking);

        tx.commit();
        session.close();
    }

    private void addRanking(Session session, String subjectName, String oName, String skillName,
            int rank) {
        Person subject = savePerson(session, subjectName);
        Person observer = savePerson(session, oName);
        Skill skill = saveSkill(session, skillName);

        Ranking ranking = new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(rank);
        session.save(ranking);
    }

    private Skill saveSkill(Session session, String name) {
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

    private Person savePerson(Session session, String name) {
        Person person = findPerson(session, name);
        if (person == null) {
            person = new Person();
            person.setName(name);
            session.save(person);
        }
        return person;
    }

    private Person findPerson(Session session, String name) {
        Query query = session.createQuery("from Person p where p.name=:name");
        query.setParameter("name", name);
        Person person = (Person)query.uniqueResult();
        return person;
    }

    public void updateRanking(String subject, String observer, String skill, int rank) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();

        Ranking ranking = findRanking(session, subject, observer, skill);
        if (ranking == null) {
            addRanking(session, subject, observer, skill, rank);
        }
        else {
            ranking.setRanking(rank);
        }
        tx.commit();
        session.close();
    }

    private Ranking findRanking(Session session, String subjectName, String oName, String sName) {
        Query q = session
                .createQuery("from Ranking r where r.subject.name=:subject and r.observer.name=:observer and r.skill.name=:skill");
        q.setString("subject", subjectName);
        q.setString("observer", oName);
        q.setString("skill", sName);
        q.setMaxResults(1);

        List<Ranking> list = q.list();
        if (q.list() == null || q.list().isEmpty())
            return null;

        Ranking r = (Ranking)q.list().get(0);
        return r;
    }

    public void removeRanking(String subject, String observer, String skill) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();

        Ranking r = findRanking(session, subject, observer, skill);
        if (r != null)
            session.delete(r);

        tx.commit();
        session.close();
    }

    public Map<String, Integer> findRankingsFor(String subject) {
        Map<String, Integer> results;
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();

        results = findRankingsFor(session, subject);

        tx.commit();
        session.close();

        return results;
    }

    private Map<String, Integer> findRankingsFor(Session session, String subject) {
        Map<String, Integer> results=new HashMap<>();
     
        Query query = session.createQuery("from Ranking r where "
                + "r.subject.name=:subject order by r.skill.name");
        query.setParameter("subject", subject);
        List<Ranking> rankings=query.list();
        String lastSkillName="";
        int sum=0;
        int count=0;
        for(Ranking r:rankings) {
            if(!lastSkillName.equals(r.getSkill().getName())) {
                sum=0;
                count=0;
                lastSkillName=r.getSkill().getName();
            }
            sum+=r.getRanking();
            count++;
            results.put(lastSkillName, sum/count);
        }
        return results;
    }    
    
    public Person findBestPersonFor(String skill) {
        Person person = null;
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();

        person = findBestPersonFor(session, skill);

        tx.commit();
        session.close();
        return person;
    }

    private Person findBestPersonFor(Session session, String skill) {
        Query query = session.createQuery("select r.subject.name, avg(r.ranking)"
                + " from Ranking r where " + "r.skill.name=:skill " + "group by r.subject.name "
                + "order by avg(r.ranking) desc");
        query.setParameter("skill", skill);
        List<Object[]> result = query.list();
        if (result.size() > 0) {
            return findPerson(session, (String)result.get(0)[0]);
        }
        return null;
    }
}
