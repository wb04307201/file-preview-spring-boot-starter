package cn.wubo.file.preview;


import cn.wubo.file.preview.core.FilePreviewRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({FilePreviewRegistrar.class})
public @interface EnableFilePreview {
    //office文件预览转化工具
    String convert() default "jod";
    //存储类class路径
    String storage() default "cn.wubo.file.preview.storage.impl.H2StorageServiceImpl";
}
