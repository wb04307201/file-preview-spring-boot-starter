<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Xmind</title>
    <script type="text/javascript"
            src="${contextPath}/file/preview/static/xmind-embed-viewer/1.2.0/xmind-embed-viewer.js"></script>
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

        #xmind-container {
            height: 100%;
            width: 100%;
        }
    </style>
</head>
<body>
<div id="xmind-container"></div>
<script>
    const viewer = new XMindEmbedViewer({
        el: '#xmind-container', // HTMLElement | HTMLIFrameElement | string
        styles: {height: "100%", width: "100%"},
    });
    fetch('${url}')
        .then(res => res.arrayBuffer())
        .then(file => viewer.load(file))
</script>
</body>
</html>