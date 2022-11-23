<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Markdown</title>
    <script type="text/javascript" src="${contextPath}/marked/4.2.1/marked.min.js"></script>
    <script type="text/javascript" src="${contextPath}/js-base64/3.7.2/base64.min.js"></script>
    <script type="text/javascript" src="${contextPath}/highlight/11.6.0/js/highlight.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${contextPath}/highlight/11.6.0/css/gradient-light.min.css"/>
</head>
<body>
<div id="content"></div>
<script>
    marked.setOptions({
        renderer: new marked.Renderer(),
        highlight: function(code, lang) {
            var language = hljs.getLanguage(lang) ? lang : 'plaintext';
            return hljs.highlight(code, { language }).value;
        },
        langPrefix: 'hljs language-', // highlight.js css expects a top-level 'hljs' class.
        pedantic: false,
        gfm: true,
        breaks: false,
        sanitize: false,
        smartypants: false,
        xhtml: false
    });
    document.getElementById('content').innerHTML = marked.parse(Base64.decode("${content}"));
</script>
</body>
</html>