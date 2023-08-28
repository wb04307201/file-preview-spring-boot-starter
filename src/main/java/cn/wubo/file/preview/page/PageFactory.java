package cn.wubo.file.preview.page;

public class PageFactory {

    public static IPage create(Class<? extends IPage> clazz) {
        try {
            if (clazz != null) {
                return clazz.newInstance();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
