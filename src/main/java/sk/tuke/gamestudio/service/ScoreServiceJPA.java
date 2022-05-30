package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) {
        if (score.getPoints() < 0) {
            throw new IllegalArgumentException("Score can be only >=0");
        }
        entityManager.persist(score);
    }

    @Override
    public List<Score> getTopScores(String game) {
        //return entityManager.createQuery("select s from Score s order by s.points desc").setMaxResults(10).getResultList();
        return entityManager.createNamedQuery("Score.getTopScores").setParameter("game", game).setMaxResults(10).getResultList();
    }

    @Override
    public void reset() {
       // entityManager.createQuery("delete from Score").executeUpdate();
        entityManager.createNamedQuery("Score.resetScores").executeUpdate();
    }
}
