package sk.tuke.gamestudio.service;

import org.springframework.transaction.annotation.Transactional;
import sk.tuke.gamestudio.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addComment(Comment comment) {
        entityManager.persist(comment);
    }

    @Override
    public List<Comment> getComment(String game) {
        //return entityManager.createQuery("select c from Comment c").setMaxResults(5).getResultList();
        return entityManager.createNamedQuery("Comment.getComments").setParameter("game", game).setMaxResults(5).getResultList();
    }

    @Override
    public void reset() {
       // entityManager.createQuery("delete from Comment").executeUpdate();
        entityManager.createNamedQuery("Comment.resetComments").executeUpdate();
    }
}
