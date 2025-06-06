<template>
  <footer class="footer">
    <div class="container footer-container">
      <p class="copyright">&copy; {{ new Date().getFullYear() }} 커뮤니티 게시판. 모든 권리 보유.</p>
    </div>
  </footer>
</template>

<script>
export default {
  name: 'Footer'
}
</script>

<style scoped>
.footer {
  background-color: #f8f9fa;
  padding: 1.5rem 0;
  margin-top: 2rem;
}

.footer-container {
  display: flex;
  justify-content: center;
  align-items: center;
}

.copyright {
  color: #6c757d;
  margin: 0;
  font-size: 0.9rem;
  text-align: center;
}
</style>
