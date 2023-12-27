# file-preview-spring-boot-starter

[![](https://jitpack.io/v/wb04307201/file-preview-spring-boot-starter.svg)](https://jitpack.io/#wb04307201/file-preview-spring-boot-starter)
[![star](https://gitee.com/wb04307201/file-preview-spring-boot-starter/badge/star.svg?theme=dark)](https://gitee.com/wb04307201/file-preview-spring-boot-starter)
[![fork](https://gitee.com/wb04307201/file-preview-spring-boot-starter/badge/fork.svg?theme=dark)](https://gitee.com/wb04307201/file-preview-spring-boot-starter)
[![star](https://img.shields.io/github/stars/wb04307201/file-preview-spring-boot-starter)](https://github.com/wb04307201/file-preview-spring-boot-starter)
[![fork](https://img.shields.io/github/forks/wb04307201/file-preview-spring-boot-starter)](https://github.com/wb04307201/file-preview-spring-boot-starter)

> 一个文档在线预览的中间件  
> 可通过简单的配置即可集成到springboot中  
> 支持word、excel、ppt、pdf、图片、视频、音频、markdown、代码、网页、epub电子书、Xmind脑图、压缩文件、bpmn(业务流程管理和符号)、cmmn(案例管理模型和符号)、dmn(决策管理和符号)等格式文件的在线预览

## 代码示例
1. 使用[文档在线预览](https://gitee.com/wb04307201/file-preview-spring-boot-starter)、[多平台文件存储](https://gitee.com/wb04307201/file-storage-spring-boot-starter)、[实体SQL工具类](https://gitee.com/wb04307201/sql-util)实现的[文件预览Demo](https://gitee.com/wb04307201/file-preview-demo)
2. 使用[文档在线预览](https://gitee.com/wb04307201/file-preview-spring-boot-starter)、[多平台文件存储](https://gitee.com/wb04307201/file-storage-spring-boot-starter)、[实体SQL工具类](https://gitee.com/wb04307201/sql-util)实现的[文件预览VUE Demo](https://gitee.com/wb04307201/file-preview-vue)

## 第一步 增加 JitPack 仓库
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

## 第二步 引入jar
1.1.7版本后groupId更换为com.github.wb04307201  
1.2.0版本后升级到jdk 17 SpringBoot 3.2.0
```xml
<dependency>
    <groupId>com.github.wb04307201</groupId>
    <artifactId>file-preview-spring-boot-starter</artifactId>
    <version>1.2.2</version>
</dependency>
```

## 第三步 在启动类上加上`@EnableFilePreview`注解
```java
@EnableFilePreview
@SpringBootApplication
public class FilePreviewDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilePreviewDemoApplication.class, args);
    }

}
```

## 第四步 注入IFilePreviewService，并对文件进行转换
> 目的是将 word，ppt转换成pdf excel转换成html，并存储所有的预览文件
> 也可以只记录源文件的位置
```java
    @Autowired
    FilePreviewService filePreviewService;

    //预览文件转换
    FilePreviewInfo filePreviewInfo=filePreviewService.covert(file.getInputStream(),file.getOriginalFilename());
```

### 在针对word，excel，ppt文件的预览上支持5种模式
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
docker run --name onlyoffice -i -t -d -p 80:80 -e JWT_ENABLED=false onlyoffice/documentserver
#如果需要使用JWT
docker run --name onlyoffice -i -t -d -p 80:80 -e JWT_SECRET=my_jwt_secret onlyoffice/documentserver
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
        #secret: my_jwt_secret #如果启用JWT，需要在这里配置约定好的secret

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
> ![img_18.png](img_18.png)
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

## 第六步 预览文件信息
> 可通过第四步返回的文件信息中的id  
> 访问http://ip:port/file/preview?id=??进行文件预览  
> 如果配置了context-path,请在地址中同样添加  
> 使用了一些开源组件帮助预览文件内容  

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
| markdonw       | [Marked](https://marked.js.org/)                                                 | <img src="img.png" width="30%" height="30%">                                                                                                  |
| 代码             | [CodeMirror](https://codemirror.net/)                                            | <img src="img_1.png" width="30%" height="30%"><img src="img_10.png" width="30%" height="30%">                                                 |
| epub电子书        | [epub.js](https://github.com/futurepress/epub.js)                                | <img src="img_12.png" width="30%" height="30%">                                                                                               |
| xmid脑图         | [xmind-embed-viewer](https://github.com/xmindltd/xmind-embed-viewer)             | <img src="img_14.png" width="30%" height="30%">                                                                                               |
| 网页             | 直接渲染                                                                             | <img src="img_13.png" width="30%" height="30%">                                                                                               |
| 压缩文件           | [Apache Commons Compress](https://commons.apache.org/proper/commons-compress/)   | <img src="img_22.png" width="30%" height="30%">                                                                                               |
| bpmn           | [bpmn.io](https://bpmn.io/)                                                      | <img src="img_26.png" width="30%" height="30%">                                                                                               |
| cmmn           | [bpmn.io](https://bpmn.io/)                                                      | <img src="img_27.png" width="30%" height="30%">                                                                                               |
| dmn            | [bpmn.io](https://bpmn.io/)                                                      | <img src="img_28.png" width="30%" height="30%">                                                                                               |



## 其他1：内置界面

上传的文件可通过http://ip:端口/file/preview/list进行查看  
注意：如配置了context-path需要在地址中对应添加  
![img_9.png](img_9.png)

## 其他2：下载文件、删除文件

> 可通过第四步返回的文件信息中的id  
> 访问http://ip:port/file/preview/download?id=??进行文件下载  
> 访问http://ip:port/file/preview/delete?id=??进行文件删除  
> 如果配置了context-path,请在地址中同样添加  
> 也可以调用IFilePreviewService服务中方法自行处理下载和删除

```java
    //获取文件bytes
    FilePreviewInfo filePreviewInfo = filePreviewService.findById(id);
    byte[] bytes = filePreviewService.getBytes(filePreviewInfo);

    //删除预览文件
    Boolean result = filePreviewService.delete(filePreviewInfo.getId());  
```

## 其他3：实际使用中，可通过配置和实现文件预览记录接口方法将数据持久化到数据库中

```yaml
file:
  preview:
    file-preview-record: cn.wubo.file.preview.demo.H2FilePriviewRecordImpl
```

```java
@Component
public class H2FilePriviewRecordImpl implements IFilePreviewRecord {

    private static final String HISTORY = "file_preview_history";

    private static ConnectionPool connectionPool = new ConnectionPool(new ConnectionParam());

    @Override
    public FilePreviewInfo save(FilePreviewInfo filePreviewInfo) {
        try {
            Connection conn = connectionPool.getConnection();
            if (!StringUtils.hasLength(filePreviewInfo.getId())) {
                filePreviewInfo.setId(UUID.randomUUID().toString());
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.insertSql(HISTORY, filePreviewInfo), new HashMap<>());
            } else {
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.updateByIdSql(HISTORY, filePreviewInfo), new HashMap<>());
            }
            connectionPool.returnConnection(conn);
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return filePreviewInfo;
    }

    @Override
    public List<FilePreviewInfo> list(FilePreviewInfo filePreviewInfo) {
        try {
            Connection conn = connectionPool.getConnection();
            String sql = ModelSqlUtils.selectSql(HISTORY, new FileInfo());

            List<String> condition = new ArrayList<>();
            if (StringUtils.hasLength(filePreviewInfo.getId()))
                condition.add(" id  = '" + filePreviewInfo.getId() + "'");
            if (StringUtils.hasLength(filePreviewInfo.getFileName()))
                condition.add(" fileName  like '%" + filePreviewInfo.getFileName() + "%'");
            if (StringUtils.hasLength(filePreviewInfo.getOriginalFilename()))
                condition.add(" originalFilename like '%" + filePreviewInfo.getOriginalFilename() + "%'");
            if (StringUtils.hasLength(filePreviewInfo.getFilePath()))
                condition.add(" filePath like '%" + filePreviewInfo.getFilePath() + "%'");

            if (!condition.isEmpty()) sql = sql + " where " + String.join("and", condition);

            List<FilePreviewInfo> res = ExecuteSqlUtils.executeQuery(conn, sql, new HashMap<>(), FilePreviewInfo.class);
            connectionPool.returnConnection(conn);
            return res;
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FilePreviewInfo findById(String s) {
        FilePreviewInfo query = new FilePreviewInfo();
        query.setId(s);
        String sql = ModelSqlUtils.selectSql(HISTORY, query);
        try {
            Connection conn = connectionPool.getConnection();
            List<FilePreviewInfo> res = ExecuteSqlUtils.executeQuery(conn, sql, new HashMap<>(), FilePreviewInfo.class);
            connectionPool.returnConnection(conn);
            return res.get(0);
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean delete(FilePreviewInfo filePreviewInfo) {
        List<FilePreviewInfo> filePreviewInfos = list(filePreviewInfo);
        try {
            Connection conn = connectionPool.getConnection();
            conn.setAutoCommit(false);
            AtomicInteger count = new AtomicInteger();
            filePreviewInfos.stream().forEach(e -> {
                FilePreviewInfo delete = new FilePreviewInfo();
                delete.setId(e.getId());
                String sql = ModelSqlUtils.deleteByIdSql(HISTORY, delete);
                count.addAndGet(ExecuteSqlUtils.executeUpdate(conn, sql, new HashMap<>()));
            });
            conn.commit();
            connectionPool.returnConnection(conn);
            return count.get() > 0;
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        try {
            Connection conn = connectionPool.getConnection();
            if (!ExecuteSqlUtils.isTableExists(conn, HISTORY, DbType.h2)) {
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.createSql(HISTORY, new FilePreviewInfo()), new HashMap<>());
            }
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

## 其他4：实际使用中，可通过配置和实现文件存储记录接口方法将文件持久化到其他平台中

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

*注意：
文件存储这部分使用了[file-storage-spring-boot-starter](https://gitee.com/wb04307201/file-storage-spring-boot-starter)*

## 其他5：自定义预览界面渲染
在实际使用minio作为对象存储，想直接使用minio的url播放视频  
可通过集成IRenderPage并实现support和render方法的方式自定义页面渲染的方式
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


## 待办

- [ ] *查看压缩文件的列表时，可对其中的文件进行预览和下载*

