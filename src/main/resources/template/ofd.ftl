<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <meta charset="UTF-8">
    <title>Audio</title>
    <script type="text/javascript" src="${contextPath}/file/preview/static/ofd.js/0.2.6/ofd.umd.js"></script>
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
<div id="ofdContainer" style="width:100%;height:100%;"></div>
<script>
    // 使用fetch获取文件
    fetch('${url}')
        .then(response => {
            // 检查响应状态是否为200（OK）
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            // 根据需要处理文件内容：
            // 如果是文本文件，使用response.text()
            // 如果是二进制文件，如图片、PDF，使用response.arrayBuffer() 或 response.blob()
            return response.arrayBuffer();
        })
        .then(arrayBuffer => {
            ofd.parseOfdDocument({
                ofd: arrayBuffer,
                success: function (res) {
                    var ofdRenderRes = ofd.renderOfd(window.innerWidth, res[0]);
                    var ofdContainerDiv = document.getElementById('ofdContainer');
                    // 清空元素
                    ofdContainerDiv.innerHTML = '';
                    for(var i = 0; i < ofdRenderRes.length; i++){
                        ofdContainerDiv.appendChild(ofdRenderRes[i]);
                    }
                },
                fail: function(err){
                    console.error('ofd文件渲染失败',err);
                }
            });
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
</script>
</body>
</html>