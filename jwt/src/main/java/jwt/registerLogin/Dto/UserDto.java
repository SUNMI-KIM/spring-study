package jwt.registerLogin.Dto;

import jwt.registerLogin.Domain.User;
import jwt.registerLogin.Domain.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String email;
    private UserRole role;

    public UserDto(User user) {
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
