package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.UUID;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;

public interface OwnerService {
    public Owner findOwnerByJwt(String jwt) throws Exception;
    public Owner findOwnerByID(UUID id) throws Exception;

    Owner updatePassword(Owner customer, String newPassword);

}
