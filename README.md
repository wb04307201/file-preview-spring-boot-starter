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
#### spire。office 使用注解@EnableFilePreview(convert="spire")
#### onlyoffice 使用注解@EnableFilePreview(convert="onlyoffice")

## 第五步 预览文件



### 示例
https://gitee.com/wb04307201/file-preview-demo