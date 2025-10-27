package track.app.model.dto;

import lombok.*;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private String userName;
    private String email;
    private String password;
}