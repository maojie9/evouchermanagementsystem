package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;
import java.util.UUID;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;

public interface VoucherService {
    List<Voucher> retrieveVoucherListByCurrentCustomer(Customer customer);
    Voucher changeCustomer(Voucher voucher, Customer currentCustomer, Customer newCustomer) throws Exception;
    Voucher voucherUsed(Voucher voucher) throws Exception;
    Voucher retrieveVoucherById(UUID voucherId);

}
