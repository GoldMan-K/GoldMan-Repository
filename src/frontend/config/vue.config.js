const path = require('path')

module.exports = {
  devServer: {
    port: 8080,
    proxy: {
      '/home': {
        target: 'http://localhost:7777',
        changeOrigin: true
      }
    }
  },
  outputDir: path.resolve(__dirname, 'src/frontend'),
  configureWebpack: {
    entry: './src/main.js'
  }
}
