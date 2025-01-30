package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;
import java.util.UUID;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;

public interface OrderService {
    List<Sorder> findOrderByCustomer(Customer customer);
    Sorder findOrderById(UUID id) throws Exception;


}
