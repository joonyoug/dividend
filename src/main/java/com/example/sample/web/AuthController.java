package com.example.sample.web;

import com.example.sample.model.Auth;
import com.example.sample.security.TokenProvider;
import com.example.sample.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request){
        var result=this.memberService.register(request);
        return ResponseEntity.ok(request);

    }
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request){
        var member=this.memberService.authenticate(request);
        var token=this.tokenProvider.generateToken(member.getUsername(),member.getRoles());
        log.info("user login ->"+request.getUsername());

        return ResponseEntity.ok(token);
    }





}
