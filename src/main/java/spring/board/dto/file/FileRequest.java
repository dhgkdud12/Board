package spring.board.dto.file;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FileRequest {
    private Integer fileNo;
    private Integer boardNo;
    private String fileName;
    private String convertName;
    private String path;
    private String extension;
    private Long size;
}
