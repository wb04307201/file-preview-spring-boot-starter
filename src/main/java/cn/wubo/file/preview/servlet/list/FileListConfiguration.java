package cn.wubo.file.preview.servlet.list;

import cn.wubo.file.preview.storage.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

public class FileListConfiguration {

    @Autowired
    IStorageService historyService;

    @Bean
    public ServletRegistrationBean<FileListServlet> listServlet() {
        historyService.check();
        ServletRegistrationBean<FileListServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FileListServlet());
        registration.addUrlMappings("/file/preview/list");
        return registration;
    }
}
