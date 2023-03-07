package cn.wubo.file.preview.record.impl;

import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.sql.util.ConnectionParam;
import cn.wubo.sql.util.ConnectionPool;
import cn.wubo.sql.util.ExecuteSqlUtils;
import cn.wubo.sql.util.ModelSqlUtils;
import org.springframework.util.StringUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class H2FilePreviewRecordImpl implements IFilePreviewRecord {

    private static final String FILE_CONVERT_INFO = "file_online_convert_info";

    private static ConnectionPool connectionPool = new ConnectionPool(new ConnectionParam());

    @Override
    public ConvertInfoDto save(ConvertInfoDto convertInfoDto) {
        if (!StringUtils.hasLength(convertInfoDto.getId())) {
            convertInfoDto.setId(UUID.randomUUID().toString());
            try {
                Connection conn = connectionPool.getConnection();
                int res = ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.insertSql(FILE_CONVERT_INFO, convertInfoDto), new HashMap<>());
                connectionPool.returnConnection(conn);
                if (res == 1) return convertInfoDto;
                else throw new RuntimeException("结果不为1");
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                Connection conn = connectionPool.getConnection();
                int res = ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.updateByIdSql(FILE_CONVERT_INFO, convertInfoDto), new HashMap<>());
                connectionPool.returnConnection(conn);
                if (res == 1) return convertInfoDto;
                else throw new RuntimeException("结果不为1");
            } catch (SQLException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<ConvertInfoDto> list(ConvertInfoDto convertInfoDto) {
        try {
            Connection conn = connectionPool.getConnection();
            List<ConvertInfoDto> res = ExecuteSqlUtils.executeQuery(conn, ModelSqlUtils.selectSql(FILE_CONVERT_INFO, convertInfoDto), new HashMap<>(), ConvertInfoDto.class);
            connectionPool.returnConnection(conn);
            return res;
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() {
        try {
            Connection conn = connectionPool.getConnection();
            if (!ExecuteSqlUtils.isTableExists(conn, FILE_CONVERT_INFO, connectionPool.getDbType())) {
                ExecuteSqlUtils.executeUpdate(conn, ModelSqlUtils.createSql(FILE_CONVERT_INFO, new ConvertInfoDto()), new HashMap<>());
            }
            connectionPool.returnConnection(conn);
        } catch (SQLException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
