package com.communityboard.service;

import com.communityboard.dto.CommentDto;
import com.communityboard.entity.Comment;
import com.communityboard.entity.Post;
import com.communityboard.repository.CommentRepository;
import com.communityboard.repository.PostRepository;
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

        // 임시 사용자 이름 (실제로는 사용자 서비스에서 가져와야 함)
        String authorName = "사용자" + commentDto.getAuthorId();

        Comment comment = Comment.builder()
                .content(commentDto.getContent())
                .authorId(commentDto.getAuthorId())
                .author(authorName)
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
        if (!comment.getAuthorId().equals(commentDto.getAuthorId())) {
            throw new IllegalArgumentException("댓글을 수정할 권한이 없습니다.");
        }

        comment.setContent(commentDto.getContent());

        Comment updatedComment = commentRepository.save(comment);
        return CommentDto.fromEntity(updatedComment);
    }

    @Transactional
    public void deleteComment(Long id, Long authorId) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다: " + id));

        // 작성자 일치 여부 확인 (실제로는 보안 관련 코드 추가 필요)
        if (!comment.getAuthorId().equals(authorId)) {
            throw new IllegalArgumentException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
