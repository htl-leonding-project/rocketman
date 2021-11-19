const PROXY_CONFIG = {
    "/api": {
        "target": "http:localhost:8080",
        "changeOrigin": true,
        "secure": false,
        "logLevel": "debug"
    }
};
module.exports = PROXY_CONFIG;
