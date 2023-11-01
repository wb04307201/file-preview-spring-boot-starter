package cn.wubo.file.preview.page;

import org.springframework.web.servlet.function.ServerResponse;

import java.io.IOException;

public interface IPage {

    ServerResponse build();
}
