package cn.wubo.file.preview.core;

import cn.wubo.file.preview.EnableFilePreview;
import cn.wubo.file.preview.config.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

@EnableConfigurationProperties({FilePreviewProperties.class})
@Slf4j
public class FilePreviewRegistrar implements ImportBeanDefinitionRegistrar {

    FilePreviewProperties properties;

    public FilePreviewRegistrar(FilePreviewProperties properties) {
        this.properties = properties;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> params = importingClassMetadata.getAnnotationAttributes(EnableFilePreview.class.getName());
        if (params != null) {
            String covert = (String) params.get("convert");
            registerBean(properties.getFilePreviewRecord(), registry);
            registerBean(properties.getFileStorage(), registry);
            registerBeanConvert(covert, registry);
            registerBean(FileListConfiguration.class, registry);
        }
        //ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

    /*private void registerBeanConvert(String value, BeanDefinitionRegistry registry) {
        log.debug("convert:{}", value);
        switch (value) {
            case "spire":
                registerBean(SpireOfficeConverter.class, registry);
                registerBean(FilePreviewServiceImpl.class, registry);
                registerBean(PreviewConfiguration.class, registry);
                break;
            case "onlyoffice":
                registerBean(OnlyOfficeConverter.class, registry);
                registerBean(OnlyOfficeFilePreviewServiceImpl.class, registry);
                registerBean(OnlyOfficePreviewConfiguration.class, registry);
                break;
            case "jod":
            default:
                registerBean(JodOfficeConverter.class, registry);
                registerBean(FilePreviewServiceImpl.class, registry);
                registerBean(PreviewConfiguration.class, registry);
                break;
        }
    }*/

    private void registerBeanConvert(String value, BeanDefinitionRegistry registry) {
        log.debug("convert:{}", value);
        switch (value) {
            case "spire":
                registerBean(SpireConfiguration.class,registry);
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

    private void registerBean(String className, BeanDefinitionRegistry registry) {
        try {
            registerBean(Class.forName(className), registry);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
