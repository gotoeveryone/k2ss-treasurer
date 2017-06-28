const path = require('path');
const ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = [
    {
        entry: {
            app: './js/app.js',
        },
        output: {
            path: path.join(__dirname, 'public/javascripts'),
            filename: '[name].js',
        },
        resolve: {
            extensions: ['.vue', '.js'],
        },
        module: {
            loaders: [
                {
                    test: /\.vue$/,
                    loader: 'vue-loader',
                    options: {
                        loaders: {
                            js: 'buble-loader'
                        }
                    },
                },
                {
                    test: /\.js$/,
                    exclude: /node_modules/,
                    loader: 'buble-loader',
                },
            ],
        },
    },
    {
        entry: {
            app: './sass/app.scss',
        },
        output: {
            path: path.join(__dirname, 'public/stylesheets'),
            filename: '[name].css',
        },
        resolve: {
            extensions: ['.scss', 'css'],
        },
        plugins: [
            new ExtractTextPlugin({
                filename: '[name].css',
                disable: false,
                allChunks: true,
            }),
        ],
        module: {
            loaders: [
                {
                    test: /\.scss$/,
                    exclude: /node_modules/,
                    use: ExtractTextPlugin.extract({
                        fallback: 'style-loader',
                        use: 'css-loader!sass-loader',
                    }),
                },
            ],
        },
    },
];
