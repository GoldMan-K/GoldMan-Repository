 module.exports = {
   devServer: {
     proxy: {
       '/': {
         target: 'http://localhost:7777', // Spring Boot 서버 주소
         changeOrigin: true,
       },
     },
   },
 };