package com.speakly.bank_backend.persistence.dao.impl;

import es.speakly.store_backend.persistence.dao.UserDao;
import es.speakly.store_backend.persistence.dao.impl.entity.CourseJpaEntity;
import es.speakly.store_backend.persistence.dao.impl.entity.UserJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserDaoJpaImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Optional<UserJpaEntity> findById(Long id) {
        return Optional.ofNullable(entityManager.find(UserJpaEntity.class, id));
    }

    @Override
    public Optional<UserJpaEntity> findByUsername(String username) {
        String jpql = "SELECT u FROM UserJpaEntity u WHERE u.username = :username";
        TypedQuery<UserJpaEntity> query = entityManager.createQuery(jpql, UserJpaEntity.class);
        query.setParameter("username", username);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserJpaEntity> findByEmail(String email) {
        String jpql = "SELECT u FROM UserJpaEntity u WHERE u.email = :email";
        TypedQuery<UserJpaEntity> query = entityManager.createQuery(jpql, UserJpaEntity.class);
        query.setParameter("email", email);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserJpaEntity> findAll(int pageNumber, int pageSize) {
        int pageIndex = Math.max(pageNumber - 1, 0);
        String sql = "SELECT u FROM UserJpaEntity u";
        TypedQuery<UserJpaEntity> query = entityManager.createQuery(sql, UserJpaEntity.class)
                .setFirstResult(pageIndex * pageSize).setMaxResults(pageSize);

        return query.getResultList();
    }

    @Override
    public UserJpaEntity insert(UserJpaEntity entity) {
        if (entity.getCoursesTaken() != null && !entity.getCoursesTaken().isEmpty()) {
            List<CourseJpaEntity> managedCourses = entity.getCoursesTaken().stream()
                    .map(course -> {
                        if (course.getId() != null) {
                            return entityManager.find(CourseJpaEntity.class, course.getId());
                        }
                        return course;
                    })
                    .filter(Objects::nonNull)
                    .toList();

            entity.getCoursesTaken().clear();
            entity.getCoursesTaken().addAll(managedCourses);
        }

        entityManager.persist(entity);
        return entity;
    }

    @Override
    public UserJpaEntity update(UserJpaEntity entity) {
        UserJpaEntity userJpaEntity = entityManager.find(UserJpaEntity.class, entity.getId());
        if (userJpaEntity == null){
            throw new  RuntimeException("User not found with id: " + entity.getId());
        }
        entity.setEncryptedPassword(userJpaEntity.getEncryptedPassword());
        userJpaEntity.getCoursesTaken().clear();
        entityManager.merge(userJpaEntity);
        return entityManager.merge(entity);
    }

    @Override
    public void deleteById(Long id) {
        entityManager.remove(entityManager.find(UserJpaEntity.class, id));
    }

    @Override
    public long count() {
        return entityManager.createQuery("SELECT COUNT(u) FROM UserJpaEntity u", Long.class)
                .getSingleResult();
    }
}