package spring.board.domain;

import lombok.*;
import spring.board.dto.user.UserRequest;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
    private Timestamp joinDate;

    public User(UserRequest userRequest) {
        this.id = userRequest.getId();
        this.name = userRequest.getName();
        this.password = userRequest.getPassword();
        this.password = userRequest.getPassword();
        this.email = userRequest.getEmail();
        this.joinDate = new Timestamp(new Date().getTime());
    }
}
