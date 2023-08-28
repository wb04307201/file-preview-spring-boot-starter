package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.page.AbstractPage;

import java.util.Map;

public class MarkdownPage extends AbstractPage<Map<String, Object>> {

    @Override
    public void build(Map<String, Object> params) {
        this.writePage("", params, null);
    }
}
