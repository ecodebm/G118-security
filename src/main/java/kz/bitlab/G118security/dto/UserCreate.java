package kz.bitlab.G118security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreate {
    private String username;
    private String fullName;
    private String password;
    private String rePassword;
}
