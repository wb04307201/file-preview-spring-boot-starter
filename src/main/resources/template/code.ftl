<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>CodeMirror</title>
    <script type="text/javascript" src="${contextPath}/js-base64/3.7.2/base64.min.js"></script>
    <script>
        const language = "${language}"
        const context = Base64.decode("${content}")
    </script>
  <script type="module" crossorigin src="${contextPath}/codemirror/6.0.1/index-599f5341.js"></script>
  <link rel="stylesheet" href="${contextPath}/codemirror/6.0.1/index-d95d1fef.css">
</head>
<body>
<div id="app"></div>

</body>
</html>
