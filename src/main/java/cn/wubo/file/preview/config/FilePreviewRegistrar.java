package cn.wubo.file.preview.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

@Slf4j
public class FilePreviewRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(OfficeConfiguration.class);
        //Map<String, Object> params = importingClassMetadata.getAnnotationAttributes(EnableFilePreview.class.getName());
        //MutablePropertyValues values = genericBeanDefinition.getPropertyValues();
        //values.addPropertyValue("officeConverter", params.get("convert"));
        registry.registerBeanDefinition(OfficeConfiguration.class.getSimpleName(), genericBeanDefinition);
        //ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }
}
