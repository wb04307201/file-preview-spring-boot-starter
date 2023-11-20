<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>文件信息列表</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/bootstrap/5.3.1/css/bootstrap.min.css"/>
    <script type="text/javascript" src="${contextPath}/bootstrap/5.3.1/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col">
            <table class="table table-bordered table-striped table-sm">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">原文件名</th>
                    <th scope="col">文件名</th>
                    <th scope="col">文件定位</th>
                    <th scope="col">删除</th>
                    <th scope="col">预览</th>
                    <th scope="col">下载</th>
                </tr>
                </thead>
                <tbody>
                <#if list?? && (list?size > 0)>
                    <#list list as row>
                        <tr>
                            <th scope="row">${row_index + 1}</th>
                            <td>${row.originalFilename!'-'}</td>
                            <td>${row.fileName!'-'}</td>
                            <td>${row.filePath!'-'}</td>
                            <td><a href="${contextPath}/file/preview/delete?id=${row.id}" class="link-primary">@删除</a></td>
                            <td><a href="${contextPath}/file/preview?id=${row.id}" rel="noopener" target="_blank" class="link-primary">@预览</a></td>
                            <td><a href="${contextPath}/file/preview/download?id=${row.id}" class="link-primary">@下载</a></td>
                        </tr>
                    </#list>
                </#if>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>