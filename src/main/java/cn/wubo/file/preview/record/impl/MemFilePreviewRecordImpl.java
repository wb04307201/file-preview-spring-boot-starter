package cn.wubo.file.preview.record.impl;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.exception.RecordRuntimeException;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MemFilePreviewRecordImpl implements IFilePreviewRecord {

    private static List<FilePreviewInfo> filePreviewInfos = new ArrayList<>();

    @Override
    public FilePreviewInfo save(FilePreviewInfo filePreviewInfo) {
        if (StringUtils.hasLength(filePreviewInfo.getId())) {
            filePreviewInfos.stream().filter(e -> e.getId().equals(filePreviewInfo.getId())).findAny().ifPresent(e -> e = filePreviewInfo);
        } else {
            filePreviewInfo.setId(UUID.randomUUID().toString());
            filePreviewInfos.add(filePreviewInfo);
        }
        return filePreviewInfo;
    }

    @Override
    public List<FilePreviewInfo> list(FilePreviewInfo filePreviewInfo) {
        return filePreviewInfos.stream().filter(e -> !StringUtils.hasLength(filePreviewInfo.getId()) || e.getId().equals(filePreviewInfo.getId())).filter(e -> !StringUtils.hasLength(filePreviewInfo.getFileName()) || e.getFileName().contains(filePreviewInfo.getFileName())).filter(e -> !StringUtils.hasLength(filePreviewInfo.getFilePath()) || e.getFilePath().contains(filePreviewInfo.getFilePath())).toList();
    }

    @Override
    public FilePreviewInfo findById(String id) {
        Optional<FilePreviewInfo> optionalFileInfo = filePreviewInfos.stream().filter(e -> e.getId().equals(id)).findAny();
        if (optionalFileInfo.isPresent()) return optionalFileInfo.get();
        else throw new RecordRuntimeException("文件记录未找到!");
    }

    @Override
    public Boolean delete(FilePreviewInfo filePreviewInfo) {
        return filePreviewInfos.removeAll(list(filePreviewInfo));
    }

    @Override
    public void init() {

    }
}
