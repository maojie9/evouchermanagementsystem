package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.Data;

@Data
public class AfterPurchaseVoucher implements Serializable {
    private UUID voucherId;
    private int numberOfVoucherPurchased;
}
