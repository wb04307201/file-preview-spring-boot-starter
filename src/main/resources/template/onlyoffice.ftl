<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Only Office</title>
    <style>
        html {
            height: 100%;
            width: 100%;
        }

        body {
            background: #fff;
            color: #333;
            font-family: Arial, Tahoma,sans-serif;
            font-size: 12px;
            font-weight: normal;
            height: 100%;
            margin: 0;
            overflow-y: hidden;
            padding: 0;
            text-decoration: none;
        }

        .form {
            height: 100%;
        }

        div {
            margin: 0;
            padding: 0;
        }
    </style>
    <script src="${url}"></script>
    <script inline="javascript">
        var config = JSON.parse('${config}');
        console.log(config);
    </script>
</head>
<body>
<div class="form">
    <div id="iframeEditor"></div>
</div>
<script language="javascript" type="text/javascript">
    var docEditor = new DocsAPI.DocEditor("iframeEditor", config);
</script>
</body>
</html>