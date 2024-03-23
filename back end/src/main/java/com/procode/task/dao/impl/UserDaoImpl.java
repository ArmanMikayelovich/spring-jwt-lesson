package com.procode.task.dao.impl;

import com.procode.task.dao.UserDao;
import com.procode.task.model.entity.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    private final SessionFactory sessionFactory;

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<UserEntity> findById(Long id) {

        UserEntity userEntity = currentSession().find(UserEntity.class, id);
        return Optional.ofNullable(userEntity);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        final Query<UserEntity> findByUsernameQuery = currentSession()
                .createQuery("select u from UserEntity u where u.username = :username", UserEntity.class);

        UserEntity userEntity = findByUsernameQuery.setParameter("username", username).uniqueResult();
        return Optional.ofNullable(userEntity);
    }

    @Override
    public List<UserEntity> findAll() {
        final Query<UserEntity> query = currentSession()
                .createQuery("select u from UserEntity as u", UserEntity.class);
        return query.list();
    }

    @Override
    public void save(UserEntity user) {
        currentSession().persist(user);
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
