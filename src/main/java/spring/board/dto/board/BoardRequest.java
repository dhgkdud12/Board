package spring.board.dto.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardRequest {

    private int bId;

    @NotEmpty(message = "제목을 입력해주세요.")
    @Size(min = 2, max = 45, message = "제목은 45자 이내로 작성해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 2, max = 3000, message = "내용은 3000자 이내로 작성해주세요.")
    private String content;
    private MultipartFile file;
}

