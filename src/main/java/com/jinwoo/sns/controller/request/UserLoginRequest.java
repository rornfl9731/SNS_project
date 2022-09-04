package com.jinwoo.sns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserLoginRequest {
    private String userName;
    private String password;
}
