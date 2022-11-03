package cn.wubo.file.online.preview.servlet;

import cn.wubo.file.online.BaseServlet;
import cn.wubo.file.online.common.CommonUtils;
import cn.wubo.file.online.common.Page;
import cn.wubo.file.online.file.IFileService;
import cn.wubo.file.online.file.dto.ConvertInfoDto;
import cn.wubo.file.online.file.storage.IStorageService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OnlyOfficePreviewServlet extends BaseServlet {

    @Autowired
    IStorageService historyService;

    @Autowired
    IFileService fileService;

    OnlyOfficePreviewProperties onlyOfficePreviewProperties;

    private static final String LOST_ID = "缺少预览文件id";

    private static final String FILE_NOT_READY = "文件正在转码，请稍后再试！";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        log.debug(onlyOfficePreviewProperties.toString());

        log.debug("预览文件-----开始");
        String id = req.getParameter("id");
        if (!StringUtils.hasLength(id)) {
            log.debug("预览文件-----error:{}", LOST_ID);
            CommonUtils.errorPage(LOST_ID, resp);
        } else {
            log.debug("预览文件-----id:{}", id);
            ConvertInfoDto convertInfoDto = new ConvertInfoDto();
            convertInfoDto.setId(id);
            List<ConvertInfoDto> list = historyService.list(convertInfoDto);
            convertInfoDto = list.get(0);
            log.debug("预览文件-----convertStatus:{}", convertInfoDto.getConvertStatus());
            if ("10".equals(convertInfoDto.getConvertStatus())) {
                log.debug("预览文件-----error:{}", FILE_NOT_READY);
                CommonUtils.errorPage(FILE_NOT_READY, resp);
            } else {
                if ("00".equals(convertInfoDto.getConvertStatus()))
                    convertInfoDto = fileService.covert(new File(convertInfoDto.getFilePath()), convertInfoDto);
                convertInfoDto.setPrePreviewTime(new Timestamp(System.currentTimeMillis()));
                historyService.save(convertInfoDto);

                CommonUtils.setContentType(resp, convertInfoDto.getType(), convertInfoDto.getExtName());

                if ("word".equals(convertInfoDto.getType()) || "excel".equals(convertInfoDto.getType()) || "power point".equals(convertInfoDto.getType())) {
                    Map<String, Object> data = new HashMap<>();
                    Page onlyofficePage = new Page("onlyoffice.ftl", data, resp.getWriter());
                    try {
                        onlyofficePage.write();
                    } catch (TemplateException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    File file = new File(convertInfoDto.getFilePath());
                    try (OutputStream os = resp.getOutputStream()) {
                        CommonUtils.writeToStream(file, os);
                    }
                }
            }
        }
        log.debug("预览文件-----结束");
        //super.doGet(req, resp);
    }
}
