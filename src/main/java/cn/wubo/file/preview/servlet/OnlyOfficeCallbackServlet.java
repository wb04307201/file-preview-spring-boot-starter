package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class OnlyOfficeCallbackServlet extends HttpServlet {

    FilePreviewService filePreviewService;

    public OnlyOfficeCallbackServlet(FilePreviewService filePreviewService) {
        this.filePreviewService = filePreviewService;
    }

    private static final String STATUS = "status";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("回调文件-----开始");
        Map<String, String[]> paramMap = req.getParameterMap();
        if (paramMap.containsKey(STATUS) && (Integer.parseInt(paramMap.get(STATUS)[0]) == 2 || Integer.parseInt(paramMap.get(STATUS)[0]) == 3)) {
            String id = paramMap.get("id")[0];
            FilePreviewInfo info = filePreviewService.findById(id);

            //取回修改后文件
            //byte[] fileBytes = downloadFromOnlyOffice(paramMap.get("url")[0]);
            //变更记录下载
            //byte[] changeBytes = downloadFromOnlyOffice(paramMap.get("changesurl")[0]);
        }

        try (PrintWriter writer = resp.getWriter()) {
            writer.write("{\"error\":0}");
        }
        log.debug("回调文件-----结束");
    }

    private byte[] downloadFromOnlyOffice(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        return response.getBody();
    }
}
