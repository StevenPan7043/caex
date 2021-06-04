const BASE_URL = process.env.NODE_ENV === 'production'
    ? './'
    : './'

module.exports = {

  baseUrl: BASE_URL,
  productionSourceMap: false,
  // 这里写你调用接口的基础路径，来解决跨域，如果设置了代理，那你本地开发环境的axios的baseUrl要写为 '' ，即空字符串
  devServer: {
    port: 9000,
  },
  chainWebpack (config) {
    config
        // https://webpack.js.org/configuration/devtool/#development
        .when(process.env.NODE_ENV === 'development',
            config => config.devtool('source-map')
        )
  }
}
