<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Markdown</title>
    <link href="${contextPath}/file/preview/static/vditor/3.9.9/index.css" rel="stylesheet">
    <script type="text/javascript" src="${contextPath}/file/preview/static/vditor/3.9.9/method.min.js"></script>
    <script type="text/javascript" src="${contextPath}/file/preview/static/js-base64/3.7.7/base64.min.js"></script>
</head>
<body>
<div id="content"></div>
<script>
    Vditor.preview(document.getElementById('content'), Base64.decode("${content}"), {})
</script>
</body>
</html>