# file-preview-spring-boot-starter

[![](https://jitpack.io/v/com.gitee.wb04307201/file-preview-spring-boot-starter.svg)](https://jitpack.io/#com.gitee.wb04307201/file-preview-spring-boot-starter)

> 文档在线预览快速启动器  
> 只需要简单的配置和编码，即可集成到springboot中  
> 支持word，excel，ppt，pdf，图片，视频，音频，markdown等格式文件的在线预览

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
```xml
    <dependency>
        <groupId>com.gitee.wb04307201</groupId>
        <artifactId>chatbot-spring-boot-starter</artifactId>
        <version>Tag</version>
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
    IFilePreviewService filePreviewService;

    //存储预览文件
    filePreviewService.covert(uploadFile.getAbsolutePath());

    //存储预览文件,预览时再存储预览文件
    filePreviewService.record(uploadFile.getAbsolutePath());
```
### 在针对word，excel，ppt文件的处理上，支持3种模式
#### jodconverter 使用注解@EnableFilePreview或者@EnableFilePreview(convert="jod")
> 安装[libroffice](https://zh-cn.libreoffice.org/)并添加配置  
> 详细配置内容请查看[jodconverter](https://github.com/sbraconnier/jodconverter/)
```yml
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
    portNumbers: [2001,2002,2003]
    # 一个进程的超时时间
    processTimeout: 86400000
```
#### spire.office 使用注解@EnableFilePreview(convert="spire")
> 项目中使用的spire.office为免费版本，转换office文件存在一定限制
> 如要使用收费版本，请排除免费版本的依赖并添加正式版本  
> [Spire.Office](https://www.e-iceblue.com/)
#### onlyoffice 使用注解@EnableFilePreview(convert="onlyoffice")
> 使用[onlyoffice](https://www.onlyoffice.com/zh/)将不对office文件进行转换    
> 并使用onlyoffice预览office文件以及pdf，txt等类型的文件  
> 可以通过docker快速安装onlyoffice，命令如下

```commandline
docker run -i -t -d -p 80:80 -e JWT_ENABLED=false onlyoffice/documentserver
```

> 容器启动成功后，打开http://127.0.0.1/可以看到欢迎页面
> 如果需要使用onlyoffice自带的测试页面，可以找页面中如下部分，并分别在终端中执行1，2的命令，然后点击按钮3    
> ![img_3.png](img_3.png)   
> ![img_7.png](img_7.png)  
> docker版本的onlyoffice安装成功后，在项目中添加配置信息
```yml
file:
  online:
    preview:
      onlyoffice:
        apijs: http://127.0.0.1/web-apps/apps/api/documents/api.js
        download: http://当前机器ip:当前机器port/file/preview/download
        callback: http://当前机器ip:当前机器port/file/preview/onlyoffice/callback
```
## 第五步 存储预览文件信息
> 默认使用h2数据库存储预览文件信息，如有需要可以扩展其他存储方式，不想修改可以跳过这一步  
> 新建一个类并引用接口cn.wubo.file.preview.storage.IStorageService并实现接口方法，例如
```java
package cn.wubo.file.preview.demo;

import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.storage.IStorageService;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MemoryStorageServiceImpl implements IStorageService {

    private static List<ConvertInfoDto> convertInfoDtoList;

    @Override
    public ConvertInfoDto save(ConvertInfoDto convertInfoDto) {
        if (!StringUtils.hasLength(convertInfoDto.getId())) {
            convertInfoDto.setId(UUID.randomUUID().toString());
            convertInfoDtoList.add(convertInfoDto);
        } else {
            convertInfoDtoList.stream().filter(e -> e.getId().equals(convertInfoDto.getId())).findAny().ifPresent(e -> e = convertInfoDto);
        }
        return convertInfoDto;
    }

    @Override
    public List<ConvertInfoDto> list(ConvertInfoDto convertInfoDto) {
        return convertInfoDtoList;
    }

    @Override
    public void init() {
        convertInfoDtoList = new ArrayList<>();
    }
}
```
> 修改`@EnableFilePreview`注解，增加storage参数只想新存储类的路径，例如
```java
@EnableFilePreview(convert = "onlyoffice", storage = "cn.wubo.file.preview.demo.MemoryStorageServiceImpl")
@SpringBootApplication
public class FilePreviewDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilePreviewDemoApplication.class, args);
    }

}
```

#### ! *可能会扩展对压缩文件的支持*
#### ! *可能会扩展[document4j](https://github.com/documents4j/documents4j)，但其不支持ppt，需要其他方案补充*

## 第六步 预览文件
> 通过http://127.0.0.1:8080/file/preview?id=?预览文件  
> 通过http://127.0.0.1:8080/file/preview/list查看历史记录  
> 如果使用`spring.servlet.context-path`,请在地址中同样增加context-path值  
> 截图为使用onlyoffice进行预览

![img.png](img.png)
![img_1.png](img_1.png)
![img_2.png](img_2.png)
![img_4.png](img_4.png)
![img_5.png](img_5.png)
![img_6.png](img_6.png)



### 示例
https://gitee.com/wb04307201/file-preview-demo