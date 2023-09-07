<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Compressed File</title>
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
</div>
</body>
</html>