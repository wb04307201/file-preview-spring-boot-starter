package cn.wubo.file.preview.common;

import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Objects;

public class BaseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void init() throws ServletException {
        super.init();
        Objects.requireNonNull(WebApplicationContextUtils
                        .getWebApplicationContext(getServletContext()))
                .getAutowireCapableBeanFactory().autowireBean(this);
    }
}
