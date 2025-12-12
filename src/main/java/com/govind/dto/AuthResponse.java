package com.govind.dto;

import com.govind.models.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor   
@NoArgsConstructor
public class AuthResponse {

    private String token;
    private Role role;

}
