package com.chzzk.study.learn_spring_boot;

import com.chzzk.study.learn_spring_boot.querydsl.entity.Member;
import com.chzzk.study.learn_spring_boot.querydsl.entity.QMember;
import com.chzzk.study.learn_spring_boot.querydsl.entity.QTeam;
import com.chzzk.study.learn_spring_boot.querydsl.entity.Team;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.List;

import static com.chzzk.study.learn_spring_boot.querydsl.entity.QMember.member;
import static com.chzzk.study.learn_spring_boot.querydsl.entity.QTeam.team;
import static com.querydsl.jpa.JPAExpressions.select;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class QuerydslBasicTest {
    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @PersistenceUnit
    EntityManagerFactory emf;

    @BeforeEach
    public void before() {
        // 기존 데이터 모두 삭제
        em.createQuery("DELETE FROM Member").executeUpdate();
        em.createQuery("DELETE FROM Team").executeUpdate();

        queryFactory = new JPAQueryFactory(em);
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

        em.flush();
        em.clear();
    }

    @Test
    public void startJPQL() {
        // member 1 찾기
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .setMaxResults(1)
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() {
        // member 1 찾기
        // JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        // QMember m = new QMember("m");
        // QMember m = QMember.member;

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1")) // Parameter binding 처리
                .fetchFirst();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        // .and(member.age.eq(10)))
                        .and(member.age.between(10, 30)))
                .fetchFirst();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member)
                // .where(member.username.eq("member1").and(member.age.eq(10)))
                .where(
                        member.username.eq("member1"),
                        (member.age.eq(10))
                        // (member.age.eq(10)), null
                        // 위 코드처럼 구현할 경우 null이 들어가면 무시되어버림
                        // 이 방법으로 동적쿼리를 만들 때 이걸 가지고 깔끔하게 코드를 구현할 수 있음
                )
                .fetchFirst();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetchTest() {
        // 리스트
        // List<Member> fetch = queryFactory
        //        .selectFrom(member)
        //        .fetch();

        // 단건
        // Member fineMember1 = queryFactory
        //        .selectFrom(member)
        //        .fetchOne();

        // 처음 한 건
        // Member findMember2 = queryFactory
        //         .selectFrom(member)
        //         .fetchFirst();

        // 페이징에서 사용 -> 현재 버전 Deprecated 왜?
        // groupby having 절이 섞인 복잡한 쿼리에문에서 잘 작동하지 않는 ISSUE 있음
        // 따라서 카운트하려면 그냥 fetch() 쓰고 count query 따로 날려야함
        // queryFactory
        //        .selectFrom(member)
        //        .fetchResults();
        // long count = queryFactory
        //        .selectFrom(member)
        //        .fetchCount();

        // 위 코드 최신 권장 방식에 맞춰 다음과 같이 구현
        List<Member> members = queryFactory
                .selectFrom(member)
                .fetch();

        Long count = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();
    }

    @Test
    public void sort() {
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    @Test
    public void paging1() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void paging2() {
        // fetchResults() -> deprecated
        // QueryResults<Member> queryResults = queryFactory
        //        .selectFrom(member)
        //        .orderBy(member.username.desc())
        //        .offset(1)
        //        .limit(2)
        //        .fetchResults();

        List<Member> members = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        Long totalCount = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();

        // 전체 개수, 결과 리스트 크기 TEST
        assertThat(totalCount).isEqualTo(4);
        assertThat(members).hasSize(2);

        // offset, limit 확인
        List<Member> allMembers = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .fetch();
        // 결과가 전체 결과의 부분집합인지 TEST
        assertThat(allMembers).containsAll(members);                    // paging 결과 전체 결과에 포함되는지
        assertThat(allMembers).size().isGreaterThan(members.size());    // 전체 결과 크기가 페이징 된 결과보다 큰지
        assertThat(allMembers.subList(1, 3)).isEqualTo(members);        // 페이징 결과가 전체 결과의 정확한 부분과 일치하는지
    }

    @Test
    public void aggregation() throws Exception {
        List<Tuple> result = queryFactory
                .select(member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min())
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);
    }

    @Test
    public void group() throws Exception {
        // 팀 이름과 각 팀의 평균 연령 구하기
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    @Test
    public void join() throws Exception {
        // 팀 A에 소속된 모든 회원
        QMember member = QMember.member;
        QTeam team = QTeam.team;

        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");

    }

    @Test
    public void theta_join() throws Exception {
        // 세타 조인 -> 연관관계가 없는 필드로 Join
        // 회원의 이름이 팀 이름과 같은 회원 조회
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    @Test
    public void join_on_filtering() throws Exception {
        // 회원과 팀을 조인하면서 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result){
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    public void join_on_no_relation() throws Exception {
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));

        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();

        for (Tuple tuple : result){
            System.out.println("t= " + tuple);
        }
    }

    @Test
    public void fetchJoinNo() throws Exception {
        em.flush();
        em.clear();

        // 패치 조인 미적용 -> 지연로딩으로 member, team SQL 쿼리 각각 실행
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        assertThat(loaded).as("패치 조인 미적용").isFalse();
    }

    @Test
    public void fetchJoinUse() throws Exception {
        em.flush();
        em.clear();

        // 패치 조인 적용
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()                                                                                                                                                                                                                
                .where(member.username.eq("member1"))
                .fetchOne();

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());

        assertThat(loaded).as("패치 조인 적용").isTrue();
    }
    
    @Test
    public void subQuery() throws Exception {
        // 나이가 가장 많은 회원 조회
        QMember memberSub= new QMember(("memberSub"));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }

    @Test
    public void subQueryGoe() throws Exception {
        // 나이가 평균 나이 이상인 회원
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40);
    }

    @Test
    public void subQueryIn() throws Exception {
        // 서브 쿼리 여러 건 처리, in 사용
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10))
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    @Test
    public void subQuerySelect() throws Exception {
        // select 절에 서브 쿼리
        QMember memberSub = new QMember("memberSub");

        List<Tuple> fetch = queryFactory
                .select(member.username,
                        select(memberSub.age.avg())
                                .from(memberSub)
                ).from(member)
                .fetch();

        for (Tuple tuple: fetch){
            System.out.println("username = " + tuple.get(member.username));
            System.out.println("age = " +
                    tuple.get(select(memberSub.age.avg())
                            .from(memberSub)));
        }
    }

    @Test
    public void subQueryImport() throws Exception {
        QMember memberSub = new QMember("memberSub");

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age").containsExactly(40);
    }

    @Test
    public void caseSimple() throws Exception {
        List<String> result = queryFactory
                .select(member.age
                        .when(10).then("10살")
                        .when(20).then("20살")
                        .otherwise("Etc."))
                .from(member)
                .fetch();

        for (String s: result){
            System.out.println("s : " + s);
        }
    }

    @Test
    public void caseComplex() throws Exception {
        List<String> result = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("Etc"))
                .from(member)
                .fetch();

        for(String s : result){
            System.out.println("s = " + s);
        }
    }

    @Test
    public void caseOrderBy() throws Exception {
        // 임의의 순서로 회원을 출력하고 싶다면?
        NumberExpression<Integer> rankPath = new CaseBuilder()
                .when(member.age.between(0, 20)).then(2)
                .when(member.age.between(21, 30)).then(1)
                .otherwise(3);

        List<Tuple> result = queryFactory
                .select(member.username, member.age, rankPath)
                .from(member)
                .orderBy(rankPath.desc())
                .fetch();

        for (Tuple tuple : result){
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);
            Integer rank = tuple.get(rankPath);
            System.out.println("username = " + username + ", age = " + age + ", rank = " + rank);
        }
    }

    @Test
    public void expressions() throws Exception {
        Tuple result = queryFactory
                .select(member.username, Expressions.constant("A"))
                .from(member)
                .fetchFirst();

        System.out.println(("result" + result));
    }

    @Test
    public void concatString() throws Exception {
        String result = queryFactory
                .select(member.username.concat("_").concat(member.age.stringValue()))
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        System.out.println("result = " + result);
    }




}

