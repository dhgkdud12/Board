package spring.board.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import spring.board.dao.JdbcFileDao;
import spring.board.dto.BoardDto;
import spring.board.entity.FileEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileService {

    private final JdbcFileDao fileDao;

    public FileService(JdbcFileDao fileDao) {
        this.fileDao = fileDao;
    }

    public String uploadFile(BoardDto boardDto, HttpServletRequest request) throws ParseException, IOException {
        // 원본 파일명, 확장자
        String fileName = boardDto.getFile().getOriginalFilename();
        int idx = fileName.indexOf(".");
        String onlyFName = fileName.substring(0, idx);
        String fileE = fileName.substring(idx+1);

        // 파일명 변환
        SimpleDateFormat fDate = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss.SSS");
        Date n = fDate.parse(fDate.format(new Timestamp(new Date().getTime())));
        String convertName = onlyFName+n.getTime();

        // 파일 경로 및 저장
        String filePath = request.getServletContext().getRealPath("/");
        boardDto.getFile().transferTo(new File(filePath+"/"+boardDto.getFile().getOriginalFilename()));
        String fullPath = filePath+fileName;

        Integer bIdx = boardDto.getId();
        System.out.println(onlyFName);
        System.out.println(convertName);
        System.out.println(fileE);
        System.out.println(fullPath);

        FileEntity file = new FileEntity(null, bIdx, onlyFName, convertName, fullPath, fileE);
        if (fileDao.insertFile(file) == 1) return "파일 저장 완료";
        else return "파일 저장 실패";


    }

    public String downloadFile(Integer fIdx, HttpServletRequest request) throws IOException {
       FileEntity g_file = fileDao.selectFile(fIdx);

        String path = g_file.getPath();
        Path filePath = Paths.get(path);
        System.out.println(filePath);
        Resource resource = new InputStreamResource(Files.newInputStream(filePath));

        File file = new File(path);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더

        return "파일 다운로드 완료";

    }
}
