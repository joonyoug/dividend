package com.example.sample.service;

import com.example.sample.exception.impl.AlreadyExistUserException;
import com.example.sample.model.Auth;
import com.example.sample.model.constants.MemberEntity;
import com.example.sample.persist.entity.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Member;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        return this.memberRepository.findByUsername(s)
                .orElseThrow(()->new UsernameNotFoundException("couldn't find user"+s));
    }


    public MemberEntity register(Auth.SignUp signUp){
        boolean exists= this.memberRepository.existsByUsername(signUp.getUsername());
        if(exists){
            throw new AlreadyExistUserException();
        }
        signUp.setPassword(this.passwordEncoder.encode(signUp.getPassword()));
        return this.memberRepository.save(signUp.toEntity());

    }

    public MemberEntity authenticate(Auth.SignIn member){
        var user=this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(()->new RuntimeException("존재하지 않는 id 입니다,"));

        if(!this.passwordEncoder.matches(member.getPassword(), user.getPassword())){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        return user;





    }








}
