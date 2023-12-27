<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Audio</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/audio.js/1.0.1/audio.min.js"></script>
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
<audio src="${url}" preload="auto" autoplay style="height: 100%;width: 100%"/>
<script>
    audiojs.events.ready(function () {
        var as = audiojs.createAll();
    });
</script>
</body>
</html>