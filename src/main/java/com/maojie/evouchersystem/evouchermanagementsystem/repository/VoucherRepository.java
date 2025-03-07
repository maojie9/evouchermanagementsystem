package com.maojie.evouchersystem.evouchermanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PromoCode;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;
import java.util.List;


public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
    List<Voucher> findByCurrentCustomerNotAndPromoCode(Customer currentCustomer, PromoCode promoCode);
    List<Voucher> findByCurrentCustomerAndPromoCode(Customer currentCustomer, PromoCode promoCode);
    List<Voucher> findByPromoCode(PromoCode promoCode);
}
