package cn.wubo.file.online.preview.servlet;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({OnlyOfficePreviewProperties.class})
public class OnlyOfficePreviewConfiguration {

    @Bean
    public ServletRegistrationBean<OnlyOfficePreviewServlet> onlyOfficePreviewServlet() {
        ServletRegistrationBean<OnlyOfficePreviewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new OnlyOfficePreviewServlet());
        registration.addUrlMappings("/file/online/preview");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<OnlyOfficeDownloadServlet> onlyOfficeDownloadServlet() {
        ServletRegistrationBean<OnlyOfficeDownloadServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new OnlyOfficeDownloadServlet());
        registration.addUrlMappings("/file/online/onlyoffice/download");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<OnlyOfficeCallbackServlet> onlyOfficeCallbackServlet() {
        ServletRegistrationBean<OnlyOfficeCallbackServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new OnlyOfficeCallbackServlet());
        registration.addUrlMappings("/file/online/onlyoffice/callback");
        return registration;
    }
}
