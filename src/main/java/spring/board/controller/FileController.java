package spring.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.board.domain.response.CommonResponse;
import spring.board.domain.response.ResponseStatus;
import spring.board.service.FileService;

import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // 파일 다운로드
    @GetMapping("/download/{fIdx}")
    public CommonResponse download(@PathVariable("fIdx") Integer fIdx) throws IOException {
        ResponseEntity<Object> entity = fileService.downloadFilefromFTP(fIdx);
        if (entity.getStatusCode().value() == 200) {
            return new CommonResponse<>(ResponseStatus.SUCCESS, 200, "파일 다운로드 성공", entity);
        }
        else
            return new CommonResponse<>(ResponseStatus.FAILURE, 200, "파일 다운로드 실패", entity);
    }

}
