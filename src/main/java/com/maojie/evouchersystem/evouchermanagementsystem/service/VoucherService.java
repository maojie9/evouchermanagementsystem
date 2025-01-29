package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;

public interface VoucherService {
    List<Voucher> retrieveVoucherListByCurrentCustomer(Customer customer);
    Voucher changeCustomer(Voucher voucher, Customer newCustomer);
    Voucher voucherUsed(Voucher voucher);

}
