package cn.wubo.file.online;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void init() throws ServletException {
        super.init();
        WebApplicationContextUtils
                .getWebApplicationContext(getServletContext())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }
}
