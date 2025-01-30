package com.maojie.evouchersystem.evouchermanagementsystem.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PromoCode;

public interface PromoCodeRepository extends JpaRepository<PromoCode, UUID> {

    List<PromoCode> findByPromoCode(String promoCodeString);
    List<PromoCode> findBySorder(Sorder sorder);
}