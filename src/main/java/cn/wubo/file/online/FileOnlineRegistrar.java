package cn.wubo.file.online;

import cn.wubo.file.online.file.impl.FileServiceImpl;
import cn.wubo.file.online.file.impl.OnlyOfficeFileServiceImpl;
import cn.wubo.file.online.file.servlet.FileListConfiguration;
import cn.wubo.file.online.file.storage.impl.H2StorageServiceImpl;
import cn.wubo.file.online.office.impl.JodOfficeConverter;
import cn.wubo.file.online.office.impl.OnlyOfficeConverter;
import cn.wubo.file.online.office.impl.SpireOfficeConverter;
import cn.wubo.file.online.preview.servlet.PreviewConfiguration;
import cn.wubo.file.online.preview.servlet.oo.OnlyOfficePreviewConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

@Slf4j
public class FileOnlineRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<String, Object> params = importingClassMetadata.getAnnotationAttributes(EnableFileOnline.class.getName());
        String covert = (String) params.get("convert");
        String storage = (String) params.get("storage");
        registerBean(getStorageClass(storage), registry);
        registerBeanConvert(covert, registry);
        registerBean(FileListConfiguration.class, registry);
        //ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

    private void registerBeanConvert(String value, BeanDefinitionRegistry registry) {
        switch (value) {
            case "spire":
                registerBean(SpireOfficeConverter.class, registry);
                registerBean(FileServiceImpl.class, registry);
                registerBean(PreviewConfiguration.class, registry);
                break;
            case "only":
                registerBean(OnlyOfficeConverter.class, registry);
                registerBean(OnlyOfficeFileServiceImpl.class, registry);
                registerBean(OnlyOfficePreviewConfiguration.class, registry);
                break;
            case "jod":
            default:
                registerBean(JodOfficeConverter.class, registry);
                registerBean(FileServiceImpl.class, registry);
                registerBean(PreviewConfiguration.class, registry);
                break;
        }
    }

    private Class<?> getStorageClass(String value) {
        switch (value) {
            case "h2":
            default:
                return H2StorageServiceImpl.class;
        }
    }

    private void registerBean(Class<?> clasz, BeanDefinitionRegistry registry) {
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(clasz);
        registry.registerBeanDefinition(clasz.getSimpleName(), genericBeanDefinition);
    }
}
