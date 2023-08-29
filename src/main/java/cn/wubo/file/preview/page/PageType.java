package cn.wubo.file.preview.page;

import cn.wubo.file.preview.page.impl.*;

import java.util.HashMap;
import java.util.Map;

public enum PageType {

    Markdown(MarkdownPage.class), Code(CodePage.class), Epub(EpubPage.class), Video(VideoPage.class), Audio(AudioPage.class), Xmind(XmindPage.class), Pdf(PdfPage.class), Only(OnlyOfficePage.class), Lool(LoolPage.class), Compress(CompressPage.class),
    ;

    private static final String[] CODES = {"sql", "cpp", "java", "xml", "javascript", "json", "css", "python"};
    private static final Map<String, PageType> PREVIEW_TYPE_MAPPER = new HashMap<>();

    static {
        PREVIEW_TYPE_MAPPER.put("markdown", PageType.Markdown);
        for (String code : CODES) {
            PREVIEW_TYPE_MAPPER.put(code, PageType.Code);
        }
        PREVIEW_TYPE_MAPPER.put("epub", PageType.Epub);
        PREVIEW_TYPE_MAPPER.put("video", PageType.Video);
        PREVIEW_TYPE_MAPPER.put("audio", PageType.Audio);
        PREVIEW_TYPE_MAPPER.put("xmind", PageType.Xmind);
        PREVIEW_TYPE_MAPPER.put("pdf", PageType.Pdf);
        PREVIEW_TYPE_MAPPER.put("only", PageType.Only);
        PREVIEW_TYPE_MAPPER.put("lool", PageType.Lool);
        PREVIEW_TYPE_MAPPER.put("compressed file", PageType.Compress);
    }

    public static Class<? extends AbstractPage> getClass(String fileType) {
        if (PREVIEW_TYPE_MAPPER.containsKey(fileType)) return PREVIEW_TYPE_MAPPER.get(fileType).clazz;
        else return CommomPage.class;
    }

    private Class<? extends AbstractPage> clazz;

    PageType(Class<? extends AbstractPage> clazz) {
        this.clazz = clazz;
    }
}
