const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // 自定义IP和PORT
  "devServer": {
    "host": "localhost",
    "port": 7725
  }
})
