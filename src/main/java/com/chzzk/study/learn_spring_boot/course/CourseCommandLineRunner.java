//package com.chzzk.study.learn_spring_boot.course;
//
//import com.chzzk.study.learn_spring_boot.course.jdbc.CourseJdbcRepository;
//import com.chzzk.study.learn_spring_boot.course.jpa.CourseJpaRepository;
//import com.chzzk.study.learn_spring_boot.course.springdatajpa.CourseSpringDataJpaRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CourseCommandLineRunner implements CommandLineRunner {
//
////    @Autowired
////    private CourseJdbcRepository repository;
//
////    @Autowired
////    private CourseJpaRepository repository;
//
//    private CourseSpringDataJpaRepository repository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        repository.save(new Course(1, "Learn AWS Jpa!", "chzzk"));
//        repository.save(new Course(2, "Learn Azure Jpa!", "chzzk"));
//        repository.save(new Course(3, "Learn DevOps Jpa!", "chzzk"));
//
//        repository.deleteById(1l);
//
//        System.out.println(repository.findById(2l));
//        System.out.println(repository.findById(3l));
//
//        System.out.println(repository.findByAuthor("chzzk"));
//        System.out.println(repository.findByAuthor(""));
//
//        System.out.println(repository.findByName("Learn Azure Jpa!"));
//        System.out.println(repository.findByName("Learn AWS Jpa!"));
//    }
//}
