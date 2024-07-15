package cn.wubo.file.preview.page;

import cn.wubo.file.preview.page.impl.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 页面类型的枚举类，定义了不同类型的页面对应的类。
 */
public enum PageType {

    // @formatter:off
    Markdown(MarkdownPage.class),
    Code(CodePage.class),
    Epub(EpubPage.class),
    Video(VideoPage.class),
    Audio(AudioPage.class),
    Xmind(XmindPage.class),
    Pdf(PdfPage.class),
    Ofd(OfdPage.class),
    Only(OnlyOfficePage.class),
    Lool(LoolPage.class),
    Cool(CoolPage.class),
    Compress(CompressPage.class),
    Bpmn(BpmnPage.class),
    Cmmn(CmmnPage.class),
    Dmn(DmnPage.class),
    O3dv(O3dvPage.class);
    // @formatter:on

    /**
     * 代码类型数组，定义了支持预览的代码类型。
     */
    private static final String[] CODES = {"sql", "cpp", "java", "xml", "javascript", "json", "css", "python"};

    /**
     * 页面类型与类的映射，用于根据页面类型快速获取对应的类。
     */
    private static final Map<String, PageType> PREVIEW_TYPE_MAPPER = new HashMap<>();

    /**
     * 静态初始化块，填充页面类型与类的映射表。
     */
    static {
        PREVIEW_TYPE_MAPPER.put("markdown", PageType.Markdown);
        for (String code : CODES)
            PREVIEW_TYPE_MAPPER.put(code, PageType.Code);
        PREVIEW_TYPE_MAPPER.put("epub", PageType.Epub);
        PREVIEW_TYPE_MAPPER.put("video", PageType.Video);
        PREVIEW_TYPE_MAPPER.put("audio", PageType.Audio);
        PREVIEW_TYPE_MAPPER.put("xmind", PageType.Xmind);
        PREVIEW_TYPE_MAPPER.put("pdf", PageType.Pdf);
        PREVIEW_TYPE_MAPPER.put("only", PageType.Only);
        PREVIEW_TYPE_MAPPER.put("lool", PageType.Lool);
        PREVIEW_TYPE_MAPPER.put("cool", PageType.Cool);
        PREVIEW_TYPE_MAPPER.put("compressed file", PageType.Compress);
        PREVIEW_TYPE_MAPPER.put("bpmn", PageType.Bpmn);
        PREVIEW_TYPE_MAPPER.put("cmmn", PageType.Cmmn);
        PREVIEW_TYPE_MAPPER.put("dmn", PageType.Dmn);
        PREVIEW_TYPE_MAPPER.put("ofd", PageType.Ofd);
        PREVIEW_TYPE_MAPPER.put("o3dv", PageType.O3dv);
    }

    /**
     * 根据文件类型获取对应的页面类。
     *
     * @param fileType 文件类型字符串。
     * @return 对应的页面类，如果不存在则返回CommonPage类。
     */
    public static Class<? extends AbstractPage> getClass(String fileType) {
        if (PREVIEW_TYPE_MAPPER.containsKey(fileType)) return PREVIEW_TYPE_MAPPER.get(fileType).clazz;
        else return CommomPage.class;
    }

    /**
     * 页面类型对应的类。
     */
    private Class<? extends AbstractPage> clazz;

    /**
     * 构造函数，初始化页面类型与类的映射。
     *
     * @param clazz 页面类型对应的类。
     */
    PageType(Class<? extends AbstractPage> clazz) {
        this.clazz = clazz;
    }
}

