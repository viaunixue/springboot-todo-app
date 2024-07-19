package com.chzzk.study.learn_spring_boot.querydsl.controller;

import com.chzzk.study.learn_spring_boot.querydsl.dto.MemberSearchCondition;
import com.chzzk.study.learn_spring_boot.querydsl.dto.MemberTeamDto;
import com.chzzk.study.learn_spring_boot.querydsl.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberJpaRepository memberJpaRepository;

    @GetMapping("/v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition){
        return memberJpaRepository.searchByWhere(condition);
    }
}
