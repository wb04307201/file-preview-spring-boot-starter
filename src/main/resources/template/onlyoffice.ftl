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
            font-family: Arial, Tahoma, sans-serif;
            font-size: 12px;
            font-weight: normal;
            height: 100%;
            margin: 0;
            overflow-y: hidden;
            padding: 0;
            text-decoration: none;
        }
    </style>
    <script src="${url}"></script>
    <script inline="javascript">
        var config = {
            "type": "desktop",
            "width": "100%",
            "height": "100%",
            "documentType": "${documentType}",
            "document": {
                "fileType": "${fileType}",
                "key": "${key}",
                "title": "${title}",
                "url": "${downloadUrl}"
            },
            "editorConfig": {
                "mode": "view",
                "callbackUrl": "${callbackUrl}",
                "lang": "${lang}",
                "user": {
                    "id":"${userid}",
                    "name":"${username}"
                }
            }
        };
    </script>
</head>
<body>
<div id="placeholder"></div>
<script language="javascript" type="text/javascript">
    var docEditor = new DocsAPI.DocEditor("placeholder", config);
</script>
</body>
</html>