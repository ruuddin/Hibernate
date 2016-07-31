package chapter03.simple;

import org.testng.annotations.Test;

import chapter03.hibernate.Person;
import chapter03.hibernate.Ranking;
import chapter03.hibernate.Skill;

public class ModelTest {

    @Test
    public void create(){
        Person subject = new Person();
        subject.setName("Smell");
        
        Person observer = new Person();
        observer.setName("Drew");
        
        Skill skill = new Skill();
        skill.setName("Java");
        
        Ranking ranking = new Ranking();
        ranking.setSubject(subject);
        ranking.setObserver(observer);
        ranking.setSkill(skill);
        ranking.setRanking(8);
        
        System.out.println(ranking);
    }
}
