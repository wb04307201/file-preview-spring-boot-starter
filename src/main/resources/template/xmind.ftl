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
    });
    const xhr = new XMLHttpRequest();
    xhr.open('GET', '${url}', false); //同步清华求
    xhr.responseType = 'blob';  // 重点
    xhr.send(null)
    if (xhr.status === 200) {
        const data = new Blob([xhr.response]);
        viewer.load(data)
    }
</script>
</body>
</html>