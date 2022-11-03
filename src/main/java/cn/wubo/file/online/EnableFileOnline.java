package cn.wubo.file.online;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({FileOnlineRegistrar.class})
public @interface EnableFileOnline {
    String convert() default "jod";
    String storage() default "h2";
}
