
package chapter03.service;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import chapter03.hibernate.Person;

public class RankingTest {

    RankingService service = new HibernateRankingService();

    @Test
    public void addRanking() {
        service.addRanking("Smell", "Drew", "Mule", 8);
        Assert.assertEquals(service.getRankingFor("Smell", "Mule"), 8);
    }

    @Test
    public void updateExistingRanking() {
        service.addRanking("Gene", "Scott", "Ceylon", 6);
        Assert.assertEquals(service.getRankingFor("Gene", "Ceylon"), 6);
        service.updateRanking("Gene", "Scott", "Ceylon", 7);
        Assert.assertEquals(service.getRankingFor("Gene", "Ceylon"), 7);
    }

    @Test
    public void updateNonexistentRanking() {
        Assert.assertEquals(service.getRankingFor("Scott", "Ceylon"), 0);
        service.updateRanking("Scott", "Gene", "Ceylon", 7);
        Assert.assertEquals(service.getRankingFor("Scott", "Ceylon"), 7);
    }

    @Test
    public void removeRanking() {
        service.addRanking("R1", "R2", "RS1", 8);
        Assert.assertEquals(service.getRankingFor("R1", "RS1"), 8);
        service.removeRanking("R1", "R2", "RS1");
        Assert.assertEquals(service.getRankingFor("R1", "RS1"), 0);
    }

    @Test
    public void removeNonexistentRanking() {
        service.removeRanking("R3", "R4", "RS2");
    }

    @Test
    public void validateRankingAverage() {
        service.addRanking("A", "B", "C", 4);
        service.addRanking("A", "B", "C", 5);
        service.addRanking("A", "B", "C", 6);
        Assert.assertEquals(service.getRankingFor("A", "C"), 5);
        service.addRanking("A", "B", "C", 7);
        service.addRanking("A", "B", "C", 8);
        Assert.assertEquals(service.getRankingFor("A", "C"), 6);
    }

    @Test
    public void findAllRankingsEmptySet() {
        Assert.assertEquals(service.getRankingFor("Nobody", "Java"), 0);
        Assert.assertEquals(service.getRankingFor("Nobody", "Python"), 0);
        Map<String, Integer> rankings = service.findRankingsFor("Nobody");

        Assert.assertEquals(rankings.size(), 0);
    }

    @Test
    public void findAllRankings() {
        Assert.assertEquals(service.getRankingFor("Somebody", "Java"), 0);
        Assert.assertEquals(service.getRankingFor("Somebody", "Python"), 0);
        service.addRanking("Somebody", "Nobody", "Java", 9);
        service.addRanking("Somebody", "Nobody", "Java", 7);
        service.addRanking("Somebody", "Nobody", "Python", 7);
        service.addRanking("Somebody", "Nobody", "Python", 5);
        Map<String, Integer> rankings = service.findRankingsFor("Somebody");

        Assert.assertEquals(rankings.size(), 2);
        Assert.assertNotNull(rankings.get("Java"));
        Assert.assertEquals(rankings.get("Java"), new Integer(8));
        Assert.assertNotNull(rankings.get("Python"));
        Assert.assertEquals(rankings.get("Python"), new Integer(6));
    }

    @Test
    public void findBestForNonexistentSkill() {
        Person p = service.findBestPersonFor("no skill");
        Assert.assertNull(p);
    }

    @Test
    public void findBestForSkill() {
        service.addRanking("S1", "O1", "Sk1", 6);
        service.addRanking("S1", "O2", "Sk1", 8);
        service.addRanking("S2", "O1", "Sk1", 5);
        service.addRanking("S2", "O2", "Sk1", 7);
        service.addRanking("S3", "O1", "Sk1", 7);
        service.addRanking("S3", "O2", "Sk1", 9);
        // data that should not factor in!
        service.addRanking("S3", "O1", "Sk2", 2);
        Person p = service.findBestPersonFor("Sk1");
        Assert.assertEquals(p.getName(), "S3");
    }
}
