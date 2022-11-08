package cn.wubo.file.preview;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({FilePreviewRegistrar.class})
public @interface EnableFilePreview {
    String convert() default "jod";
    String storage() default "h2";
}
