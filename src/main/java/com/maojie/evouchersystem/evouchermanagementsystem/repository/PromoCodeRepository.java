package com.maojie.evouchersystem.evouchermanagementsystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PromoCode;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;

public interface PromoCodeRepository extends JpaRepository<PromoCode, UUID> {
    List<PromoCode> findByPromoCode(String promoCodeString);
    List<PromoCode> findByVoucherListAndCustomer(VoucherList voucherList, Customer customer);
    List<PromoCode> findByVoucherList(VoucherList voucherList);
}