package com.maojie.evouchersystem.evouchermanagementsystem.model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class OrderHistory implements Serializable {
    List<Voucher> mainVoucher;
    List<Voucher> voucherFromOtherCustomer;

}