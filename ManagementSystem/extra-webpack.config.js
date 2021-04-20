const webpack = require('webpack');

module.exports = {
    plugins: [
        new webpack.DefinePlugin({
            'STABLE_FEATURE': JSON.stringify(true),
            'EXPERIMENTAL_FEATURE': JSON.stringify(false)
        }),
        new webpack.LoaderOptionsPlugin({
            options: {
                rules: [
                    {
                        test: /\.(png|jpg|gif)$/i,
                        use: [
                            {
                                loader: 'url-loader',
                                options: {
                                    limit: false,
                                },
                            },
                        ],
                    }
                ],
            }
        }),
    ],
};

