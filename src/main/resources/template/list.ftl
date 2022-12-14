<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>文件信息列表</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/bootstrap/5.1.3/css/bootstrap.css"/>
    <script type="text/javascript" src="${contextPath}/bootstrap/5.1.3/js/bootstrap.bundle.js"></script>
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
                    <th scope="col">转换状态</th>
                    <th scope="col">转换开始时间</th>
                    <th scope="col">转换结束时间</th>
                    <th scope="col">上次预览时间</th>
                    <th scope="col">异常信息</th>
                    <th scope="col">预览文件</th>
                </tr>
                </thead>
                <tbody>
                <#if list?? && (list?size > 0)>
                    <#list list as row>
                        <tr>
                            <th scope="row">${row_index + 1}</th>
                            <td>${row.sourceFileName!'-'}</td>
                            <td>${row.fileName!'-'}</td>
                            <td>
                                <#switch row.convertStatus>
                                    <#case "00">
                                        未转换
                                        <#break>
                                    <#case "10">
                                        转换中
                                        <#break>
                                    <#case "20">
                                        转换完成
                                        <#break>
                                    <#case "30">
                                        转换失败
                                        <#break>
                                    <#default>
                                        -
                                </#switch>
                            </td>
                            <td>${row.convertStartTime!'-'}</td>
                            <td>${row.convertEndTime!'-'}</td>
                            <td>${row.prePreviewTime!'-'}</td>
                            <td>${row.errorMessage!'-'}</td>
                            <td><a href="${contextPath}/file/preview?id=${row.id}" class="link-primary">预览文件</a></td>
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