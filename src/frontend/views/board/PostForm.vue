<template>
  <div class="post-form-container">
    <h2 class="form-title">{{ isEdit ? '게시글 수정' : '새 게시글 작성' }}</h2>

    <div class="loading-indicator" v-if="loading">로딩 중...</div>
    <div class="error-message" v-if="error">{{ error }}</div>

    <form @submit.prevent="submitPost" v-if="!loading">
      <div class="form-group">
        <label for="title">제목</label>
        <input 
          type="text" 
          id="title" 
          v-model="postData.title" 
          class="form-control" 
          placeholder="제목을 입력하세요"
          required
        />
      </div>

      <div class="form-group">
        <label for="content">내용</label>
        <textarea 
          id="content" 
          v-model="postData.content" 
          class="form-control" 
          placeholder="내용을 입력하세요"
          rows="10"
          required
        ></textarea>
      </div>

      <div class="form-actions">
        <button type="button" class="btn cancel-btn" @click="cancel">취소</button>
        <button type="submit" class="btn submit-btn">{{ isEdit ? '수정하기' : '작성하기' }}</button>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'PostForm',
  props: {
    postId: {
      type: [Number, String],
      required: false,
      default: null
    }
  },
  data() {
    return {
      postData: {
        title: '',
        content: '',
        authorId: 1 // 임시 사용자 ID (실제로는 로그인 시스템에서 가져와야 함)
      },
      loading: false,
      error: null
    };
  },
  computed: {
    isEdit() {
      return !!this.postId;
    }
  },
  methods: {
    fetchPost() {
      if (!this.isEdit) return;

      this.loading = true;
      this.error = null;

      axios.get(`http://localhost:7777/api/posts/${this.postId}`)
        .then(response => {
          const post = response.data;
          this.postData.title = post.title;
          this.postData.content = post.content;
          this.loading = false;
        })
        .catch(error => {
          this.error = '게시글을 불러오는데 실패했습니다.';
          console.error('API 호출 실패:', error);
          this.loading = false;
        });
    },
    submitPost() {
      if (!this.postData.title.trim() || !this.postData.content.trim()) {
        alert('제목과 내용을 모두 입력해주세요.');
        return;
      }

      this.loading = true;
      this.error = null;

      if (this.isEdit) {
        // 게시글 수정
        axios.put(`http://localhost:7777/api/posts/${this.postId}`, this.postData)
          .then(() => {
            alert('게시글이 수정되었습니다.');
            this.$router.push(`/post/${this.postId}`);
          })
          .catch(error => {
            this.error = '게시글 수정에 실패했습니다.';
            console.error('API 호출 실패:', error);
            this.loading = false;
          });
      } else {
        // 새 게시글 작성
        axios.post('http://localhost:7777/api/posts', this.postData)
          .then(response => {
            alert('게시글이 등록되었습니다.');
            this.$router.push(`/post/${response.data.id}`);
          })
          .catch(error => {
            this.error = '게시글 등록에 실패했습니다.';
            console.error('API 호출 실패:', error);
            this.loading = false;
          });
      }
    },
    cancel() {
      if (this.isEdit) {
        this.$router.push(`/post/${this.postId}`);
      } else {
        this.$router.push('/board');
      }
    }
  },
  mounted() {
    this.fetchPost();
  }
};
</script>

<style scoped>
.post-form-container {
  width: 100%;
  background-color: #fff;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.form-title {
  color: #4a6bff;
  margin-top: 0;
  margin-bottom: 1.5rem;
  font-size: 1.8rem;
  text-align: center;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
}

.form-control {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: inherit;
  font-size: 1rem;
}

textarea.form-control {
  resize: vertical;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 1.5rem;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  font-weight: 500;
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
  padding: 1rem 0;
  margin-bottom: 1rem;
}

.loading-indicator {
  color: #6c757d;
}

.error-message {
  color: #dc3545;
  background-color: #f8d7da;
  padding: 0.75rem;
  border-radius: 4px;
}
</style>
