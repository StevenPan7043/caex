// const BASE_URL = process.env.NODE_ENV === 'production' ? '/datalab' : '/'
module.exports = {
    configureWebpack: {
        resolve: {
            alias: {
                '~': '@/assets'
            }
        }
    },
    // publicPath: BASE_URL,
    productionSourceMap:false,
    publicPath: "./"
}