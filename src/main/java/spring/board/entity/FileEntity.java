package spring.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {

    // 파일 번호
    private Integer fileNo;

    // 게시물 번호
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
