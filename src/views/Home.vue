<template>
  <div class="home">
    <div class="welcome-card">
      <p>Spring Boot와 Vue.js로 만든 커뮤니티 게시판입니다.</p>
      <div class="status" v-if="loading">데이터를 불러오는 중...</div>
      <div class="status error" v-if="error">{{ error }}</div>
    </div>

    <div class="feature-section">
      <h3>주요 기능</h3>
      <div class="features">
        <div class="feature-card">
          <div class="icon">📝</div>
          <h4>게시판</h4>
          <p>다양한 주제의 게시판을 이용할 수 있습니다.</p>
        </div>
        <div class="feature-card">
          <div class="icon">💬</div>
          <h4>실시간 댓글</h4>
          <p>게시글에 실시간으로 댓글을 남겨보세요.</p>
        </div>
        <div class="feature-card">
          <div class="icon">👥</div>
          <h4>회원 관리</h4>
          <p>회원가입으로 더 많은 기능을 이용할 수 있습니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Home',
  data() {
    return {
      message: '환영합니다!',
      loading: false,
      error: null
    };
  },
  methods: {
    fetchMessage() {
      this.loading = true;
      this.error = null;

      axios.get('http://localhost:7777/home/index')
        .then(response => {
          this.message = response.data;
          this.loading = false;
        })
        .catch(error => {
          this.error = '서버 연결에 실패했습니다. 잠시 후 다시 시도해주세요.';
          console.error('API 호출 실패:', error);
          this.loading = false;
        });
    }
  },
  mounted() {
    this.fetchMessage();
  }
};
</script>

<style scoped>
.home {
  padding: 2rem 0;
}

.welcome-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 2rem;
  margin-bottom: 2rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.welcome-card h2 {
  color: #4a6bff;
  margin-top: 0;
  margin-bottom: 1rem;
  font-size: 2rem;
}

.status {
  margin-top: 1rem;
  color: #6c757d;
}

.status.error {
  color: #dc3545;
}

.feature-section {
  margin-top: 3rem;
}

.feature-section h3 {
  text-align: center;
  margin-bottom: 2rem;
  font-size: 1.8rem;
  color: #333;
}

.features {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
}

.feature-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.feature-card h4 {
  color: #4a6bff;
  margin-top: 0;
  margin-bottom: 0.5rem;
}

.feature-card p {
  color: #6c757d;
  margin: 0;
}
</style>
