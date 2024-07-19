package com.chzzk.study.learn_spring_boot.querydsl.repository;


import com.chzzk.study.learn_spring_boot.querydsl.dto.MemberSearchCondition;
import com.chzzk.study.learn_spring_boot.querydsl.dto.MemberTeamDto;
import com.chzzk.study.learn_spring_boot.querydsl.entity.Member;
import com.chzzk.study.learn_spring_boot.querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {
    @Autowired
    EntityManager em;

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @BeforeEach
    public void before() {
        // 기존 데이터 모두 삭제
        em.createQuery("DELETE FROM Member").executeUpdate();
    }

    @Test
    public void basicTest() {

        Member member = new Member("member1", 10);
        memberJpaRepository.save(member);

        em.flush();
        em.clear();

        Member findMember = memberJpaRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);
//        assertThat(findMember.getId()).isEqualTo(member.getId());
//        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//        assertThat(findMember.getAge()).isEqualTo(member.getAge());

        List<Member> result1 = memberJpaRepository.findAll_Querydsl();
        assertThat(result1).containsExactly(member);
//        assertThat(result1).hasSize(1);
//        assertThat(result1.get(0).getUsername()).isEqualTo("member1");
//        assertThat(result1.get(0).getAge()).isEqualTo(10);

        List<Member> result2 = memberJpaRepository.findByUserName_Querydsl("member1");
        assertThat(result2).containsExactly(member);
//        assertThat(result2).hasSize(1);
//        assertThat(result2.get(0).getUsername()).isEqualTo("member1");
//        assertThat(result2.get(0).getAge()).isEqualTo(10);
    }

    @Test
    public void searchTest() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");

//        List<MemberTeamDto> result = memberJpaRepository.searchByBuilder(condition);
        List<MemberTeamDto> result = memberJpaRepository.searchByWhere(condition);

        assertThat(result).extracting("username").containsExactly("member4");
    }




}