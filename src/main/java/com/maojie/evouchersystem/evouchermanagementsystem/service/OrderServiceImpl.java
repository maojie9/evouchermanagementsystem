package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.OrderRepository;

@RestController
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Sorder> findOrderByCustomer(Customer customer) {
        return orderRepository.findByCustomer(customer);
    }

    @Override
    public Sorder findOrderById(UUID id) throws Exception {
        return orderRepository.findById(id).orElseThrow(() -> new Exception ("Unable to find the order"));
    }


}
