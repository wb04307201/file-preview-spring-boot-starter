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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class OnlyOfficeCallbackServlet extends BaseServlet {

    @Autowired
    IStorageService historyService;

    @Autowired
    IFileService fileService;

    private static final String LOST_ID = "缺少预览文件id";

    private static final String FILE_NOT_READY = "文件正在转码，请稍后再试！";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        log.debug("回调文件-----开始");
        Map<String, String[]> paramMap = req.getParameterMap();

        Integer status = Integer.parseInt(paramMap.get("status")[0]);
        int saved = 0;
        if (status == 2 || status == 3) {
            String id = paramMap.get("id")[0];
            ConvertInfoDto convertInfoDto = new ConvertInfoDto();
            convertInfoDto.setId(id);
            List<ConvertInfoDto> list = historyService.list(convertInfoDto);
            convertInfoDto = list.get(0);
            File file = new File(convertInfoDto.getFilePath());

            String downloadUrl = paramMap.get("url")[0];
            CommonUtils.writeFromByte(downloadFromOnlyOffice(downloadUrl), file);

            String changesurl = paramMap.get("changesurl")[0];
            File changeFile = new File("change" + File.separator + convertInfoDto.getId() + File.separator + System.currentTimeMillis() + ".zip");
            CommonUtils.writeFromByte(downloadFromOnlyOffice(changesurl), changeFile);
        }

        log.debug("回调文件-----结束");
        //super.doGet(req, resp);
    }

    private byte[] downloadFromOnlyOffice(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        return response.getBody();
    }
}
