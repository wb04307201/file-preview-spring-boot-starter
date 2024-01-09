<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>bpmn</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/js-base64/3.7.2/base64.min.js"></script>
    <script src="${contextPath}/file/preview/static/cmmn-js/0.20.0/cmmn-viewer.production.min.js"></script>
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
    </style>
</head>
<body>
<script>
    const xml = Base64.decode("${content}"); // my BPMN 2.0 xml
    const viewer = new CmmnJS({
        container: 'body'
    });
    viewer.importXML(xml, function (err) {
        if (err) {
            console.log('error rendering', err);
        } else {
            console.log('rendered');
        }
    });
</script>
</body>
</html>
