package cn.wubo.file.online.file.servlet;

import cn.wubo.file.online.BaseServlet;
import cn.wubo.file.online.common.Page;
import cn.wubo.file.online.file.dto.ConvertInfoDto;
import cn.wubo.file.online.file.storage.IStorageService;
import cn.wubo.file.online.file.storage.impl.H2StorageServiceImpl;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileListServlet extends BaseServlet {

    IStorageService fileService = new H2StorageServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        Map<String, Object> data = new HashMap<>();
        data.put("list", fileService.list(new ConvertInfoDto()));

        Page listPage = new Page("list.ftl", data, resp);
        listPage.write();
        //super.doGet(req, resp);
    }
}
