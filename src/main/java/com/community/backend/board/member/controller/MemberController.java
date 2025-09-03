package com.community.backend.board.member.controller;

import com.community.backend.board.member.dto.MemberDto;
import com.community.backend.board.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<MemberDto.Response> register(@RequestBody MemberDto.Request request) {
        MemberDto.Response response = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto.Response> getMember(@PathVariable Long id) {
        MemberDto.Response response = memberService.getMember(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MemberDto.Response>> getAllMembers() {
        List<MemberDto.Response> responses = memberService.getAllMembers();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto.Response> updateMember(
            @PathVariable Long id,
            @RequestBody MemberDto.Request request) {
        MemberDto.Response response = memberService.updateMember(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }
}
