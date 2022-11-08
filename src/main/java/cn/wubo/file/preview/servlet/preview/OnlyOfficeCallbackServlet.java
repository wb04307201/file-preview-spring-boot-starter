package cn.wubo.file.preview.servlet.preview;

import cn.wubo.file.preview.common.BaseServlet;
import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.IFilePreviewService;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.storage.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@Slf4j
public class OnlyOfficeCallbackServlet extends BaseServlet {

    @Autowired
    IStorageService historyService;

    @Autowired
    IFilePreviewService fileService;

    private static final String STATUS = "status";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        log.debug("回调文件-----开始");
        Map<String, String[]> paramMap = req.getParameterMap();

        if (paramMap.containsKey(STATUS) && (Integer.parseInt(paramMap.get(STATUS)[0]) == 2 || Integer.parseInt(paramMap.get(STATUS)[0]) == 3)) {
            String id = paramMap.get("id")[0];
            ConvertInfoDto convertInfoDto = new ConvertInfoDto();
            convertInfoDto.setId(id);
            List<ConvertInfoDto> list = historyService.list(convertInfoDto);
            convertInfoDto = list.get(0);

            downloadFromOnlyOffice(paramMap.get("url")[0],convertInfoDto.getFilePath());
            downloadFromOnlyOffice(paramMap.get("changesurl")[0],"change" + File.separator + convertInfoDto.getId() + File.separator + System.currentTimeMillis() + ".zip");
        }

        try (PrintWriter writer = resp.getWriter()) {
            writer.write("{\"error\":0}");
        }

        log.debug("回调文件-----结束");
        //super.doGet(req, resp);
    }

    private void downloadFromOnlyOffice(String url,String target) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        CommonUtils.writeFromByte(response.getBody(),target);
    }
}
