package jwt.registerLogin.Domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private String email;
    private String passWord;
    private UserRole role;

    public User(String email, String passWord, UserRole role) {
        this.email = email;
        this.passWord = passWord;
        this.role = role;
    }

    public void hashPassWord(PasswordEncoder passwordEncoder) {
        this.passWord = passwordEncoder.encode(this.passWord);
    }

    public boolean checkPassWord(String plainPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(plainPassword, this.passWord);
    }
}
