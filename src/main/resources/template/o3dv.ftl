<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Audio</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/o3dv/0.14.0/o3dv.min.js"></script>
    <script type="text/javascript" src="${contextPath}/file/preview/static/js-base64/3.7.7/base64.min.js"></script>
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
<div id="online_3d_viewer" style="width: 100%; height: 100%; overflow: hidden;">
</div>
<script>
    window.addEventListener('load', async () => {
        // tell the engine where to find the libs folder
        OV.SetExternalLibLocation('libs');

        // get the parent element of the viewer
        let parentDiv = document.getElementById('online_3d_viewer');

        // initialize the viewer with the parent element and some parameters
        let viewer = new OV.EmbeddedViewer(parentDiv, {});

        const dataBlob = await fetch('${url}').then(res => res.blob());
        const dataFile = new File([dataBlob], '${fileName}');  // must manually provide correct extension

        viewer.LoadModelFromFileList([dataFile]);
    });
</script>
</body>
</html>