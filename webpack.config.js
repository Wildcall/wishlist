const path = require('path')
const {CleanWebpackPlugin} = require('clean-webpack-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin')
const {merge} = require('webpack-merge');
const {VueLoaderPlugin} = require('vue-loader')

const PATHS = {
    entry: path.join(__dirname, 'src', 'main', 'resources', 'js', 'main.js'),
    output: path.resolve(__dirname, 'src', 'main', 'resources', 'static', 'js'),
}

const commonConfig = {
    entry: PATHS.entry,
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: '/node_modules/',
                loader: 'babel-loader',
            },
            {
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            {
                test: /\.css$/,
                use: [
                    'vue-style-loader',
                    'css-loader'
                ],
            },
        ]
    },
    plugins: [
        new VueLoaderPlugin()
    ],
    resolve: {
        modules: [
            path.join(__dirname, 'src', 'main', 'resources', 'js'),
            path.join(__dirname, 'node_modules'),
        ],
    }
}

const productionConfig = {
    mode: "production",
    output: {
        filename: 'main.js',
        path: PATHS.output,
        clean: true
    },
    plugins: [
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin({
            title: 'Output Management',
        }),
    ],
}

const developmentConfig = {
    mode: "development",
    devtool: 'source-map',
    devServer: {
        client: {
            overlay: {
                warnings: true,
                errors: true
            },
        },
        allowedHosts: [
            'localhost:9000'
        ],
        compress: true,
        port: 8000
    }
}

module.exports = (env, args) => {
    switch (args.mode) {
        case 'development':
            return merge(commonConfig, developmentConfig);
        case 'production':
            return merge(commonConfig, productionConfig);
        default:
            console.log('No matching configuration was found!');
    }
}