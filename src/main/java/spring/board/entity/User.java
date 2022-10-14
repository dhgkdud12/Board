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
    private Integer idx;
    private String id;
    private String name;
    private String password;
    private String email;
}
