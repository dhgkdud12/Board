package spring.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {
    private int id;
    private String title;
    private String content;
    private MultipartFile file;
}

