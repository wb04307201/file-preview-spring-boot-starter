package cn.wubo.file.online.file.impl;

import cn.wubo.file.online.common.CommonUtils;
import cn.wubo.file.online.file.IFileService;
import cn.wubo.file.online.file.dto.ConvertInfoDto;
import cn.wubo.file.online.file.storage.IStorageService;
import cn.wubo.file.online.office.IOfficeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class AbstractFileService implements IFileService {
    private static ExecutorService threadPool = Executors.newWorkStealingPool();

    @Autowired
    IOfficeConverter officeConverter;

    @Autowired
    IStorageService historyService;

    @Override
    public ConvertInfoDto covert(File file) {
        log.debug("转换文件-----开始");
        ConvertInfoDto convertInfoDto = doRecord(file, "10");
        CompletableFuture.runAsync(() -> doCovert(file, convertInfoDto), threadPool);
        log.debug("转换文件-----结束");
        return convertInfoDto;
    }

    @Override
    public ConvertInfoDto covert(File file, ConvertInfoDto convertInfoDto) {
        convertInfoDto.setConvertStatus("10");
        convertInfoDto.setConvertStartTime(new Timestamp(System.currentTimeMillis()));
        historyService.save(convertInfoDto);
        CompletableFuture.runAsync(() -> doCovert(file, convertInfoDto), threadPool);
        return convertInfoDto;
    }

    @Override
    public ConvertInfoDto record(File file) {
        return doRecord(file, "00");
    }

    private ConvertInfoDto doRecord(File file, String convertStatus) {
        String fileName = file.getName();
        ConvertInfoDto convertInfoDto = new ConvertInfoDto();
        convertInfoDto.setOrgFileName(fileName);
        convertInfoDto.setOrgFilePath(file.getAbsolutePath());
        convertInfoDto.setConvertStatus(convertStatus);
        convertInfoDto.setConvertStartTime(new Timestamp(System.currentTimeMillis()));
        convertInfoDto.setOrgExtName(CommonUtils.extName(convertInfoDto.getOrgFileName()));
        convertInfoDto.setOrgType(CommonUtils.fileType(convertInfoDto.getOrgExtName()));
        log.debug("转换文件-----orgExtName:{} orgType:{}", convertInfoDto.getOrgExtName(), convertInfoDto.getOrgType());
        historyService.save(convertInfoDto);
        return convertInfoDto;
    }

    /*private void doCovert(File file, ConvertInfoDto convertInfoDto) {
        log.debug("转换线程-----开始-----" + Thread.currentThread().getName());
        switch (convertInfoDto.getOrgType()) {
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
                convertInfoDto.setExtName(convertInfoDto.getOrgExtName());
                convertInfoDto.setType(convertInfoDto.getOrgType());
                break;
        }

        convertInfoDto.setFileName(convertInfoDto.getId() + CommonUtils.DOT + convertInfoDto.getExtName());
        log.debug("转换文件-----covertFileName:{}", convertInfoDto.getFileName());

        String covertFilePath = "covert" + File.separator + convertInfoDto.getFileName();
        log.debug("转换文件-----covertFilePath:{}", covertFilePath);
        File covertFile = new File(covertFilePath);

        try (OutputStream os = CommonUtils.getOutputStream(covertFile)) {
            switch (convertInfoDto.getOrgType()) {
                case "word":
                    officeConverter.wordConvert(file, os);
                    break;
                case "power point":
                    officeConverter.pptConvert(file, os);
                    break;
                case "excel":
                    officeConverter.excelConvert(file, os);
                    break;
                default:
                    CommonUtils.writeToStream(file, os);
                    break;
            }
        } catch (IOException e) {
            convertInfoDto.setConvertStatus("30");
            convertInfoDto.setErrorMessage(e.getMessage());
            log.debug("转换文件-----error:{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            convertInfoDto.setFilePath(covertFile.getAbsolutePath());
            if ("10".equals(convertInfoDto.getConvertStatus())) convertInfoDto.setConvertStatus("20");
            convertInfoDto.setConvertEndTime(new Timestamp(System.currentTimeMillis()));
            historyService.save(convertInfoDto);
        }
        log.debug("转换线程-----结束-----" + Thread.currentThread().getName());
    }*/

    private void doCovert(File file, ConvertInfoDto convertInfoDto) {
        log.debug("转换文件-----开始-----OnlyOffice-----" + Thread.currentThread().getName());
        convertInfoDto.setExtName(convertInfoDto.getOrgExtName());
        convertInfoDto.setType(convertInfoDto.getOrgType());

        convertInfoDto.setFileName(convertInfoDto.getId() + CommonUtils.DOT + convertInfoDto.getExtName());
        log.debug("转换文件-----covertFileName:{}", convertInfoDto.getFileName());

        String covertFilePath = "covert" + File.separator + convertInfoDto.getFileName();
        log.debug("转换文件-----covertFilePath:{}", covertFilePath);
        File covertFile = new File(covertFilePath);

        try (OutputStream os = CommonUtils.getOutputStream(covertFile)) {
            CommonUtils.writeToStream(file, os);
        } catch (IOException e) {
            convertInfoDto.setConvertStatus("30");
            convertInfoDto.setErrorMessage(e.getMessage());
            log.debug("转换文件-----error:{}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            convertInfoDto.setFilePath(covertFile.getAbsolutePath());
            if ("10".equals(convertInfoDto.getConvertStatus())) convertInfoDto.setConvertStatus("20");
            convertInfoDto.setConvertEndTime(new Timestamp(System.currentTimeMillis()));
            historyService.save(convertInfoDto);
        }
        log.debug("转换文件-----结束-----OnlyOffice-----" + Thread.currentThread().getName());
    }
}
