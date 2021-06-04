
module.exports = {
  // 基本路径
  baseUrl: './',
  // 生产环境是否生成 sourceMap 文件
  productionSourceMap: false,
  configureWebpack: {
    plugins: []
  },
  chainWebpack(config) {
    config
      // https://webpack.js.org/configuration/devtool/#development
      .when(process.env.NODE_ENV === 'development',
        config => config.devtool('source-map')
      )
  },
  // devServer: {
  //   proxy: {
  //     '/swap-ex': {
  //       target: 'https://api.hotdogvip.com/',
  //       ws: true,
  //       changeOrigin: true
  //     },
  //   }
  // },
}
