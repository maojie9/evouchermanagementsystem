package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.config.JwtProvider;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.CustomerRepository;

@RestController
public class CustomerServiceImpl implements CustomerService{

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer findCustomerByJwt(String jwt) throws Exception {
        String mobileNoString = JwtProvider.getMobielNoStringFromToken(jwt);
        Customer customer = customerRepository.findByMobileNoString(mobileNoString);

        if(customer == null || DBStatus.valueOfStatusCode(customer.getStatus()) != DBStatus.ACTIVE) {
            throw new Exception("Customer not found");
        }

        return customer;
    }

    @Override
    public Customer findCustomerById(UUID id) throws Exception {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty() || DBStatus.valueOfStatusCode(customer.get().getStatus()) != DBStatus.ACTIVE){
            throw new Exception("Customer not found");
        }
        return customer.get();
    }

    @Override
    public Customer updatePassword(Customer customer, String newPassword) {
        customer.setPassword(newPassword);
        return customerRepository.save(customer);
    }


}
