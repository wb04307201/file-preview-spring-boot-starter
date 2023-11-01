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
        <table class="table table-bordered table-striped table-sm">
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">文件名</th>
                <th scope="col">类型</th>
                <th scope="col">预览</th>
                <th scope="col">下载</th>
            </tr>
            </thead>
            <tbody>
            <#if list?? && (list?size > 0)>
                <#list list as row>
                    <tr>
                        <th scope="row">${row_index + 1}</th>
                        <td>${row.fileName}</td>
                        <td>${(row.fileType == 'directory')?string('文件夹','文件')}</td>
                        <td>${(row.fileType == 'directory')?string('-','待实现')}</td>
                        <td>${(row.fileType == 'directory')?string('-','待实现')}</td>
                    </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
</div>
<script>
    let tree = layui.tree;
    let treeData = [
        <#list list as row>
        {"fileName": ${row.fileName}, "fileType": ${row.fileType}},
        </#list>
    ]

    console.log("treeData", treeData)

    function buildTree(parent, deep) {
        if ("根目录" === parent.name)
            parent.children = treeData.filter(item => item.fileType === "directory" && item.split("/").length === deep + 1).map(item => {
                return buildTree(item, deep + 1)
            })
        else
            parent.children = treeData.filter(item => item.fileType === "directory" && item.fileName.startsWith(parent.fileName) && item.split("/").length === deep + 1).map(item => {
                return buildTree(item, deep + 1)
            })
        return parent;
    }

    layui.use(function () {
        let root = {
            title: '根节点',
            id: 1,
            children: []
        }

        buildTree(parent, deep)
        {

            if ("根目录" === parent.name) {
                parent.children = list.filter(item => item.split("/").length == deep + 1).map(item => buildTree(item, deep + 1)
            } else {
                parent.children = list.filter(item => item.startWith(parent.fileName) && item.split("/").length == deep + 1).map(item => buildTree(item, deep + 1)
            }

            return parent;
        }

        treeData.filter(item => item.fileType === 'directory');

        // 模拟数据
        var data = [{
            title: '江西',
            id: 1,
            children: [{
                title: '南昌',
                id: 1000,
                children: [{title: '青山湖区', id: 10001}, {title: '高新区', id: 10002}]
            }, {title: '九江', id: 1001}, {title: '赣州', id: 1002}]
        }, {title: '广西', id: 2, children: [{title: '南宁', id: 2000}, {title: '桂林', id: 2001}]}, {
            title: '陕西',
            id: 3,
            children: [{title: '西安', id: 3000}, {title: '延安', id: 3001}]
        }, {title: '山西', id: 3, children: [{title: '太原', id: 4000}, {title: '长治', id: 4001}]}];
        // 渲染
        tree.render({
            elem: '#compress-tree',
            data: data,
            showLine: false  // 是否开启连接线
        });
    });
</script>
</body>
</html>