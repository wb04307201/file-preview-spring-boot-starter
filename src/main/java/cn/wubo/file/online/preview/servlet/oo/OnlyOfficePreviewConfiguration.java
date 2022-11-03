package cn.wubo.file.online.preview.servlet.oo;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({OnlyOfficePreviewProperties.class})
public class OnlyOfficePreviewConfiguration {

    @Bean
    public ServletRegistrationBean<OnlyOfficePreviewServlet> officeServlet() {
        ServletRegistrationBean<OnlyOfficePreviewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new OnlyOfficePreviewServlet());
        registration.addUrlMappings("/file/online/preview");
        return registration;
    }
}
