const merge = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
    mode: 'development',
    devtool: 'source-map',
    devServer: {
        contentBase: './dist',
        compress: true,
        port: 8888,
        allowedHosts: [
            'localhost:8888'
        ],
        //stats и clientLogLevel убирают всю информацию из консоли, кроме ошибок.
        /*stats: 'errors-only',
        clientLogLevel: 'error'*/
    },
});