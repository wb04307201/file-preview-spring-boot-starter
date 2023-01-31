package cn.wubo.file.preview.impl;

import cn.wubo.file.preview.IFilePreviewService;
import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.storage.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class FilePreviewServiceImpl implements IFilePreviewService {

    private static ExecutorService threadPool = Executors.newWorkStealingPool();

    @Autowired
    IOfficeConverter officeConverter;

    @Autowired
    IStorageService historyService;

    @Override
    public ConvertInfoDto covert(String sourcePath) {
        log.debug("转换文件-----开始");
        ConvertInfoDto convertInfoDto = doRecord(sourcePath, "10");
        CompletableFuture.runAsync(() -> doCovert(convertInfoDto), threadPool);
        log.debug("转换文件-----结束");
        return convertInfoDto;
    }

    @Override
    public ConvertInfoDto recovert(ConvertInfoDto convertInfoDto) {
        convertInfoDto.setConvertStatus("10");
        convertInfoDto.setConvertStartTime(new Timestamp(System.currentTimeMillis()));
        historyService.save(convertInfoDto);
        CompletableFuture.runAsync(() -> doCovert(convertInfoDto), threadPool);
        return convertInfoDto;
    }

    @Override
    public ConvertInfoDto record(String sourcePath) {
        return doRecord(sourcePath, "00");
    }

    private ConvertInfoDto doRecord(String sourcePath, String convertStatus) {
        Path path = Paths.get(sourcePath);
        ConvertInfoDto convertInfoDto = new ConvertInfoDto();
        convertInfoDto.setSourceFileName(path.getFileName().toString());
        convertInfoDto.setSourceFilePath(path.toAbsolutePath().toString());
        convertInfoDto.setConvertStatus(convertStatus);
        if ("10".equals(convertStatus)) convertInfoDto.setConvertStartTime(new Timestamp(System.currentTimeMillis()));
        convertInfoDto.setSourceExtName(CommonUtils.extName(convertInfoDto.getSourceFileName()));
        convertInfoDto.setSourceType(CommonUtils.fileType(convertInfoDto.getSourceExtName()));
        log.debug("源文件-----sourceFilePath:{} sourceType:{}", convertInfoDto.getSourceFilePath(), convertInfoDto.getSourceType());
        historyService.save(convertInfoDto);
        switch (convertInfoDto.getSourceType()) {
            case "word":
            case "power point":
                convertInfoDto.setExtName("pdf");
                convertInfoDto.setType("pdf");
                break;
            case "excel":
                convertInfoDto.setExtName("html");
                convertInfoDto.setType("html");
                break;
            default:
                convertInfoDto.setExtName(convertInfoDto.getSourceExtName());
                convertInfoDto.setType(convertInfoDto.getSourceType());
                break;
        }
        convertInfoDto.setFileName(convertInfoDto.getId() + CommonUtils.DOT + convertInfoDto.getExtName());
        convertInfoDto.setFilePath(Paths.get("covert" + File.separator + convertInfoDto.getFileName()).toAbsolutePath().toString());
        log.debug("转换文件-----filePath:{} type:{}", convertInfoDto.getFilePath(), convertInfoDto.getType());
        return convertInfoDto;
    }

    public void doCovert(ConvertInfoDto convertInfoDto) {
        log.debug("转换线程-----开始-----{}", Thread.currentThread().getName());
        try {
            switch (convertInfoDto.getSourceType()) {
                case "word":
                    officeConverter.wordConvert(convertInfoDto);
                    break;
                case "power point":
                    officeConverter.pptConvert(convertInfoDto);
                    break;
                case "excel":
                    officeConverter.excelConvert(convertInfoDto);
                    break;
                default:
                    try (OutputStream os = CommonUtils.getOutputStream(convertInfoDto.getFilePath())) {
                        CommonUtils.writeToStream(convertInfoDto.getSourceFilePath(), os);
                        convertInfoDto.setConverter("copy");
                    }
                    break;
            }
            convertInfoDto.setConvertStatus("20");
        } catch (Exception e) {
            convertInfoDto.setConvertStatus("30");
            convertInfoDto.setErrorMessage(e.getMessage());
            log.debug("转换线程-----error:{}-----{}", e.getMessage(), Thread.currentThread().getName());
            throw new RuntimeException(e);
        } finally {
            convertInfoDto.setConvertEndTime(new Timestamp(System.currentTimeMillis()));
            historyService.save(convertInfoDto);
        }
        log.debug("转换线程-----结束-----{}", Thread.currentThread().getName());
    }
}
