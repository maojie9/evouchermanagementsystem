package com.maojie.evouchersystem.evouchermanagementsystem.response;

import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private boolean status; // This stated is not related to the database status
    private String message;
    private String session;

}
