# file-preview-spring-boot-starter

[![](https://jitpack.io/v/wb04307201/file-preview-spring-boot-starter.svg)](https://jitpack.io/#wb04307201/file-preview-spring-boot-starter)
[![star](https://gitee.com/wb04307201/file-preview-spring-boot-starter/badge/star.svg?theme=dark)](https://gitee.com/wb04307201/file-preview-spring-boot-starter)
[![fork](https://gitee.com/wb04307201/file-preview-spring-boot-starter/badge/fork.svg?theme=dark)](https://gitee.com/wb04307201/file-preview-spring-boot-starter)
[![star](https://img.shields.io/github/stars/wb04307201/file-preview-spring-boot-starter)](https://github.com/wb04307201/file-preview-spring-boot-starter)
[![fork](https://img.shields.io/github/forks/wb04307201/file-preview-spring-boot-starter)](https://github.com/wb04307201/file-preview-spring-boot-starter)  
![MIT](https://img.shields.io/badge/License-Apache2.0-blue.svg) ![JDK](https://img.shields.io/badge/JDK-17+-green.svg) ![SpringBoot](https://img.shields.io/badge/Srping%20Boot-3+-green.svg)

## 简介
> 一个文档在线预览的中间件  
> 可通过简单的配置即可集成到springboot中  
> 支持word、excel、ppt、pdf、ofd、图片、视频、音频、markdown、代码、网页、epub图书文档、Xmind脑图、压缩文件、bpmn业务流程、cmmn案例管理、dmn决策、3D模型等格式文件的在线预览

## 支持的文件类型以及相关的的开源项目
| 文件类型           | 预览组件                                                                             | 预览示例                                                                                                                                          |
|----------------|----------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| word/excel/ppt | [jodconverter](https://github.com/sbraconnier/jodconverter/)                     |                                                                                                                                               |
| word/excel/ppt | [Spire.Office](https://www.e-iceblue.com/)                                       | <img src="img_20.png" width="30%" height="30%"><img src="img_19.png" width="30%" height="30%"><img src="img_21.png" width="30%" height="30%"> |
| word/excel/ppt | [onlyoffice](https://www.onlyoffice.com/zh/)                                     | <img src="img_2.png" width="30%" height="30%"><img src="img_6.png" width="30%" height="30%"><img src="img_8.png" width="30%" height="30%">    |
| word/excel/ppt | [libreoffice online](https://zh-cn.libreoffice.org/download/libreoffice-online/) | <img src="img_16.png" width="30%" height="30%"><img src="img_15.png" width="30%" height="30%"><img src="img_17.png" width="30%" height="30%"> |
| word/excel/ppt | [Collabora Online](https://www.collaboraoffice.com/)                             | <img src="img_24.png" width="30%" height="30%"><img src="img_23.png" width="30%" height="30%"><img src="img_25.png" width="30%" height="30%"> |
| pdf            | [PDF.js](https://mozilla.github.io/pdf.js/)                                      | <img src="img_11.png" width="30%" height="30%">                                                                                               |
| audio音频        | [audio.js](http://kolber.github.io/audiojs/)                                     | <img src="img_4.png" width="30%" height="30%">                                                                                                |
| video视频        | [videojs](https://videojs.com/)                                                  | <img src="img_5.png" width="30%" height="30%">                                                                                                |
| markdonw       | [vditor](https://github.com/Vanessa219/vditor)                                   | <img src="img.png" width="30%" height="30%">                                                                                                  |
| 代码             | [CodeMirror](https://codemirror.net/)                                            | <img src="img_1.png" width="30%" height="30%"><img src="img_10.png" width="30%" height="30%">                                                 |
| epub电子书        | [epub.js](https://github.com/futurepress/epub.js)                                | <img src="img_12.png" width="30%" height="30%">                                                                                               |
| xmid脑图         | [xmind-embed-viewer](https://github.com/xmindltd/xmind-embed-viewer)             | <img src="img_14.png" width="30%" height="30%">                                                                                               |
| 网页             | 直接渲染                                                                             | <img src="img_13.png" width="30%" height="30%">                                                                                               |
| 压缩文件           | [Apache Commons Compress](https://commons.apache.org/proper/commons-compress/)   | <img src="img_22.png" width="30%" height="30%">                                                                                               |
| bpmn           | [bpmn.io](https://bpmn.io/)                                                      | <img src="img_26.png" width="30%" height="30%">                                                                                               |
| cmmn           | [bpmn.io](https://bpmn.io/)                                                      | <img src="img_27.png" width="30%" height="30%">                                                                                               |
| dmn            | [bpmn.io](https://bpmn.io/)                                                      | <img src="img_28.png" width="30%" height="30%">                                                                                               |
| ofd            | [ofd.js](https://gitee.com/Donal/ofd.js)                                         | <img src="img_30.png" width="30%" height="30%">                                                                                               |
| 3D模型           | [Online 3D Viewer](https://gitee.com/Donal/ofd.js)                               | <img src="img_31.png" width="30%" height="30%">                                                                                               |

## 代码示例
1. 使用[文档在线预览](https://gitee.com/wb04307201/file-preview-spring-boot-starter)、[多平台文件存储](https://gitee.com/wb04307201/file-storage-spring-boot-starter)、[实体SQL工具](https://gitee.com/wb04307201/sql-util)实现的[文件预览Demo](https://gitee.com/wb04307201/file-preview-demo)
2. 使用[文档在线预览](https://gitee.com/wb04307201/file-preview-spring-boot-starter)、[多平台文件存储](https://gitee.com/wb04307201/file-storage-spring-boot-starter)、[实体SQL工具](https://gitee.com/wb04307201/sql-util)实现的[文件预览VUE Demo](https://gitee.com/wb04307201/file-preview-vue)

## 快速开始
### 引入依赖
增加 JitPack 仓库
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

1.1.7版本后groupId更换为com.github.wb04307201  
1.2.0版本后升级到jdk17 SpringBoot3+  
继续使用jdk 8请查看jdk8分支
```xml
<dependency>
    <groupId>com.github.wb04307201</groupId>
    <artifactId>file-preview-spring-boot-starter</artifactId>
    <version>1.2.10</version>
</dependency>
```

### 在启动类上加上`@EnableFilePreview`注解
```java
@EnableFilePreview
@SpringBootApplication
public class FilePreviewDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilePreviewDemoApplication.class, args);
    }

}
```

### 添加相关配置
```yaml
file:
  preview:
    enableWeb: true  # 默认为true，加载内置页面
    enableRest: true  # 默认为true, 加载内置接口
```
#### 配置word，excel，ppt文件的预览方式，支持5种模式
#### jodconverter
> 安装[libroffice](https://zh-cn.libreoffice.org/)并添加配置  
> 详细配置内容请查看[jodconverter](https://github.com/sbraconnier/jodconverter/)
```yaml
file:
  preview:
    # 使用jodconverter模式，可省略该配置
    officeConverter: jod
jodconverter:
  local:
    enabled: true
    # libreOffice根目录
    officeHome: C:\Program Files\LibreOffice
    # 任务执行的超时时间
    taskExecutionTimeout: 86400000
    # 任务队列的超时时间
    taskQueueTimeout: 86400000
    # 端口（线程）
    portNumbers: [ 2001,2002,2003 ]
    # 一个进程的超时时间
    processTimeout: 86400000
```

#### spire.office
> 项目中使用的spire.office为免费版本，转换office文件存在一定限制
> 如要使用收费版本，请排除免费版本的依赖并添加正式版本  
> [Spire.Office](https://www.e-iceblue.com/)
```yaml
file:
  preview:
    # 使用spire.office模式
    officeConverter: spire
```

#### onlyoffice
> 使用[onlyoffice](https://www.onlyoffice.com/zh/)将不对office文件进行转换    
> 直接预览word,excel,ppt,文本类型的文件  
> 可以通过docker快速安装onlyoffice，命令如下
```bash
docker run --name onlyoffice -i -t -d -p 80:80 -e JWT_SECRET=my_jwt_secret onlyoffice/documentserver
#如果需要关闭使用JWT
docker run --name onlyoffice -i -t -d -p 80:80 -e JWT_ENABLED=false onlyoffice/documentserver
```
可参考官方文档  
[Installing ONLYOFFICE Docs Community Edition for Docker on a local server](https://helpcenter.onlyoffice.com/installation/docs-community-install-docker.aspx)  
[Configuring JWT for ONLYOFFICE Docs](https://helpcenter.onlyoffice.com/installation/docs-configure-jwt.aspx)
> 容器启动成功后，打开http://127.0.0.1/可以看到欢迎页面
> 如果需要使用onlyoffice自带的测试页面，可以找页面中如下部分，并分别在终端中执行1，2的命令，然后点击按钮3    
> ![img_3.png](img_3.png)   
> ![img_7.png](img_7.png)  
> docker版本的onlyoffice安装成功后，在项目中添加配置信息
```yaml
file:
  preview:
    # 使用onlyoffice模式
    officeConverter: only
  online:
    preview:
      onlyoffice:
        domain: http://ip:port #OnlyOffice服务所在域
        download: http://ip:port/file/preview/download #当前服务的文件下载接口，用于onlyoffice从当前服务下载文件
        callback: http://ip:port/file/preview/onlyoffice/callback #当前服务的回写文件服务，用于onlyoffice回写文件到当前服务
        secret: my_jwt_secret #如果启用JWT，需要在这里配置约定好的secret
```

#### LibreOffice Online
> 使用[libreoffice online](https://zh-cn.libreoffice.org/download/libreoffice-online/)将不对office文件进行转换    
> 直接预览word,excel,ppt,文本类型的文件  
> 可以通过docker快速安装libreoffice online，命令如下
```bash
#安装并启动docker版本lool
docker run --name lool -e "username=admin" -e "password=123456" -e "extra_params=--o:ssl.enable=false --o:storage.filesystem[@allow]=true" -v D:/lool:/srv/data:Z -p 9980:9980 -d libreoffice/online:master

# extra_params=--o:ssl.enable=false 关闭ssl
# --o:storage.filesystem[@allow]=true 允许读取本地文件
```
> 容器启动成功后，打开http://127.0.0.1:9980/loleaflet/dist/admin/admin.html可以看到控制台界面
> ![img_18.png](img_18.png)
> docker版本的lool安装成功后，在项目中添加配置信息
```yaml
file:
  preview:
    # 使用LibreOffice Online模式
    officeConverter: lool
    libreoffice:
      domain: http://ip:port  #libreoffice online服务所在地址
      download: D:\lool #libreoffice online预览文件存储位置，需要和上面docker创建的容器映射到本地的目录一致
```

#### Collabora Online
> 使用[Collabora Online](https://www.collaboraoffice.com/)将不对office文件进行转换    
> 直接预览word,excel,ppt,文本类型的文件  
> 可以通过docker快速安装Collabora Online，命令如下
```bash
docker run -t -d --name code -e "username=admin" -e "password=123456" -e "aliasgroup1=http://10.133.61.38:8090" -e "extra_params=--o:ssl.enable=false" -p 9980:9980 collabora/code

# extra_params=--o:ssl.enable=false 关闭ssl
# aliasgroup1=http://10.133.61.38:8090 配置允许wopi访问地址
```
> 容器启动成功后，打开http://127.0.0.1:9980/browser/dist/admin/admin.html可以看到控制台界面
> ![img_29.png](img_29.png)
> docker版本的cool安装成功后，在项目中添加配置信息
```yaml
file:
  preview:
    # 使用Collabora Online模式
    officeConverter: cool
    collabora:
      domain: http://ip:port  #Collabora Online服务所在地址
      storageDomain: http://ip:port #当前服务的域名，用于Collabora Online从当前服务下载文件
```

### 访问内置界面使用文件上传
上传的文件可通过http://ip:端口/file/preview/list进行查看  
注意：如配置了context-path需要在地址中对应添加  
![img_9.png](img_9.png)

## 高级
### 如何通过配置和实现文件预览记录接口方法将数据持久化到数据库中
```yaml
file:
  preview:
    file-preview-record: cn.wubo.file.preview.demo.H2FilePriviewRecordImpl
```

```java
@Component
public class H2FilePriviewRecordImpl implements IFilePreviewRecord {

    static {
        MutilConnectionPool.init("main", "jdbc:h2:file:./data/demo;AUTO_SERVER=TRUE", "sa", "");
    }

    @Override
    public FilePreviewInfo save(FilePreviewInfo filePreviewInfo) {
        FilePreviewRecord filePreviewRecord = FilePreviewRecord.trans(filePreviewInfo);
        if (!StringUtils.hasLength(filePreviewRecord.getId())) {
            filePreviewRecord.setId(UUID.randomUUID().toString());
            MutilConnectionPool.run("main", conn -> ModelSqlUtils.insertSql(filePreviewRecord).executeUpdate(conn));
        } else MutilConnectionPool.run("main", conn -> ModelSqlUtils.updateSql(filePreviewRecord).executeUpdate(conn));
        return filePreviewRecord.getFilePreviewInfo();
    }

    @Override
    public List<FilePreviewInfo> list(FilePreviewInfo filePreviewInfo) {
        FilePreviewRecord filePreviewRecord = FilePreviewRecord.trans(filePreviewInfo);
        return MutilConnectionPool.run("main", conn -> ModelSqlUtils.selectSql(filePreviewRecord).executeQuery(conn)).stream().map(FilePreviewRecord::getFilePreviewInfo).collect(Collectors.toList());
    }

    @Override
    public FilePreviewInfo findById(String s) {
        FilePreviewInfo query = new FilePreviewInfo();
        query.setId(s);
        List<FilePreviewInfo> list = list(query);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public Boolean delete(FilePreviewInfo filePreviewInfo) {
        FilePreviewRecord filePreviewRecord = FilePreviewRecord.trans(filePreviewInfo);
        return MutilConnectionPool.run("main", conn -> ModelSqlUtils.deleteSql(filePreviewRecord).executeUpdate(conn)) > 0;
    }

    @Override
    public void init() {
        if (Boolean.FALSE.equals(MutilConnectionPool.run("main", conn -> new SQL<FilePreviewRecord>() {
        }.isTableExists(conn)))) MutilConnectionPool.run("main", conn -> new SQL<FilePreviewRecord>() {
        }.create().parse().createTable(conn));
    }
}
```

### 如何通过配置和实现文件存储记录接口方法将文件持久化到其他平台中
```yaml
file:
  preview:
    file-storage: cn.wubo.file.preview.demo.MinIOFileStorageImpl

```

```java
@Component
public class MinIOFileStorageImpl implements IFileStorage {

    @Resource
    FileStorageService fileStorageService;

    @Override
    public FilePreviewInfo save(byte[] bytes, String fileName) {
        FileInfo fileInfo = fileStorageService.save(new MultipartFileStorage(fileName, bytes).setAlias("minio-1").setPath("preview"));
        FilePreviewInfo filePreviewInfo = new FilePreviewInfo();
        filePreviewInfo.setFileName(fileInfo.getOriginalFilename());
        filePreviewInfo.setFilePath(fileInfo.getId());
        return filePreviewInfo;
    }

    @Override
    public Boolean delete(FilePreviewInfo filePreviewInfo) {
        return fileStorageService.delete(filePreviewInfo.getFilePath());
    }

    @Override
    public byte[] getBytes(FilePreviewInfo filePreviewInfo) {
        MultipartFileStorage file = fileStorageService.download(filePreviewInfo.getFilePath());
        return file.getBytes();
    }

    @Override
    public void init() {

    }
}
```
*注意： 文件存储这部分使用了[file-storage-spring-boot-starter](https://gitee.com/wb04307201/file-storage-spring-boot-starter)*

### 如何通过内置Rest接口实现自定义页面
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <title>预览文件记录</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" type="text/css" href="/layui/2.9.6/css/layui.css"/>
    <script type="text/javascript" src="/layui/2.9.6/layui.js"></script>
    <style>
        body {
            padding: 10px 20px 10px 20px;
        }
    </style>
</head>
<body>
<form class="layui-form layui-row layui-col-space16">
    <div class="layui-col-md4">
        <div class="layui-form-item">
            <label class="layui-form-label">文件名</label>
            <div class="layui-input-block">
                <input type="text" name="fileName" placeholder="请输入" class="layui-input" lay-affix="clear">
            </div>
        </div>
    </div>
    <div class="layui-col-md4">
        <div class="layui-form-item">
            <label class="layui-form-label">原始文件名</label>
            <div class="layui-input-block">
                <input type="text" name="originalFilename" placeholder="请输入" class="layui-input" lay-affix="clear">
            </div>
        </div>
    </div>
    <div class="layui-col-md4">
        <div class="layui-form-item">
            <label class="layui-form-label">文件位置</label>
            <div class="layui-input-block">
                <input type="text" name="filePath" placeholder="请输入" class="layui-input" lay-affix="clear">
            </div>
        </div>
    </div>
    <div class="layui-btn-container layui-col-xs12">
        <button class="layui-btn" lay-submit lay-filter="table-search">查询</button>
        <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
</form>
<!-- 拖拽上传 -->
<div class="layui-upload-drag" style="display: block;" id="ID-upload-demo-drag">
    <i class="layui-icon layui-icon-upload"></i>
    <div>点击上传，或将文件拖拽到此处</div>
    <div class="layui-hide" id="ID-upload-demo-preview">
        <hr>
        <img src="" alt="上传成功后渲染" style="max-width: 100%">
    </div>
</div>
<!-- 原始容器 -->
<table class="layui-hide" id="table"></table>
<!-- 操作列 -->
<script type="text/html" id="table-templet-operator">
    <div class="layui-clear-space">
        <a class="layui-btn layui-btn-xs" lay-event="delete">删除</a>
        <a class="layui-btn layui-btn-xs" lay-event="preview">预览</a>
        <a class="layui-btn layui-btn-xs" lay-event="download">下载</a>
    </div>
</script>
<script>
    layui.use(['table', 'form', 'util'], function () {
        let table = layui.table, form = layui.form, layer = layui.layer, $ = layui.$, laydate = layui.laydate,
            upload = layui.upload;

        // 搜索提交
        form.on('submit(table-search)', function (data) {
            let field = data.field; // 获得表单字段
            // 执行搜索重载
            table.reloadData('table', {
                where: field // 搜索的字段
            });
            return false; // 阻止默认 form 跳转
        });

        // 渲染
        upload.render({
            elem: '#ID-upload-demo-drag', // 绑定多个元素
            url: '/file/preview/upload', // 此处配置你自己的上传接口即可
            accept: 'file', // 普通文件
            done: function (res) {
                if (res.code === 200)
                    table.reloadData('table', {});
                layer.msg(res.message);
            }
        });

        table.render({
            elem: '#table',
            cols: [[ //标题栏
                {type: 'numbers', fixed: 'left'},
                {field: 'id', title: 'ID', width: 150, fixed: 'left', hide: true},
                {field: 'fileName', title: '文件名', width: 300},
                {field: 'originalFilename', title: '原始文件名', width: 300},
                {field: 'filePath', title: '文件位置', width: 300},
                {field: 'operator', title: '操作', width: 200, fixed: 'right', templet: '#table-templet-operator'},
            ]],
            url: '/file/preview/list',
            method: 'post',
            contentType: 'application/json',
            parseData: function (res) { // res 即为原始返回的数据
                return {
                    "code": res.code === 200 ? 0 : res.code, // 解析接口状态
                    "msg": res.message, // 解析提示文本
                    "count": res.data.length, // 解析数据长度
                    "data": res.data // 解析数据列表
                };
            },
        });

        // 操作列事件
        table.on('tool(table)', function (obj) {
            let data = obj.data; // 获得当前行数据
            switch (obj.event) {
                case 'delete':
                    deleteRow(data.id)
                    break;
                case 'preview':
                    previewRow(data.id)
                    break;
                case 'download':
                    downloadRow(data.id)
                    break;
            }
        })

        function deleteRow(id) {
            layer.confirm('确定要删除么？', {icon: 3}, function (index, layero, that) {
                fetch("/file/preview/delete?id=" + id)
                    .then(response => response.json())
                    .then(res => {
                        if (res.code === 200)
                            table.reloadData('table', {});
                        layer.close(index);
                        layer.msg(res.message);
                    })
                    .catch(err => {
                        layer.msg(err)
                        layer.close(index);
                    })
            }, function (index, layero, that) {
            });
        }

        function previewRow(id) {
            window.open("/file/preview?id=" + id);
        }

        function downloadRow(id) {
            window.open("/file/preview/download?id=" + id);
        }
    })
</script>
</body>
</html>
```

### 如何通过注入FileStorageService实现自定义Rest接口和自定义页面
```java
@Controller
public class Demo2Controller {

    @Autowired
    FilePreviewService filePreviewService;

    /**
     * 处理demo2请求
     *
     * @param model           模型对象
     * @param filePreviewInfo 文件预览信息
     * @return 返回demo2页面
     */
    @PostMapping(value = "/demo2")
    public String demo2(Model model, FilePreviewInfo filePreviewInfo) {
        // 获取文件存储记录列表
        model.addAttribute("list", filePreviewService.list(filePreviewInfo));
        // 设置查询参数
        model.addAttribute("query", filePreviewInfo);
        return "demo2";
    }

    /**
     * 上传文件
     *
     * @param file  上传的文件
     * @param model 模型对象
     * @return 返回上传结果页面
     */
    @PostMapping(value = "/upload")
    public String upload(MultipartFile file, Model model) {
        try {
            filePreviewService.covert(file.getInputStream(), file.getOriginalFilename());
            FilePreviewInfo filePreviewInfo = new FilePreviewInfo();
            model.addAttribute("list", filePreviewService.list(filePreviewInfo));
            model.addAttribute("query", filePreviewInfo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "demo2";
    }

    /**
     * 获取文件列表
     *
     * @param model 模型对象
     * @return 返回页面名称
     */
    @GetMapping(value = "/demo2")
    public String demo2(Model model) {
        FilePreviewInfo filePreviewInfo = new FilePreviewInfo();
        List<FilePreviewInfo> list = filePreviewService.list(filePreviewInfo);
        model.addAttribute("list", list);
        model.addAttribute("query", filePreviewInfo);
        return "demo2";
    }

    /**
     * 删除文件
     *
     * @param req   请求对象
     * @param model 模型对象
     * @return 返回页面名称
     */
    @GetMapping(value = "/delete")
    public String delete(HttpServletRequest req, Model model) {
        String id = req.getParameter("id");
        filePreviewService.delete(id);
        FilePreviewInfo filePreviewInfo = new FilePreviewInfo();
        List<FilePreviewInfo> list = filePreviewService.list(filePreviewInfo);
        model.addAttribute("list", list);
        model.addAttribute("query", filePreviewInfo);
        return "demo2";
    }

    /**
     * 下载文件
     *
     * @param req  请求对象
     * @param resp 响应对象
     */
    @GetMapping(value = "/download")
    public void download(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        FilePreviewInfo filePreviewInfo = filePreviewService.findById(id);
        byte[] bytes = filePreviewService.getBytes(filePreviewInfo);
        resp.reset();
        try {
            resp.setContentType(FileUtils.getMimeType(filePreviewInfo.getFileName()));
            try (OutputStream os = resp.getOutputStream()) {
                resp.addHeader("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(filePreviewInfo.getOriginalFilename()).getBytes(), StandardCharsets.ISO_8859_1));
                IoUtils.writeToStream(bytes, os);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
```
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>上传文件</title>
    <link rel="stylesheet" type="text/css" href="/bootstrap/5.3.2/css/bootstrap.min.css"/>
    <script type="text/javascript" src="/bootstrap/5.3.2/js/bootstrap.bundle.min.js"></script>
    <style>
        .table tbody tr td {
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="row">
        <div class="col">
            <form method="post" enctype="multipart/form-data" action="/upload">
                <div class="mb-3">
                    <label for="fileInput" class="form-label">文件上传</label>
                    <input type="file" class="form-control" id="fileInput" aria-describedby="fileHelp" name="file">
                    <div id="fileHelp" class="form-text">支持word，excel，ppt，pdf，图片，视频，音频，markdown等格式文件的在线预览</div>
                </div>
                <button type="submit" class="btn btn-primary">提交</button>
            </form>
        </div>
    </div>
    <form class="row g-3 mb-3 mt-3" method="POST" action="/demo2">
        <div class="col-6">
            <label class="form-check-label" for="fileName">文件名</label>
            <input type="text" class="form-control" id="fileName" name="fileName" aria-describedby="文件名"
                   value="${(query.fileName)!''}">
        </div>
        <div class="col-6">
            <label class="form-check-label" for="originalFilename">原始文件名</label>
            <input type="text" class="form-control" id="originalFilename" name="originalFilename" aria-describedby="原始文件名"
                   value="${(query.originalFilename)!''}">
        </div>
        <div class="col-6">
            <label class="form-check-label" for="filePath">文件位置</label>
            <input type="text" class="form-control" id="filePath" name="filePath" aria-describedby="文件位置"
                   value="${(query.filePath)!''}">
        </div>
        <div class="col-12">
            <button type="submit" class="btn btn-primary">查询</button>
        </div>
    </form>
    <div class="row">
        <div class="col-12" style="overflow-x: auto">
            <table class="table table-striped table-border">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">文件名</th>
                    <th scope="col">原始文件名</th>
                    <th scope="col">文件位置</th>
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
                            <td><a href="/delete?id=${row.id}" class="link-primary">@删除</a></td>
                            <td><a href="/file/preview?id=${row.id}" rel="noopener" target="_blank" class="link-primary">@预览</a></td>
                            <td><a href="/download?id=${row.id}" class="link-primary">@下载</a></td>
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
```

### 如何自定义文件预览方式
比如在实际使用minio作为对象存储，并想直接使用minio的url播放上传的视频  
可通过继承IRenderPage并实现support和render方法的方式自定义页面渲染的方式
```java
@Service
public class MinIORenderPage implements IRenderPage {
    @Override
    public Boolean support(FilePreviewService filePreviewService, FilePreviewInfo filePreviewInfo) {
        if ("life goes on.mp4".equals(filePreviewInfo.getOriginalFilename())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public ServerResponse render(FilePreviewService filePreviewService, FilePreviewInfo filePreviewInfo) {
        try {
            return ServerResponse.permanentRedirect(new URI("http://127.0.0.1:9000/testfilestorage/temp/preview/50236952-e8c5-4e67-afe9-ff39e3eac8ca.mp4?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=MJC7FWTK56VPHS6SUZQL%2F20231106%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20231106T004949Z&X-Amz-Expires=604800&X-Amz-Security-Token=eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJhY2Nlc3NLZXkiOiJNSkM3RldUSzU2VlBIUzZTVVpRTCIsImV4cCI6MTY5OTI3NDk3NiwicGFyZW50IjoiUk9PVFVTRVIifQ.DHAPkWUuPpy7-EVcQOh9VN6FOIbtsZiIX5THR3n7ds72zRpn9EY23BdCqf1wBYwjOel9a8IHF3qi-6z0PAAC0g&X-Amz-SignedHeaders=host&versionId=null&X-Amz-Signature=acf50009a980667d5084236bcb4993a42a57b2419b0ec1955f5eef8f2e7f982c")).build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
```

