package ru.egorov.userservice.dto;

import lombok.Data;

@Data
public class UserDTO {

    private String username;
    private String password;
    private String confirmPassword;

}
