<template>
  <div class="post-detail-container">
    <div class="loading-indicator" v-if="loading">로딩 중...</div>
    <div class="error-message" v-if="error">{{ error }}</div>

    <div v-if="post && !loading" class="post-content">
      <div class="post-header">
        <h1 class="post-title">{{ post.title }}</h1>
        <div class="post-meta">
          <span class="author">작성자: {{ post.author }}</span>
          <span class="date">{{ formatDate(post.createdAt) }}</span>
          <span class="views">조회수: {{ post.viewCount }}</span>
        </div>
      </div>

      <div class="post-body">
        <p>{{ post.content }}</p>
      </div>

      <div class="post-actions">
        <button class="btn list-btn" @click="goToList">목록</button>
        <div v-if="isAuthor">
          <button class="btn edit-btn" @click="editPost">수정</button>
          <button class="btn delete-btn" @click="confirmDelete">삭제</button>
        </div>
      </div>

      <div class="comments-section">
        <h3>댓글 <span class="comment-count">({{ comments.length }})</span></h3>

        <div v-if="comments.length === 0" class="no-comments">
          첫 댓글을 작성해보세요!
        </div>

        <div v-else class="comments-list">
          <div v-for="comment in comments" :key="comment.id" class="comment">
            <div class="comment-header">
              <span class="comment-author">{{ comment.author }}</span>
              <span class="comment-date">{{ formatDate(comment.createdAt) }}</span>
            </div>
            <div class="comment-content">{{ comment.content }}</div>
            <div v-if="comment.authorId === userId" class="comment-actions">
              <button class="btn-link" @click="editComment(comment)">수정</button>
              <button class="btn-link" @click="deleteComment(comment.id)">삭제</button>
            </div>
          </div>
        </div>

        <div class="comment-form">
          <h4>{{ editingComment ? '댓글 수정' : '댓글 작성' }}</h4>
          <textarea 
            v-model="commentContent" 
            class="comment-textarea" 
            placeholder="댓글을 입력하세요"
            rows="3"
          ></textarea>
          <div class="comment-form-actions">
            <button 
              v-if="editingComment" 
              class="btn cancel-btn" 
              @click="cancelEdit"
            >
              취소
            </button>
            <button 
              class="btn submit-btn" 
              @click="submitComment"
            >
              {{ editingComment ? '수정' : '등록' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'PostDetail',
  props: {
    id: {
      type: [Number, String],
      required: true
    }
  },
  data() {
    return {
      post: null,
      comments: [],
      loading: false,
      error: null,
      commentContent: '',
      editingComment: null,
      userId: 1, // 임시 사용자 ID (실제로는 로그인 시스템에서 가져와야 함)
    };
  },
  computed: {
    isAuthor() {
      return this.post && this.post.authorId === this.userId;
    }
  },
  methods: {
    fetchPost() {
      this.loading = true;
      this.error = null;

      axios.get(`http://localhost:7777/api/posts/${this.id}`)
        .then(response => {
          this.post = response.data;
          this.loading = false;
          this.fetchComments();
        })
        .catch(error => {
          this.error = '게시글을 불러오는데 실패했습니다.';
          console.error('API 호출 실패:', error);
          this.loading = false;
        });
    },
    fetchComments() {
      axios.get(`http://localhost:7777/api/posts/${this.id}/comments`)
        .then(response => {
          this.comments = response.data;
        })
        .catch(error => {
          console.error('댓글 불러오기 실패:', error);
        });
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hours = String(date.getHours()).padStart(2, '0');
      const minutes = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day} ${hours}:${minutes}`;
    },
    goToList() {
      this.$router.push('/board');
    },
    editPost() {
      this.$router.push(`/post/edit/${this.id}`);
    },
    confirmDelete() {
      if (confirm('정말로 게시글을 삭제하시겠습니까?')) {
        this.deletePost();
      }
    },
    deletePost() {
      axios.delete(`http://localhost:7777/api/posts/${this.id}`)
        .then(() => {
          alert('게시글이 삭제되었습니다.');
          this.$router.push('/board');
        })
        .catch(error => {
          alert('게시글 삭제에 실패했습니다.');
          console.error('삭제 실패:', error);
        });
    },
    submitComment() {
      if (!this.commentContent.trim()) {
        alert('댓글 내용을 입력해주세요.');
        return;
      }

      if (this.editingComment) {
        // 댓글 수정
        axios.put(`http://localhost:7777/api/comments/${this.editingComment.id}`, {
          content: this.commentContent,
        })
          .then(() => {
            this.fetchComments();
            this.commentContent = '';
            this.editingComment = null;
          })
          .catch(error => {
            alert('댓글 수정에 실패했습니다.');
            console.error('댓글 수정 실패:', error);
          });
      } else {
        // 새 댓글 작성
        axios.post(`http://localhost:7777/api/posts/${this.id}/comments`, {
          content: this.commentContent,
          authorId: this.userId
        })
          .then(() => {
            this.fetchComments();
            this.commentContent = '';
          })
          .catch(error => {
            alert('댓글 등록에 실패했습니다.');
            console.error('댓글 등록 실패:', error);
          });
      }
    },
    editComment(comment) {
      this.editingComment = comment;
      this.commentContent = comment.content;
    },
    cancelEdit() {
      this.editingComment = null;
      this.commentContent = '';
    },
    deleteComment(commentId) {
      if (confirm('정말로 댓글을 삭제하시겠습니까?')) {
        axios.delete(`http://localhost:7777/api/comments/${commentId}`)
          .then(() => {
            this.fetchComments();
          })
          .catch(error => {
            alert('댓글 삭제에 실패했습니다.');
            console.error('댓글 삭제 실패:', error);
          });
      }
    }
  },
  mounted() {
    this.fetchPost();
  }
};
</script>

