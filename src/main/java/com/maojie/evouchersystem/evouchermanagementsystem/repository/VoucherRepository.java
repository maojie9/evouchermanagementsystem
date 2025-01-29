package com.maojie.evouchersystem.evouchermanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;

public interface VoucherRepository extends JpaRepository<Voucher, UUID> {
}
