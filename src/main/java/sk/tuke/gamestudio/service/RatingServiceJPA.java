package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addRating(Rating rating) {
        if (rating.getRating() < 0 || rating.getRating() > 5) {
            throw new IllegalArgumentException("Rating can be only >=0 and <=5");
        }
        entityManager.persist(rating);
    }

    @Override
    public List<Rating> getRatings(String game) {
        //return entityManager.createQuery("select r from Rating r").getResultList();
        return entityManager.createNamedQuery("Rating.getRating").setParameter("game", game).getResultList();
    }
}
