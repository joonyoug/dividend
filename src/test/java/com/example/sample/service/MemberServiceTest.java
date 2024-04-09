package com.example.sample.service;

import com.example.sample.model.Auth;
import com.example.sample.model.constants.MemberEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @Test
    void successRegister() {
        //given

        List<String> roles= Arrays.asList("ROLE_READ");
        MemberEntity memberEntity= MemberEntity.builder()
                .username("qwer")
                .password("1234")
                .roles(roles)
                .build();
        Auth.SignUp signUp=new Auth.SignUp();
        signUp.setPassword("1234");
        signUp.setUsername("qwer");
        signUp.setRoles(roles);
        //when
        MemberEntity member=memberService.register(signUp);
        //then
        assertEquals(member.getUsername(),memberEntity.getUsername());
        assertEquals(member.getRoles().size(),memberEntity.getRoles().size());

    }

}