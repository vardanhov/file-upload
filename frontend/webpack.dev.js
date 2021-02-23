const merge = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
    mode: 'development',
    devtool: 'source-map',
    devServer: {
        contentBase: './dist',
        compress: true,
        port: 8070,
        allowedHosts: [
            'localhost:9070'
        ],
        //stats и clientLogLevel убирают всю информацию из консоли, кроме ошибок.
        /*stats: 'errors-only',
        clientLogLevel: 'error'*/
    },
});