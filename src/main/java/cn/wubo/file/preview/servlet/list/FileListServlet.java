package cn.wubo.file.preview.servlet.list;

import cn.wubo.file.preview.common.BaseServlet;
import cn.wubo.file.preview.common.Page;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.storage.IStorageService;
import cn.wubo.file.preview.storage.impl.H2StorageServiceImpl;

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
