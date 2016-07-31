package chapter03.service;

import java.util.Map;

import chapter03.hibernate.Person;

public interface RankingService {

    public int getRankingFor(String subject, String skill);
    
    public void addRanking(String subject, String observer, String skill, int ranking);
    
    public void updateRanking(String subject, String observer, String skill, int rank);
    
    public void removeRanking(String subject, String observer, String skill);
    
    public Map<String, Integer> findRankingsFor(String subject);
    
    public Person findBestPersonFor(String skill);
}
