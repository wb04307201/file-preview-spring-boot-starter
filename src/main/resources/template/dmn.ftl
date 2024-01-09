<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>bpmn</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/js-base64/3.7.2/base64.min.js"></script>
    <link rel="stylesheet" href="${contextPath}/file/preview/static/dmn-js/15.0.0/assets/dmn-js-drd.css">
    <link rel="stylesheet" href="${contextPath}/file/preview/static/dmn-js/15.0.0/assets/dmn-js-decision-table.css">
    <link rel="stylesheet" href="${contextPath}/file/preview/static/dmn-js/15.0.0/assets/dmn-js-literal-expression.css">
    <link rel="stylesheet" href="${contextPath}/file/preview/static/dmn-js/15.0.0/assets/dmn-js-shared.css">
    <link rel="stylesheet" href="${contextPath}/file/preview/static/dmn-js/15.0.0/assets/dmn-font/css/dmn.css">
    <script src="${contextPath}/file/preview/static/dmn-js/15.0.0/dmn-viewer.production.min.js"></script>
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
    const viewer = new DmnJS({
        container: 'body'
    });
    try {
        viewer.importXML(xml);
        console.log('rendered');
    } catch (err) {
        console.log('error rendering', err)
    }
</script>
</body>
</html>
