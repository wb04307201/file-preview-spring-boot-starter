package cn.wubo.file.preview.servlet.preview;

import cn.wubo.file.preview.common.BaseServlet;
import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.IFilePreviewService;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.storage.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Slf4j
public class DownloadServlet extends BaseServlet {

    @Autowired
    IStorageService historyService;

    @Autowired
    IFilePreviewService fileService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("下载文件-----开始");
        String id = req.getParameter("id");
        ConvertInfoDto convertInfoDto = new ConvertInfoDto();
        convertInfoDto.setId(id);
        List<ConvertInfoDto> list = historyService.list(convertInfoDto);
        convertInfoDto = list.get(0);
        File file = new File(convertInfoDto.getFilePath());
        try (OutputStream os = resp.getOutputStream()) {
            CommonUtils.writeToStream(file, os);
        }
        log.debug("下载----结束");
    }
}
