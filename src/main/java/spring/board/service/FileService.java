package spring.board.service;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import spring.board.dao.JdbcFileDao;
import spring.board.dto.BoardRequest;
import spring.board.dto.FileRequest;
import spring.board.entity.FileEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class FileService {

    private final JdbcFileDao fileDao;

    public FileService(JdbcFileDao fileDao) {
        this.fileDao = fileDao;
    }

    public String uploadFile(BoardRequest boardRequest, HttpServletRequest request) throws IOException {

        // 원본 파일명, 확장자
        String fileName = boardRequest.getFile().getOriginalFilename();
        int idx = fileName.indexOf(".");
        String onlyFName = fileName.substring(0, idx);
        String fileE = fileName.substring(idx+1);

        // 파일명 변환
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyymmddhhmmss"));
        String convertName = onlyFName+formattedNow;

        // 파일 경로 및 저장
        String filePath = "C:\\file\\";
        String fullPath = filePath+convertName+"."+fileE;
        boardRequest.getFile().transferTo(new File(fullPath));

        Integer bIdx = boardRequest.getId();
        System.out.println(onlyFName);
        System.out.println(convertName);
        System.out.println(fileE);
        System.out.println(fullPath);

        FileEntity file = new FileEntity(null, bIdx, onlyFName, convertName, fullPath, fileE);
        if (fileDao.insertFile(file) == 1) return "파일 업로드 완료";
        else return "파일 업로드 실패";


    }

    public ResponseEntity<Object> downloadFile(Integer fIdx) throws IOException {
        FileRequest g_file = fileDao.selectFile(fIdx);

        String path = g_file.getPath(); // 파일 경로 얻기

        try {
            Path filePath = Paths.get(path);
            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

            File file = new File(path);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(file.getName()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            System.out.println("파일 다운로드 성공");
            
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            System.out.println("파일 다운로드 실패");
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }
}
