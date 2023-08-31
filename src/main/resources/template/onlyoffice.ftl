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
    <script>
        var config = {
            "type": "desktop",
            "width": "100%",
            "height": "100%",
            "documentType": "${config.documentType}",
            "document": {
                "fileType": "${config.document.fileType}",
                "key": "${config.document.key}",
                "title": "${config.document.title}",
                "url": "${config.document.url}"
            },
            "editorConfig": {
                "mode": "view",
                "callbackUrl": "${config.editorConfig.callbackUrl}",
                "lang": "${config.editorConfig.lang}",
                "user": {
                    "id": "${config.editorConfig.user.id}",
                    "name": "${config.editorConfig.user.name}"
                }
            },
            <#if (config.token!"")?trim?length gt 1>"token": "${config.token}"</#if>
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