package com.chzzk.study.learn_spring_boot.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
