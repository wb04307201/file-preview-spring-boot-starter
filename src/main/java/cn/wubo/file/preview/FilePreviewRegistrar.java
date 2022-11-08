package cn.wubo.file.preview;

import cn.wubo.file.preview.impl.FilePreviewServiceImpl;
import cn.wubo.file.preview.impl.OnlyOfficeFilePreviewServiceImpl;
import cn.wubo.file.preview.servlet.list.FileListConfiguration;
import cn.wubo.file.preview.storage.impl.H2StorageServiceImpl;
import cn.wubo.file.preview.office.impl.JodOfficeConverter;
import cn.wubo.file.preview.office.impl.OnlyOfficeConverter;
import cn.wubo.file.preview.office.impl.SpireOfficeConverter;
import cn.wubo.file.preview.servlet.preview.PreviewConfiguration;
import cn.wubo.file.preview.servlet.preview.OnlyOfficePreviewConfiguration;
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
            String covert = (String) params.get("convert");
            String storage = (String) params.get("storage");
            registerBean(getStorageClass(storage), registry);
            registerBeanConvert(covert, registry);
            registerBean(FileListConfiguration.class, registry);
        }
        //ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

    private void registerBeanConvert(String value, BeanDefinitionRegistry registry) {
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
    }

    private Class<?> getStorageClass(String value) {
        log.debug("storage:{}", value);
        return H2StorageServiceImpl.class;
    }

    private void registerBean(Class<?> clasz, BeanDefinitionRegistry registry) {
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(clasz);
        registry.registerBeanDefinition(clasz.getSimpleName(), genericBeanDefinition);
    }
}
