package com.maojie.evouchersystem.evouchermanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maojie.evouchersystem.evouchermanagementsystem.model.PaymentMethodDiscount;

public interface PaymentMethodDiscountRepository extends JpaRepository<PaymentMethodDiscount, UUID> {

}
