<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Xmind</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/file/preview/static/bootstrap/5.3.1/css/bootstrap.min.css"/>
    <script type="text/javascript" src="${contextPath}/file/preview/static/bootstrap/5.3.1/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript" src="${contextPath}/file/preview/static/xmind-embed-viewer/1.2.0/xmind-embed-viewer.js"></script>
    <script type="module" src="${contextPath}/file/preview/static/fetch/3.6.2/fetch.js"></script>
    <style>
        #container-or-iframe-selector {
            width: 100%;
            height: 500px;
        }
    </style>
</head>
<body>
<div class="container">
    <div id="container-or-iframe-selector"></div>
</div>
<script>
    const viewer = new XMindEmbedViewer({
        el: '#container-or-iframe-selector', // HTMLElement | HTMLIFrameElement | string
        styles: {height: "100%", width: "100%"},
    });
    fetch('${url}')
        .then(res => res.arrayBuffer())
        .then(file => viewer.load(file))
</script>
</body>
</html>