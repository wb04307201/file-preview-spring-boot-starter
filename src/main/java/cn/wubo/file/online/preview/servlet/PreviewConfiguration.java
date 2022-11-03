package cn.wubo.file.online.preview.servlet;

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
}
