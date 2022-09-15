package spring.board.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// 사용자
public class User {

    // 사용자 번호
    private Integer idx;

    // 아이디
    private String id;

    // 이름
    private String name;

    // 비밀번호
    private String password;

    // 이메일
    private String email;
}
