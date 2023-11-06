package cn.wubo.file.preview.render;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import org.springframework.web.servlet.function.ServerResponse;

public interface IRenderPage {

    /**
     * 页面渲染文件支持判断，返回true则调用render方法
     */
    Boolean support(FilePreviewService filePreviewService, FilePreviewInfo info);

    /**
     * 页面渲染方法，注意返回ServerResponse
     */
    ServerResponse render(FilePreviewService filePreviewService, FilePreviewInfo info);
}
