package com.community.backend.board.member.service.impl;

import com.community.backend.board.member.dto.MemberDto;
import com.community.backend.board.member.entity.Member;
import com.community.backend.board.member.repository.MemberRepository;
import com.community.backend.board.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public MemberDto.Response register(MemberDto.Request request) {
        // 이메일 중복 검사
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 사용자명 중복 검사
        if (memberRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }

        Member member = Member.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .phone(request.getPhone())
                .role(Member.Role.USER) // 기본 역할은 USER
                .build();

        Member savedMember = memberRepository.save(member);
        return MemberDto.Response.fromEntity(savedMember);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDto.Response getMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + id));
        return MemberDto.Response.fromEntity(member);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDto.Response> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberDto.Response::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MemberDto.Response updateMember(Long id, MemberDto.Request request) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + id));

        // 이메일 변경 시 중복 체크
        if (!member.getEmail().equals(request.getEmail()) && 
                memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        // 사용자명 변경 시 중복 체크
        if (!member.getUsername().equals(request.getUsername()) && 
                memberRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 사용자명입니다.");
        }

        member.setEmail(request.getEmail());
        member.setUsername(request.getUsername());
        member.setPhone(request.getPhone());

        // 비밀번호가 제공된 경우에만 업데이트
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        Member updatedMember = memberRepository.save(member);
        return MemberDto.Response.fromEntity(updatedMember);
    }

    @Override
    @Transactional
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다. ID: " + id);
        }
        memberRepository.deleteById(id);
    }
}
