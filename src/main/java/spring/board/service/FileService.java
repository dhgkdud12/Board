package spring.board.service;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.core.io.FileSystemResource;
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
import java.io.*;
import java.net.SocketException;
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
        String formattedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
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

        FileEntity file = new FileEntity(null, bIdx, onlyFName, convertName, filePath, fileE);
        if (fileDao.insertFile(file) == 1) return "파일 업로드 완료";
        else return "파일 업로드 실패";


    }

    public String uploadFiletoFtp(BoardRequest boardRequest, HttpServletRequest request) throws IOException {

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
        String filePath = "/file/";
        String fullPath = filePath+convertName+"."+fileE;

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            int reply = 0;
            ftpClient.connect("211.62.140.77",21); // 호스트 연결
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.out.println("연결 비정상");
            } else {
                ftpClient.login("administrator","ulalalab12!@");
                showServerReply(ftpClient);

                ftpClient.enterLocalPassiveMode(); // passive 모드
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                boolean isSuccess = ftpClient.storeFile(fullPath, boardRequest.getFile().getInputStream()); // 파일 저장
                System.out.println("fullPath: " + fullPath);
                if (isSuccess) {
                    System.out.println("업로드 성공");
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("파일이 없음");
        } catch (SocketException e){
            System.out.println("소켓 오륲");
        }
        finally {
            if (request.getInputStream() != null) {
                request.getInputStream().close();
            }
        }

        ftpClient.disconnect(); // 연결 해제


        Integer bIdx = boardRequest.getId();
        System.out.println(onlyFName);
        System.out.println(convertName);
        System.out.println(fileE);
        System.out.println(fullPath);

        FileEntity fileEntity = new FileEntity(null, bIdx, onlyFName, convertName, filePath, fileE);
        if (fileDao.insertFile(fileEntity) == 1) return "파일 업로드 완료";
        else return "파일 업로드 실패";


    }

    private static void showServerReply(FTPClient ftpClient){
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }

    public ResponseEntity<Object> downloadFilefromFTP(Integer fIdx) throws IOException {
        FileRequest g_file = fileDao.selectFile(fIdx);

        String f_path = g_file.getPath(); // 파일 경로 얻기
        System.out.println("path: "+f_path);

        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
            int reply = 0;
            ftpClient.connect("211.62.140.77",21); // 호스트 연결
            reply = ftpClient.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                System.out.println("연결 비정상");
            } else {
                ftpClient.login("administrator","ulalalab12!@");
                showServerReply(ftpClient);

                ftpClient.enterLocalPassiveMode(); // passive 모드
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

                System.out.println("remote address: " + ftpClient.getLocalAddress());

                ftpClient.changeWorkingDirectory(f_path);

                String name = "output";
                OutputStream outputStream = new FileOutputStream(name);

                if (ftpClient.retrieveFile(g_file.getConvertName()+"."+g_file.getExtention(), outputStream))
                    System.out.println("success");

                Path path = Paths.get(g_file.getConvertName()+"."+g_file.getExtention());
                long bytes = Files.size(path);

                System.out.print("os: ");
//                outputStream.flush();

//                String path = ftpClient.printWorkingDirectory()+"/"+g_file.getConvertName()+"."+g_file.getExtention();
//                System.out.println(path);

                InputStream inputStream = new FileInputStream("output");


                byte[] buffer = new byte[1024];

                while (true) {
                    int count = inputStream.read(buffer);
                    if (count == -1) {
                        System.out.println("더이상 읽을 데이터가 없음");
                        break;
                    }
                }
                inputStream.close();

                Resource resource = new InputStreamResource(inputStream); // 파일 resource 얻기

                HttpHeaders headers = new HttpHeaders();
                headers.setContentDisposition(ContentDisposition.builder("attachment").filename(g_file.getFileName()+"."+g_file.getExtention()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
                System.out.println("파일 다운로드 성공");
                ftpClient.disconnect(); // 연결 해제
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            }

        } catch(Exception e) {
            System.out.println("파일 다운로드 실패");
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
        return null;
    }

    public ResponseEntity<Object> downloadFile(Integer fIdx) throws IOException {
        FileRequest g_file = fileDao.selectFile(fIdx);

        String path = g_file.getPath()+g_file.getConvertName()+"."+g_file.getExtention(); // 파일 경로 얻기

        try {
            Path filePath = Paths.get(path);

            Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename(g_file.getFileName()+"."+g_file.getExtention()).build());  // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            System.out.println("파일 다운로드 성공");

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch(Exception e) {
            System.out.println("파일 다운로드 실패");
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }
}
