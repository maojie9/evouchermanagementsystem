package com.maojie.evouchersystem.evouchermanagementsystem.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Customer findByMobileNoString(String mobileNoString);

}
