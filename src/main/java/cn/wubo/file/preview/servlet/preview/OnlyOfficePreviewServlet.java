package cn.wubo.file.preview.servlet.preview;

import cn.wubo.file.preview.IFilePreviewService;
import cn.wubo.file.preview.common.BaseServlet;
import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.storage.IStorageService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
public class OnlyOfficePreviewServlet extends BaseServlet {

    @Autowired
    IStorageService historyService;

    @Autowired
    IFilePreviewService fileService;

    @Setter
    OnlyOfficePreviewProperties onlyOfficePreviewProperties;

    private static final String LOST_ID = "缺少预览文件id";

    private static final String FILE_NOT_READY = "文件正在转码，请稍后再试！";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        log.debug("预览文件-----开始");
        String id = req.getParameter("id");
        if (!StringUtils.hasLength(id)) {
            log.debug("预览文件-----error:{}", LOST_ID);
            CommonUtils.errorPage(LOST_ID, req, resp);
        } else {
            log.debug("预览文件-----id:{}", id);
            ConvertInfoDto convertInfoDto = new ConvertInfoDto();
            convertInfoDto.setId(id);
            List<ConvertInfoDto> list = historyService.list(convertInfoDto);
            convertInfoDto = list.get(0);
            log.debug("预览文件-----convertStatus:{}", convertInfoDto.getConvertStatus());
            if ("10".equals(convertInfoDto.getConvertStatus())) {
                log.debug("预览文件-----error:{}", FILE_NOT_READY);
                CommonUtils.errorPage(FILE_NOT_READY, req, resp);
            } else {
                if ("00".equals(convertInfoDto.getConvertStatus()))
                    convertInfoDto = fileService.recovert(convertInfoDto);
                convertInfoDto.setPrePreviewTime(new Timestamp(System.currentTimeMillis()));
                historyService.save(convertInfoDto);

                PreviewBuilder.onlyOffice(convertInfoDto, req, resp, onlyOfficePreviewProperties);
            }
        }
        log.debug("预览文件-----结束");
        //super.doGet(req, resp);
    }
}
