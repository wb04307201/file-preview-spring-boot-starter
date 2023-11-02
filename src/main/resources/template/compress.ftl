<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Compressed File</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/bootstrap/5.3.1/css/bootstrap.min.css"/>
    <script type="text/javascript" src="${contextPath}/bootstrap/5.3.1/js/bootstrap.bundle.min.js"></script>

    <link href="${contextPath}/layui/2.8.18/layui.css" rel="stylesheet">
    <script src="${contextPath}/layui/2.8.18/layui.js"></script>
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
<script>
    const tree = layui.tree;
    const table = layui.table;
    const data = [
        <#list list as row>
        {fileName: "${row.fileName}", fileType: "${row.fileType}", id: "id"},
        </#list>
    ]

    function buildTree(parent, deep) {
        if ("根目录" === parent.title)
            parent.children = data.filter(item => item.fileType === "directory" && item.fileName.split("/").length === deep + 2).map(item => {
                return buildTree({title: item.fileName, id: item.id}, deep + 1)
            })
        else
            parent.children = data.filter(item => item.fileType === "directory" && item.fileName.startsWith(parent.fileName) && item.fileName.split("/").length === deep + 2).map(item => {
                return buildTree({title: item.fileName, id: item.id}, deep + 1)
            })
        return parent;
    }

    function buildData(title) {
        if ("根目录" === title) {
            return data.filter(item => item.fileType === "file").map(item => {
                return {...item, preview: "待实现", download: "待实现"}
            })
        } else {
            return data.filter(item => item.fileType === "file" && item.fileName.startsWith(title)).map(item => {
                return {...item, preview: "待实现", download: "待实现"}
            })
        }
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
                {field: 'id', title: 'ID', width: 80},
                {field: 'fileName', title: '文件名', minWidth: 300},
                /*{field: 'fileType', title: '类型', width: 100},*/
                {field: 'preview', title: '预览', width: 80},
                {field: 'download', title: '下载', width: 80}
            ]],
            data: buildData("根目录"),
            //skin: 'line', // 表格风格
            //even: true,
            //page: true, // 是否显示分页
            //limits: [5, 10, 15],
            //limit: 5 // 每页默认显示的数量
        });
    });
</script>
</body>
</html>