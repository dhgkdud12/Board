package spring.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {

    private Integer fileNo;
    private Integer boardNo;

    // 원본 파일명
    private String fileName;
    // 변환 파일명
    private String convertName;
    // 파일 경로
    private String path;
    // 파일 확장자
    private String extension;
    //파일 크기
    private Long size;

}
