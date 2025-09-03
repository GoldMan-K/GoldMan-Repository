package com.community.backend.board.member.service;

import com.community.backend.board.member.dto.MemberDto;

import java.util.List;

public interface MemberService {
    MemberDto.Response register(MemberDto.Request request);
    MemberDto.Response getMember(Long id);
    List<MemberDto.Response> getAllMembers();
    MemberDto.Response updateMember(Long id, MemberDto.Request request);
    void deleteMember(Long id);
}
