package com.community.backend.board.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<com.community.backend.board.post.entity.Post, Long> {

    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword%")
    Page<com.community.backend.board.post.entity.Post> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.content LIKE %:keyword%")
    Page<com.community.backend.board.post.entity.Post> findByContentContaining(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.author LIKE %:keyword%")
    Page<com.community.backend.board.post.entity.Post> findByAuthorContaining(@Param("keyword") String keyword, Pageable pageable);
}
