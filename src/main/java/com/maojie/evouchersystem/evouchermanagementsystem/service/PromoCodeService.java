package com.maojie.evouchersystem.evouchermanagementsystem.service;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PromoCode;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;

public interface PromoCodeService {
    PromoCode createPromoCode(Customer customer, VoucherList voucherList, PromoCode promoCode);

}
