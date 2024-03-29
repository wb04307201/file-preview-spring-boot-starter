<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeMirror</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/js-base64/3.7.7/base64.min.js"></script>
    <script>
        const language = "${language}"
        const context = Base64.decode("${content}")
    </script>
  <script type="module" crossorigin src="${contextPath}/file/preview/static/codemirror/6.0.1/index-816cd1b6.js"></script>
  <link rel="stylesheet" href="${contextPath}/file/preview/static/codemirror/6.0.1/index-d95d1fef.css">
</head>
<body>
<div id="app"></div>

</body>
</html>
