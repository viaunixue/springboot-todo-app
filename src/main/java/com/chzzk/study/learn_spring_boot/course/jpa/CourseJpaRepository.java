package com.chzzk.study.learn_spring_boot.course.jpa;

import com.chzzk.study.learn_spring_boot.course.Course;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class CourseJpaRepository {

    // @Autowired
    @PersistenceContext
    private EntityManager entityManager;

    public void insert (Course course) {
        entityManager.merge(course);
    }

    public Course findById(long id) {
        return entityManager.find(Course.class, id);
    }

    public void deleteById (long id) {
        Course course = findById(id);
        entityManager.remove(course);
    }
}
