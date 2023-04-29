package cn.wubo.file.preview;


import cn.wubo.file.preview.config.FilePreviewRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({FilePreviewRegistrar.class})
public @interface EnableFilePreview {

}
