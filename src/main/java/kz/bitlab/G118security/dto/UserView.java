package kz.bitlab.G118security.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserView {
    private Long id;
    private String username;
    private String name;
    private LocalDate birthDate;
    private String phoneNumber;
}
