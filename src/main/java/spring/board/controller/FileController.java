package spring.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.board.common.response.exception.ErrorCode;
import spring.board.common.response.exception.TicketingException;
import spring.board.common.response.CommonResponse;
import spring.board.common.response.ResponseStatus;
import spring.board.common.response.SuccessMessage;
import spring.board.service.file.FTPFileService;

@RestController
@RequestMapping("/file")
public class FileController {
    private final FTPFileService fileService;

    public FileController(FTPFileService fileService) {
        this.fileService = fileService;
    }

    // 파일 다운로드
    @GetMapping("/download/{fIdx}")
    public CommonResponse download(@PathVariable("fIdx") int fIdx) {
        ResponseEntity<Object> entity = fileService.downloadFilefromFTP(fIdx);
        if (entity.getStatusCode().value() == 200) {
            return new CommonResponse<>(ResponseStatus.SUCCESS, 200, SuccessMessage.SUCCESS_FILE_DOWN.getMessage(), entity);
        }
        else
            throw new TicketingException(ErrorCode.FAIL_FILE_DOWN);
    }
}
