<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Audio</title>
    <script type="text/javascript" src="${contextPath}/epub.js/0.3.88/epub.min.js"></script>
    <script type="text/javascript" src="${contextPath}/jszip/3.10.1/jszip.min.js"></script>
</head>
<body>
<div id="area"></div>
<script>
    var book = ePub("${url}");
    var rendition = book.renderTo("area", { method: "default", width: "100%", height: "100%" });;
    var displayed = rendition.display();
</script>
</body>
</html>