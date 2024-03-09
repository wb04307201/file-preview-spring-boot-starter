<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Video</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/video.js/8.10.0/video.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${contextPath}/file/preview/static/video.js/8.10.0/video-js.min.css"/>
    <style>
        html {
            height: 100%;
            width: 100%;
        }

        body {
            height: 100%;
            width: 100%;
            margin: 0;
        }
    </style>
</head>
<body>
<video
        id="my-player"
        class="video-js"
        controls
        preload="auto"
        autoplay
        <#--poster="//vjs.zencdn.net/v/oceans.png"-->
        data-setup='{}'
        style="height: 100%;width: 100%">
    <source src="${url}" type="video/mp4"/>
    <source src="${url}" type="video/webm"/>
    <source src="${url}" type="video/ogg"/>
    <p class="vjs-no-js">
        To view this video please enable JavaScript, and consider upgrading to a
        web browser that
        <a href="https://videojs.com/html5-video-support/" target="_blank">
            supports HTML5 video
        </a>
    </p>
</video>
</body>
</html>