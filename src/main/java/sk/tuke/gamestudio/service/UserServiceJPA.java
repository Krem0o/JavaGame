package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Users;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class UserServiceJPA implements UserService{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(Users user) {
        entityManager.persist(user);

    }

    @Override
    public List<Users> getUsers(String game) {
        return entityManager.createNamedQuery("User.getUsers").setParameter("game", game).getResultList();
    }
}
