<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Xmind</title>
    <script type="text/javascript" src="${contextPath}/xmind-embed-viewer/1.1.0/xmind-embed-viewer.js"></script>
</head>
<body>
<div id="container-or-iframe-selector"></div>
<script>
    const viewer = new XMindEmbedViewer({
        el: '#container-or-iframe-selector', // HTMLElement | HTMLIFrameElement | string
    })
    viewer.load('${url}')
    /*fetch('test-1.xmind')
        .then(res => res.arrayBuffer())
        .then(file => viewer.load(file))*/
</script>
</body>
</html>