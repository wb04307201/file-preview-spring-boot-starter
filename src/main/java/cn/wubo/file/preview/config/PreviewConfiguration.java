package cn.wubo.file.preview.config;

import cn.wubo.file.preview.servlet.preview.DownloadServlet;
import cn.wubo.file.preview.servlet.preview.PreviewServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

public class PreviewConfiguration {

    @Bean
    public ServletRegistrationBean<PreviewServlet> officeServlet() {
        ServletRegistrationBean<PreviewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new PreviewServlet());
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
}
