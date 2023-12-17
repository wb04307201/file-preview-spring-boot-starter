<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Video</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/video.js/7.10.2/video.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${contextPath}/file/preview/static/video.js/7.10.2/video-js.min.css"/>
    <link rel="stylesheet" type="text/css" href="${contextPath}/file/preview/static/video.js/7.10.2/fantasy/index.css"/>
</head>
<body>
<video
        id="my-player"
        class="video-js vjs-theme-fantasy"
        controls
        preload="auto"
        autoplay
        <#--poster="//vjs.zencdn.net/v/oceans.png"-->
        data-setup='{}'>
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