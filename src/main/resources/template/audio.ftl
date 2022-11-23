<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Audio</title>
    <script type="text/javascript" src="${contextPath}/audio.js/1.0.1/audio.min.js"></script>
</head>
<body>
<audio src="${url}" preload="auto" autoplay/>
<script>
    audiojs.events.ready(function() {
        var as = audiojs.createAll();
    });
</script>
</body>
</html>