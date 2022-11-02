package spring.board.service.file;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spring.board.dao.MyBatis.FileMapper;
import spring.board.dto.board.BoardRequest;
import spring.board.dto.file.FileRequest;
import spring.board.domain.FileEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {

//    private final JdbcFileDao fileDao;

//    public FileService(JdbcFileDao fileDao) {
//        this.fileDao = fileDao;
//    }

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }


    public String uploadFile(BoardRequest boardRequest, HttpServletRequest request) throws IOException {

        // 원본 파일명, 확장자
        String fileName = boardRequest.getFile().getOriginalFilename();
        int idx = fileName.indexOf(".");
        String onlyFName = fileName.substring(0, idx);
        String fileE = fileName.substring(idx+1);

        // 파일명 변환
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        String convertName = onlyFName+formattedNow;

        // 파일 경로 및 저장
        String filePath = "C:\\file\\";
        String fullPath = filePath+convertName+"."+fileE;
        boardRequest.getFile().transferTo(new File(fullPath));

        // 파일 사이즈
        long fileSize = boardRequest.getFile().getSize();

        Integer bIdx = boardRequest.getBId();
        System.out.println(onlyFName);
        System.out.println(convertName);
        System.out.println(fileE);
        System.out.println(fullPath);

        FileEntity file = new FileEntity(null, bIdx, onlyFName, convertName, filePath, fileE, fileSize);
//        if (fileDao.insertFile(file) == 1) return "파일 업로드 완료";
        if (fileMapper.insertFile(file) == 1) return "파일 업로드 완료";
        else return "파일 업로드 실패";
    }

    public ResponseEntity<Object> downloadFile(Integer fIdx) {
//        FileRequest g_file = fileDao.selectFile(fIdx);
        FileRequest g_file = fileMapper.selectFile(fIdx);

        String path = g_file.getPath()+g_file.getConvertName()+"."+g_file.getExtension(); // 파일 경로 얻기

        try {
            Path filePath = Paths.get(path);

            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(g_file.getFileName()+"."+g_file.getExtension()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            System.out.println("파일 다운로드 성공");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            System.out.println("파일 다운로드 실패");
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }
}
