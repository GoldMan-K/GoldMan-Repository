<template>
  <header class="header">
    <div class="container header-container">
      <h1 class="logo">
        <router-link to="/" class="logo-link">커뮤니티 게시판</router-link>
      </h1>
      <nav class="main-nav">
        <ul class="nav-list">
          <li class="nav-item">
            <router-link to="/" class="nav-link">홈</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/board" class="nav-link">게시판</router-link>
          </li>
        </ul>
      </nav>
    </div>
  </header>
</template>

<script>
export default {
  name: 'Header'
}
</script>

<style scoped>
.header {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 1rem 0;
}

.header-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  margin: 0;
  font-size: 1.5rem;
}

.logo-link {
  color: #4a6bff;
  text-decoration: none;
  font-weight: bold;
}

.main-nav {
  display: flex;
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 1.5rem;
}

.nav-link {
  color: #333;
  text-decoration: none;
  font-weight: 500;
  padding: 0.5rem 0;
  transition: color 0.2s;
}

.nav-link:hover, .router-link-active {
  color: #4a6bff;
}
</style>
