package com.chzzk.study.learn_spring_boot.course.springdatajpa;

import com.chzzk.study.learn_spring_boot.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseSpringDataJpaRepository extends JpaRepository<Course, Long> {
    List<Course> findByAuthor (String author);
    List<Course> findByName (String name);
}