<style scoped>
.post-detail-container {
  width: 100%;
  background-color: #fff;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.post-header {
  border-bottom: 1px solid #eee;
  padding-bottom: 1rem;
  margin-bottom: 1.5rem;
}

.post-title {
  color: #333;
  margin-top: 0;
  margin-bottom: 0.5rem;
  font-size: 1.8rem;
}

.post-meta {
  display: flex;
  font-size: 0.9rem;
  color: #6c757d;
  flex-wrap: wrap;
  gap: 1rem;
}

.post-body {
  min-height: 200px;
  margin-bottom: 2rem;
  line-height: 1.6;
}

.post-actions {
  display: flex;
  justify-content: space-between;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid #eee;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.list-btn {
  background-color: #f8f9fa;
  color: #333;
}

.list-btn:hover {
  background-color: #e9ecef;
}

.edit-btn {
  background-color: #4a6bff;
  color: white;
  margin-right: 8px;
}

.edit-btn:hover {
  background-color: #3a58d6;
}

.delete-btn {
  background-color: #dc3545;
  color: white;
}

.delete-btn:hover {
  background-color: #c82333;
}

.comments-section {
  margin-top: 2rem;
}

.comments-section h3 {
  font-size: 1.3rem;
  color: #333;
  margin-bottom: 1rem;
}

.comment-count {
  color: #6c757d;
  font-weight: normal;
}

.no-comments {
  color: #6c757d;
  text-align: center;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.comments-list {
  margin-bottom: 2rem;
}

.comment {
  border-bottom: 1px solid #eee;
  padding: 1rem 0;
}

.comment:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}

.comment-author {
  font-weight: bold;
  color: #333;
}

.comment-date {
  font-size: 0.85rem;
  color: #6c757d;
}

.comment-content {
  margin-bottom: 0.5rem;
  line-height: 1.5;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.btn-link {
  background: none;
  border: none;
  color: #4a6bff;
  padding: 0;
  font-size: 0.85rem;
  cursor: pointer;
  text-decoration: underline;
}

.btn-link:hover {
  color: #3a58d6;
}

.comment-form {
  margin-top: 2rem;
  padding-top: 1rem;
  border-top: 1px solid #eee;
}

.comment-form h4 {
  margin-top: 0;
  margin-bottom: 0.5rem;
  font-size: 1.1rem;
  color: #333;
}

.comment-textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 1rem;
  font-family: inherit;
}

.comment-form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.cancel-btn {
  background-color: #f8f9fa;
  color: #333;
}

.cancel-btn:hover {
  background-color: #e9ecef;
}

.submit-btn {
  background-color: #4a6bff;
  color: white;
}

.submit-btn:hover {
  background-color: #3a58d6;
}

.loading-indicator, .error-message {
  text-align: center;
  padding: 2rem 0;
  color: #6c757d;
}

.error-message {
  color: #dc3545;
}
</style>
