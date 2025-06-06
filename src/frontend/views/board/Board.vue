<template>
  <div class="board-container">
    <h2 class="board-title">커뮤니티 게시판</h2>

    <div class="board-filter">
      <select v-model="searchType" class="filter-select">
        <option value="title">제목</option>
        <option value="content">내용</option>
        <option value="author">작성자</option>
      </select>
      <input 
        type="text" 
        v-model="searchKeyword" 
        class="filter-input" 
        placeholder="검색어를 입력하세요"
        @keyup.enter="searchPosts"
      />
      <button class="btn search-btn" @click="searchPosts">검색</button>
    </div>

    <div class="loading-indicator" v-if="loading">로딩 중...</div>
    <div class="error-message" v-if="error">{{ error }}</div>

    <table class="board-table" v-if="!loading && !error">
      <thead>
        <tr>
          <th class="id-column">번호</th>
          <th class="title-column">제목</th>
          <th class="author-column">작성자</th>
          <th class="date-column">작성일</th>
          <th class="view-column">조회수</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="posts.length === 0">
          <td colspan="5" class="no-data">게시글이 없습니다.</td>
        </tr>
        <tr v-for="post in posts" :key="post.id" @click="viewPost(post.id)" class="post-row">
          <td>{{ post.id }}</td>
          <td class="title-cell">
            {{ post.title }}
            <span class="comment-count" v-if="post.commentCount > 0">[{{ post.commentCount }}]</span>
          </td>
          <td>{{ post.author }}</td>
          <td>{{ formatDate(post.createdAt) }}</td>
          <td>{{ post.viewCount }}</td>
        </tr>
      </tbody>
    </table>

    <div class="board-footer">
      <div class="pagination">
        <button 
          :disabled="currentPage === 1" 
          @click="changePage(currentPage - 1)" 
          class="btn page-btn"
        >
          이전
        </button>
        <span class="page-info">{{ currentPage }} / {{ totalPages }}</span>
        <button 
          :disabled="currentPage === totalPages" 
          @click="changePage(currentPage + 1)" 
          class="btn page-btn"
        >
          다음
        </button>
      </div>
      <button class="btn write-btn" @click="writePost">글쓰기</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Board',
  data() {
    return {
      posts: [],
      loading: false,
      error: null,
      currentPage: 1,
      totalPages: 1,
      searchType: 'title',
      searchKeyword: ''
    };
  },
  methods: {
    fetchPosts(page = 1) {
      this.loading = true;
      this.error = null;

      axios.get('http://localhost:7777/api/posts', {
        params: {
          page: page - 1,  // Spring Data JPA는 페이지를 0부터 시작
          size: 10,
          searchType: this.searchType,
          keyword: this.searchKeyword
        }
      })
      .then(response => {
        this.posts = response.data.content;
        this.totalPages = response.data.totalPages;
        this.currentPage = page;
        this.loading = false;
      })
      .catch(error => {
        this.error = '게시글을 불러오는데 실패했습니다.';
        console.error('API 호출 실패:', error);
        this.loading = false;
      });
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    viewPost(id) {
      this.$router.push(`/post/${id}`);
    },
    writePost() {
      this.$router.push('/post/write');
    },
    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.fetchPosts(page);
    },
    searchPosts() {
      this.currentPage = 1;
      this.fetchPosts();
    }
  },
  mounted() {
    this.fetchPosts();
  }
};
</script>

<style scoped>
.board-container {
  width: 100%;
  background-color: #fff;
  border-radius: 8px;
  padding: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.board-title {
  color: #4a6bff;
  margin-top: 0;
  margin-bottom: 1.5rem;
  font-size: 1.8rem;
  text-align: center;
}

.board-filter {
  display: flex;
  margin-bottom: 1.5rem;
  justify-content: flex-end;
  gap: 8px;
}

.filter-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: white;
}

.filter-input {
  flex: 1;
  max-width: 300px;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.board-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 1.5rem;
}

.board-table th, .board-table td {
  padding: 12px 15px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

.board-table thead {
  background-color: #f8f9fa;
}

.board-table th {
  font-weight: 600;
}

.id-column {
  width: 10%;
}

.title-column {
  width: 50%;
}

.author-column, .date-column, .view-column {
  width: 15%;
}

.title-cell {
  text-align: left;
}

.comment-count {
  color: #4a6bff;
  margin-left: 5px;
  font-weight: bold;
}

.post-row {
  cursor: pointer;
  transition: background-color 0.2s;
}

.post-row:hover {
  background-color: #f8f9fa;
}

.board-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1rem;
}

.pagination {
  display: flex;
  align-items: center;
  gap: 10px;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.search-btn {
  background-color: #4a6bff;
  color: white;
}

.search-btn:hover {
  background-color: #3a58d6;
}

.page-btn {
  background-color: #f8f9fa;
  color: #333;
}

.page-btn:hover:not(:disabled) {
  background-color: #e9ecef;
}

.page-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-info {
  color: #6c757d;
}

.write-btn {
  background-color: #4a6bff;
  color: white;
}

.write-btn:hover {
  background-color: #3a58d6;
}

.loading-indicator, .error-message, .no-data {
  text-align: center;
  padding: 2rem 0;
  color: #6c757d;
}

.error-message {
  color: #dc3545;
}
</style>
