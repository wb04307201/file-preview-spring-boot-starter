<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Audio</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/o3dv/0.14.0/o3dv.min.js"></script>
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
<div class="online_3d_viewer"
     style="width: 100%; height: 100%; overflow: hidden;"
     model="${contextPath}/file/preview/static/rhombicuboctahedron.3mf">
</div>
<script>
    window.addEventListener ('load', () => {
        // tell the engine where to find the libs folder
        OV.SetExternalLibLocation ('libs');
        // init all viewers on the page
        OV.Init3DViewerElements ();

        /*// get the parent element of the viewer
        let parentDiv = document.getElementById ('viewer');

        // initialize the viewer with the parent element and some parameters
        let viewer = new OV.EmbeddedViewer (parentDiv, {
            camera : new OV.Camera (
                new OV.Coord3D (-1.5, 2.0, 3.0),
                new OV.Coord3D (0.0, 0.0, 0.0),
                new OV.Coord3D (0.0, 1.0, 0.0),
                45.0
            ),
            backgroundColor : new OV.RGBAColor (255, 255, 255, 255),
            defaultColor : new OV.RGBColor (200, 200, 200),
            edgeSettings : new OV.EdgeSettings (false, new OV.RGBColor (0, 0, 0), 1),
            environmentSettings : new OV.EnvironmentSettings (
                [
                    '${contextPath}/file/preview/static/o3dv/0.14.0/envmaps/fishermans_bastion/posx.jpg',
                    '${contextPath}/file/preview/static/o3dv/0.14.0/envmaps/fishermans_bastion/negx.jpg',
                    '${contextPath}/file/preview/static/o3dv/0.14.0/envmaps/fishermans_bastion/posy.jpg',
                    '${contextPath}/file/preview/static/o3dv/0.14.0/envmaps/fishermans_bastion/negy.jpg',
                    '${contextPath}/file/preview/static/o3dv/0.14.0/envmaps/fishermans_bastion/posz.jpg',
                    '${contextPath}/file/preview/static/o3dv/0.14.0/envmaps/fishermans_bastion/negz.jpg'
                ],
                false
            )
        });

        // load a model providing model urls
        viewer.LoadModelFromUrlList ([
            '${url}'
        ]);*/
    });
</script>
</body>
</html>