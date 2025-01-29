package com.maojie.evouchersystem.evouchermanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import java.util.List;


public interface VoucherListRepository extends JpaRepository<VoucherList, UUID> {

    List<VoucherList> findByOwner(Owner owner);
    List<VoucherList> findByVoucherTitle(String voucherTitle);
    List<VoucherList> findByStatus(int status);
}
