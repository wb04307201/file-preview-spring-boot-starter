package cn.wubo.file.preview.impl;

import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.IFilePreviewService;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.storage.IStorageService;
import cn.wubo.file.preview.office.IOfficeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class OnlyOfficeFilePreviewServiceImpl implements IFilePreviewService {

    private static ExecutorService threadPool = Executors.newWorkStealingPool();

    @Autowired
    IOfficeConverter officeConverter;

    @Autowired
    IStorageService historyService;

    @Override
    public ConvertInfoDto covert(String sourcePath) {
        ConvertInfoDto convertInfoDto = doRecord(sourcePath, "10");
        CompletableFuture.runAsync(() -> doCovert(convertInfoDto), threadPool);
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
        log.debug("?????????-----sourceExtName:{} sourceType:{}", convertInfoDto.getSourceExtName(), convertInfoDto.getSourceType());
        historyService.save(convertInfoDto);
        convertInfoDto.setExtName(convertInfoDto.getSourceExtName());
        convertInfoDto.setType(convertInfoDto.getSourceType());
        convertInfoDto.setFileName(convertInfoDto.getId() + CommonUtils.DOT + convertInfoDto.getExtName());
        convertInfoDto.setFilePath(Paths.get("covert" + File.separator + convertInfoDto.getFileName()).toAbsolutePath().toString());
        log.debug("????????????-----filePath:{} type:{}", convertInfoDto.getFilePath(), convertInfoDto.getType());
        return convertInfoDto;
    }

    public void doCovert(ConvertInfoDto convertInfoDto) {
        log.debug("????????????-----??????-----OnlyOffice-----{}", Thread.currentThread().getName());
        try (OutputStream os = CommonUtils.getOutputStream(convertInfoDto.getFilePath())) {
            CommonUtils.writeToStream(convertInfoDto.getSourceFilePath(), os);
            convertInfoDto.setConverter("copy");
            convertInfoDto.setConvertStatus("20");
        } catch (IOException e) {
            convertInfoDto.setConvertStatus("30");
            convertInfoDto.setErrorMessage(e.getMessage());
            log.debug("????????????-----error:{}-----OnlyOffice-----{}", e.getMessage(), Thread.currentThread().getName());
            throw new RuntimeException(e);
        } finally {
            convertInfoDto.setConvertEndTime(new Timestamp(System.currentTimeMillis()));
            historyService.save(convertInfoDto);
        }
        log.debug("????????????-----??????-----OnlyOffice-----{}", Thread.currentThread().getName());
    }
}
