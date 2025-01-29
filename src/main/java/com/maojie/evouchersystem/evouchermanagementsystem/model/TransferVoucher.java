package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class TransferVoucher implements Serializable {
    private UUID voucherId;
    private String newCustomerBasedOnMobileNoString;
}
