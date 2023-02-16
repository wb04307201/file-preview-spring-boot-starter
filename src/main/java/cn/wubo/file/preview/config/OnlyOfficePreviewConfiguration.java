package cn.wubo.file.preview.config;

import cn.wubo.file.preview.servlet.preview.DownloadServlet;
import cn.wubo.file.preview.servlet.preview.OnlyOfficeCallbackServlet;
import cn.wubo.file.preview.servlet.preview.OnlyOfficePreviewProperties;
import cn.wubo.file.preview.servlet.preview.OnlyOfficePreviewServlet;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({OnlyOfficePreviewProperties.class})
public class OnlyOfficePreviewConfiguration {

    @Bean
    public ServletRegistrationBean<OnlyOfficePreviewServlet> onlyOfficePreviewServlet(OnlyOfficePreviewProperties onlyOfficePreviewProperties) {
        ServletRegistrationBean<OnlyOfficePreviewServlet> registration = new ServletRegistrationBean<>();
        OnlyOfficePreviewServlet onlyOfficePreviewServlet = new OnlyOfficePreviewServlet();
        onlyOfficePreviewServlet.setOnlyOfficePreviewProperties(onlyOfficePreviewProperties);
        registration.setServlet(onlyOfficePreviewServlet);
        registration.addUrlMappings("/file/preview");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<DownloadServlet> onlyOfficeDownloadServlet() {
        ServletRegistrationBean<DownloadServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new DownloadServlet());
        registration.addUrlMappings("/file/preview/download");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<OnlyOfficeCallbackServlet> onlyOfficeCallbackServlet() {
        ServletRegistrationBean<OnlyOfficeCallbackServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new OnlyOfficeCallbackServlet());
        registration.addUrlMappings("/file/preview/onlyoffice/callback");
        return registration;
    }
}
