package com.procode.task.dao.impl;

import com.procode.task.dao.FileInfoDAO;
import com.procode.task.exceptions.NotFoundException;
import com.procode.task.model.entity.FileInfoEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class FileInfoDaoImpl implements FileInfoDAO {


    private final SessionFactory sessionFactory;

    public FileInfoDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<FileInfoEntity> findById(Long id) {
        FileInfoEntity fileInfoEntity = currentSession().find(FileInfoEntity.class, id);
        return Optional.ofNullable(fileInfoEntity);
    }

    @Override
    public void save(FileInfoEntity fileInfoEntity) {
        currentSession().persist(fileInfoEntity);
    }

    @Override
    public void update(FileInfoEntity fileInfoEntity) {
        currentSession().update(fileInfoEntity);

    }

    @Override
    public List<FileInfoEntity> getAllByUser(Long userId) {
        Query<FileInfoEntity> query = currentSession()
                .createQuery("select f from FileInfoEntity as f where f.userEntity.id = :userId",
                        FileInfoEntity.class);

        return query.setParameter("userId", userId).list();
    }

    @Override
    public List<FileInfoEntity> getAll() {
        return currentSession().createQuery("select f from FileInfoEntity as f", FileInfoEntity.class).list();
    }

    @Override
    @Transactional
    public void remove(Long id) {
        final FileInfoEntity fileInfoEntity = findById(id)
                .orElseThrow(() -> new NotFoundException("File with id: " + id + " not found"));
        currentSession().remove(fileInfoEntity);
    }

    @Override
    public Optional<FileInfoEntity> getFileByNameAndUserId(String name,Long userId) {
        Query<FileInfoEntity> query = currentSession()
                .createQuery("select f from FileInfoEntity as f " +
                        "where f.fileName = :name and f.userEntity.id = :userId",
                        FileInfoEntity.class);

        FileInfoEntity fileInfoEntity = query.setParameter("name", name)
                .setParameter("userId",userId).uniqueResult();
        return Optional.ofNullable(fileInfoEntity);
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
