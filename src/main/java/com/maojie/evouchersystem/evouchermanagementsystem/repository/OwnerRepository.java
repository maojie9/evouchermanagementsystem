package com.maojie.evouchersystem.evouchermanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;

public interface OwnerRepository extends JpaRepository<Owner, UUID> {

    Owner findByUserName(String userName);

}
