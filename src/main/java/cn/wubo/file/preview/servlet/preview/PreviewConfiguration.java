package cn.wubo.file.preview.servlet.preview;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PreviewConfiguration {

    @Bean
    public ServletRegistrationBean<PreviewServlet> officeServlet() {
        ServletRegistrationBean<PreviewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new PreviewServlet());
        registration.addUrlMappings("/file/online/preview");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<DownloadServlet> onlyOfficeDownloadServlet() {
        ServletRegistrationBean<DownloadServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new DownloadServlet());
        registration.addUrlMappings("/file/online/download");
        return registration;
    }
}
