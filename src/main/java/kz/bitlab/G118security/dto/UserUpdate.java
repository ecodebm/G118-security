package kz.bitlab.G118security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdate {
    private Long id;
    private String fullName;
    private String currentPassword;
    private String newPassword;
    private String reNewPassword;
}
