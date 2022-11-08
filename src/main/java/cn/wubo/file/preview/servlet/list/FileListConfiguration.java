package cn.wubo.file.preview.servlet.list;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileListConfiguration {

    @Bean
    public ServletRegistrationBean<FileListServlet> listServlet() {
        ServletRegistrationBean<FileListServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FileListServlet());
        registration.addUrlMappings("/file/preview/list");
        return registration;
    }
}
