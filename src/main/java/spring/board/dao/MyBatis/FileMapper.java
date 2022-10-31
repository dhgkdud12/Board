package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.dto.file.FileRequest;
import spring.board.domain.FileEntity;

@Mapper
public interface FileMapper {
    public int insertFile(FileEntity file);

    public FileRequest selectFile(Integer fIdx);

    public FileRequest selectFileByBoardId(Integer bIdx);


}
