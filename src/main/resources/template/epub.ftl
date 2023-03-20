<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Audio</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/bootstrap/5.1.3/css/bootstrap.min.css"/>
    <script type="text/javascript" src="${contextPath}/bootstrap/5.1.3/js/bootstrap.bundle.js"></script>
    <script type="text/javascript" src="${contextPath}/jszip/3.10.1/jszip.min.js"></script>
    <script type="text/javascript" src="${contextPath}/epub.js/0.3.88/epub.min.js"></script>
    <style>
        html{
            height: 100%;
            overflow-y: hidden;
        }
        body, .container-fluid, .row, .col-3, .col-9, #area {
            height: 100%;
        }
        .list-group{
            height: 100%;
            overflow-y: auto;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col-3">
            <ul class="list-group list-group-flush">
            </ul>
        </div>
        <div class="col-9">
            <div id="area"></div>
        </div>
    </div>
</div>
<div id="area"></div>
<script>
    var book = ePub("${url}", {openAs: "epub"});
    var rendition = book.renderTo("area", {method: "continuous", flow: "scrolled", width: "100%", height: "100%"});
    var displayed = rendition.display();

    book.loaded.navigation.then(function (toc) {
        /*// 方式一 toc是一个多维数组，下面这种只能显示第一级的目录
        var catalogItme = '';
        toc.forEach(function(chapter) {
            catalogItme += '<p class="catalog-itme" data-catalog="'+ chapter.href +'">'+ chapter.label +'</p>'
        });
        // 将拼接好的目录渲染到页面里
        document.querySelector('catalog').innerHTML = catalogItme*/

        // 方式二 将所有的目录全部显示出来
        // 第一级的catalog-itme-0
        // 第二级的catalog-itme-1 以此类推...
        document.querySelector('.list-group').innerHTML = recursionHandle(toc.toc, [], 0).join('')
    })

    function recursionHandle(toc, doc, i) {
        toc.forEach(function (chapter) {
            // doc.push('<p class="catalog-itme catalog-itme-'+ i +'" data-catalog="'+ chapter.href +'">'+ chapter.label +'</p>')
            doc.push('<button type="button" class="list-group-item list-group-item-action" onclick="renditionTo(\'' + chapter.href + '\')">' + chapter.label + '</button>')
            if (chapter.subitems && chapter.subitems.length) {
                i++
                recursionHandle(chapter.subitems, doc, i)
                i > 0 && i--
            }
        })

        return doc
    }

    // 点击跳转
    function renditionTo(url) {
        rendition.display(url)
    }
</script>
</body>
</html>