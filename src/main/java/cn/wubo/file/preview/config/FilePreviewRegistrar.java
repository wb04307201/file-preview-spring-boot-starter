package cn.wubo.file.preview.config;

import cn.wubo.file.preview.EnableFilePreview;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

@Slf4j
public class FilePreviewRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> params = importingClassMetadata.getAnnotationAttributes(EnableFilePreview.class.getName());
        if (params != null) {
            registerBean(OfficeConfiguration.class, registry);
            registerBeanConvert((String) params.get("convert"), registry);
        }
        //ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

    private void registerBeanConvert(String value, BeanDefinitionRegistry registry) {
        log.debug("convert:{}", value);
        switch (value) {
            case "spire":
                registerBean(SpireConfiguration.class, registry);
                break;
            case "onlyoffice":
                registerBean(OnlyConfiguration.class, registry);
                break;
            case "jod":
            default:
                registerBean(JodConfiguration.class, registry);
                break;
        }
    }

    private void registerBean(Class<?> clazz, BeanDefinitionRegistry registry) {
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(clazz);
        registry.registerBeanDefinition(clazz.getSimpleName(), genericBeanDefinition);
    }
}
