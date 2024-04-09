<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Compressed File</title>
    <link href="${contextPath}/file/preview/static/layui/2.9.6/css/layui.css" rel="stylesheet">
    <script type="text/javascript" src="${contextPath}/file/preview/static/layui/2.9.6/layui.js"></script>
</head>
<body>
<div class="layui-layout layui-layout-admin">
    <div class="layui-side">
        <div id="compress-tree"></div>
    </div>
    <div class="layui-body">
        <table class="layui-hide" id="compress-table" lay-filter="compress-table">
        </table>
    </div>
</div>
<!-- 操作列 -->
<script type="text/html" id="table-templet-operator">
    <div class="layui-clear-space">
        <a class="layui-btn layui-btn-xs" lay-event="preview">预览</a>
        <a class="layui-btn layui-btn-xs" lay-event="download">下载</a>
    </div>
</script>
<script>
    const tree = layui.tree;
    const table = layui.table;
    const mainid = '${mainid}';
    const data = [
        <#list list as row>
        {fileName: "${row.fileName}", fileType: "${row.fileType}", id: "${row.id}"},
        </#list>
    ]

    function buildTree(parent, deep) {
        if ("根目录" === parent.title) parent.children = data.filter(item => item.fileType === "directory" && item.fileName.split("/").length === deep + 2).map(item => {
            return buildTree({title: item.fileName, id: item.id}, deep + 1)
        })
        else parent.children = data.filter(item => item.fileType === "directory" && item.fileName.startsWith(parent.fileName) && item.fileName.split("/").length === deep + 2).map(item => {
            return buildTree({title: item.fileName, id: item.id}, deep + 1)
        })
        return parent;
    }

    function buildData(title) {
        if ("根目录" === title) return data.filter(item => item.fileType === "file").map(item => {
            return {...item, preview: "待实现", download: "待实现"}
        })
        else return data.filter(item => item.fileType === "file" && item.fileName.startsWith(title)).map(item => {
            return {...item, preview: "待实现", download: "待实现"}
        })
    }

    layui.use(function () {
        let treeData = buildTree({
            title: '根目录',
            id: "root",
            spread: true,
        }, 0)
        // 渲染
        tree.render({
            elem: '#compress-tree',
            data: [treeData],
            onlyIconControl: true,  // 是否仅允许节点左侧图标控制展开收缩
            showLine: false,  // 是否开启连接线
            click: function (obj) {
                table.reload("compress-table", {
                    data: buildData(obj.data.title)
                });
            }
        });

        table.render({
            elem: '#compress-table',
            cols: [[ //标题栏
                {type: 'numbers', fixed: 'left'},
                {field: 'id', title: 'ID', width: 80, fixed: 'left', hide: true},
                {field: 'fileName', title: '文件名', minWidth: 300},
                /*{field: 'fileType', title: '类型', width: 100},*/
                /*{field: 'preview', title: '预览', width: 80},
                {field: 'download', title: '下载', width: 80},*/
                {field: 'operator', title: '操作', width: 150, fixed: 'right', templet: '#table-templet-operator'},
            ]],
            data: buildData("根目录"),
            //skin: 'line', // 表格风格
            //even: true,
            //page: true, // 是否显示分页
            //limits: [5, 10, 15],
            //limit: 5 // 每页默认显示的数量
        });

        // 操作列事件
        table.on('tool(compress-table)', function (obj) {
            let data = obj.data; // 获得当前行数据
            switch (obj.event) {
                case 'preview':
                    previewRow(data.fileName)
                    break;
                case 'download':
                    downloadRow(data.fileName)
                    break;
            }
        })

        function previewRow(fileName) {
            window.open("${contextPath}/file/preview?id=" + mainid + "#" + fileName);

        }

        function downloadRow(fileName) {
            window.open("${contextPath}/file/download?id=" + mainid + "#" + fileName);
        }
    });
</script>
</body>
</html>