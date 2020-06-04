module.exports = {
    configureWebpack: {
        resolve: {
            // extensions:
            alias: {
                'assets': '@/assets',
                'front': '@/view/front',
                'back': '@/view/back',
            }
        }
    }
}