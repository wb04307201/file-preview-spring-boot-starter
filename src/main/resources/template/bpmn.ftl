<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>bpmn</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/js-base64/3.7.7/base64.min.js"></script>
    <link rel="stylesheet" href="${contextPath}/file/preview/static/bpmn-js/16.3.0/assets/bpmn-js.css"/>
    <script src="${contextPath}/file/preview/static/bpmn-js/16.3.0/bpmn-viewer.production.min.js"></script>
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
    const viewer = new BpmnJS({
        container: 'body'
    });
    try {
        viewer.importXML(xml);
        console.log('rendered');
    } catch (err) {
        console.log('error rendering', err);
    }
</script>
</body>
</html>
