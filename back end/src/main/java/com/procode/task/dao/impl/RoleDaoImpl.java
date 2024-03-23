package com.procode.task.dao.impl;

import com.procode.task.dao.RoleDao;
import com.procode.task.model.entity.RoleEntity;
import com.procode.task.model.enums.UserRole;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {
    private final SessionFactory sessionFactory;

    public RoleDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(RoleEntity role) {
        currentSession().persist(role);
    }

    @Override
    public Optional<RoleEntity> findByRoleName(UserRole role) {
        Query<RoleEntity> query = currentSession()
                .createQuery("select r from RoleEntity as r where r.role = :role", RoleEntity.class);

        RoleEntity roleEntity = query.setParameter("role", role).uniqueResult();
        return Optional.ofNullable(roleEntity);

    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
