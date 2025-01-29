package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.UUID;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;

public interface CustomerService {
    public Customer findCustomerByJwt(String jwt) throws Exception;
    public Customer findCustomerById(UUID id) throws Exception;

    Customer createCustomer(Customer customer) throws Exception;
    Customer updatePassword(Customer customer, String newPassword);
    Customer updateName(Customer customer, String newName);

}
