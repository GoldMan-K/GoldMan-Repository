package com.community.backend.board.post.entity;

import com.community.backend.board.comment.entity.Comment;
import com.community.backend.board.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Member member;

    @Column(name = "author_name", nullable = false)
    private String author;

    @Column(name = "view_count")
    private int viewCount;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public void incrementViewCount() {
        this.viewCount++;
    }

    // member 엔티티에서 작성자 ID를 가져오는 편의 메서드
    public Long getAuthorId() {
        return member != null ? member.getId() : null;
    }

    // authorId를 받아서 member 설정을 위한 편의 메서드
    public void setAuthorId(Long authorId) {
        if (this.member == null) {
            this.member = new Member();
        }
        this.member.setId(authorId);
    }

    // Builder 패턴에서 authorId를 사용할 수 있도록 하는 정적 내부 클래스
    public static class PostBuilder {
        public PostBuilder authorId(Long authorId) {
            if (this.member == null) {
                this.member = new Member();
            }
            this.member.setId(authorId);
            return this;
        }
    }
}
