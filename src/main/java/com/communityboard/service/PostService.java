package com.communityboard.service;

import com.communityboard.dto.PostDto;
import com.communityboard.entity.Post;
import com.communityboard.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public Page<PostDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<PostDto> searchPosts(String searchType, String keyword, Pageable pageable) {
        Page<Post> postPage;

        if (keyword == null || keyword.isBlank()) {
            return getAllPosts(pageable);
        }

        switch (searchType) {
            case "title":
                postPage = postRepository.findByTitleContaining(keyword, pageable);
                break;
            case "content":
                postPage = postRepository.findByContentContaining(keyword, pageable);
                break;
            case "author":
                postPage = postRepository.findByAuthorContaining(keyword, pageable);
                break;
            default:
                postPage = postRepository.findAll(pageable);
        }

        return postPage.map(PostDto::fromEntity);
    }

    @Transactional
    public PostDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다: " + id));
        post.incrementViewCount();
        return PostDto.fromEntity(post);
    }

    @Transactional
    public PostDto createPost(PostDto postDto) {
        // 임시 사용자 이름 (실제로는 사용자 서비스에서 가져와야 함)
        String authorName = "사용자" + postDto.getAuthorId();

        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .authorId(postDto.getAuthorId())
                .author(authorName)
                .viewCount(0)
                .build();

        Post savedPost = postRepository.save(post);
        return PostDto.fromEntity(savedPost);
    }

    @Transactional
    public PostDto updatePost(Long id, PostDto postDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다: " + id));

        // 작성자 일치 여부 확인 (실제로는 보안 관련 코드 추가 필요)
        if (!post.getAuthorId().equals(postDto.getAuthorId())) {
            throw new IllegalArgumentException("게시글을 수정할 권한이 없습니다.");
        }

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());

        Post updatedPost = postRepository.save(post);
        return PostDto.fromEntity(updatedPost);
    }

    @Transactional
    public void deletePost(Long id, Long authorId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다: " + id));

        // 작성자 일치 여부 확인 (실제로는 보안 관련 코드 추가 필요)
        if (!post.getAuthorId().equals(authorId)) {
            throw new IllegalArgumentException("게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }
}
