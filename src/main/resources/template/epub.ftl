<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Epub</title>
    <link href="${contextPath}/file/preview/static/layui/2.9.6/css/layui.css" rel="stylesheet">
    <script type="text/javascript" src="${contextPath}/file/preview/static/layui/2.9.6/layui.js"></script>
    <script type="text/javascript" src="${contextPath}/file/preview/static/jszip/3.10.1/jszip.min.js"></script>
    <script type="text/javascript" src="${contextPath}/file/preview/static/epub.js/0.3.88/epub.min.js"></script>
    <style>
        html {
            height: 100%;
            overflow-y: hidden;
        }

        body, .layui-row, .layui-col-xs3, .layui-col-xs9, #epub-tree, #area {
            height: 100%;
        }

        #epub-tree {
            overflow-y: auto;
        }
    </style>
</head>
<body>
<div class="layui-row">
    <div class="layui-col-xs3">
        <div id="epub-tree"></div>
    </div>
    <div class="layui-col-xs9">
        <div id="area"></div>
    </div>
</div>
<script>
    let book = ePub("${url}", {openAs: "epub"});
    let rendition = book.renderTo("area", {method: "continuous", flow: "scrolled", width: "100%", height: "100%"});
    let displayed = rendition.display();

    layui.use(['tree'], function () {
        let tree = layui.tree;

        book.loaded.navigation.then(function (toc) {
            let treeData = [];
            toc.forEach(function (chapter) {
                let node = {
                    title: chapter.label,
                    id: chapter.href,
                    spread: true,
                }
                if (chapter.subitems && chapter.subitems.length) {
                    node.children = []
                    chapter.subitems.forEach(function (subitem) {
                        node.children.push({
                            title: subitem.label,
                            id: subitem.href
                        })
                    })
                }
                treeData.push(node)
            })

            // 渲染
            tree.render({
                elem: '#epub-tree',
                data: treeData,
                onlyIconControl: true,  // 是否仅允许节点左侧图标控制展开收缩
                showLine: false,  // 是否开启连接线
                click: function (obj) {
                    rendition.display(obj.data.id)
                }
            });
        })
    })
</script>
</body>
</html>