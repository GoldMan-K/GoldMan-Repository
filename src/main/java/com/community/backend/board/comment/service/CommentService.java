package com.community.backend.board.comment.service;

import com.community.backend.board.comment.dto.CommentDto;
import com.community.backend.board.comment.entity.Comment;
import com.community.backend.board.comment.repository.CommentRepository;
import com.community.backend.board.member.entity.Member;
import com.community.backend.board.member.repository.MemberRepository;
import com.community.backend.board.post.entity.Post;
import com.community.backend.board.post.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostIdOrderByCreatedAtAsc(postId)
                .stream()
                .map(CommentDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다: " + postId));

        Member member = memberRepository.findById(commentDto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다: " + commentDto.getMemberId()));

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .member(member)
                .author(member.getUsername()) // 회원 이름 사용
                .post(post)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentDto.fromEntity(savedComment);
    }

    @Transactional
    public CommentDto updateComment(Long id, CommentDto commentDto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다: " + id));

        // 작성자 일치 여부 확인 (실제로는 보안 관련 코드 추가 필요)
        if (comment.getMember() == null || !comment.getMember().getId().equals(commentDto.getMemberId())) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setContent(commentDto.getContent());

        Comment updatedComment = commentRepository.save(comment);
        return CommentDto.fromEntity(updatedComment);
    }

    @Transactional
    public void deleteComment(Long id, Long memberId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다: " + id));

        // 작성자 일치 여부 확인 (실제로는 보안 관련 코드 추가 필요)
        if (comment.getMember() == null || !comment.getMember().getId().equals(memberId)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
