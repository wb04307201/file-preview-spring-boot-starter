<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Xmind</title>
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