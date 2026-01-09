package com.pavan.jwtDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignInResponseDto {
    private Long id;
    private String userName;
    private String email;
    private String password;

    private String jwtToken;
}
