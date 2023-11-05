package cn.wubo.file.preview.render;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import org.springframework.web.servlet.function.ServerResponse;

public interface IRenderPage {
    Boolean support(FilePreviewService filePreviewService, FilePreviewInfo info);
    ServerResponse render(FilePreviewService filePreviewService, FilePreviewInfo info);
}
