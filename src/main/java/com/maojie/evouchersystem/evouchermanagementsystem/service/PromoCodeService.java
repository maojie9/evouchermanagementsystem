package com.maojie.evouchersystem.evouchermanagementsystem.service;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;

public interface PromoCodeService {
   Boolean createPromoCode(Customer customer, Sorder order);

}
