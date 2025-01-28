package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.CustomerRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.OwnerRepository;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @PostMapping("/signup/customer")
    public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) throws Exception{ 
        Customer isCustomerExist = customerRepository.findByMobileNoString(customer.getMobileNoString());

        if(isCustomerExist != null) {
            throw new Exception("This customer is exist, please login instead");
        }
        
        Customer newCustomer = new Customer();

        newCustomer.setMobileNoString(customer.getMobileNoString());
        newCustomer.setPassword(customer.getPassword());

        Customer savedCustomer = customerRepository.save(newCustomer);

        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
    
    @PostMapping("/signup/owner")
    public ResponseEntity<Owner> registerOwner(@RequestBody Owner owner) throws Exception{
        Owner isOwnerExist = ownerRepository.findByUserName(owner.getUserName());

        if(isOwnerExist != null) {
            throw new Exception("This owner is exist, please login instead");
        }

        Owner newOwner = new Owner();

        newOwner.setUserName(owner.getUserName());
        newOwner.setPassword(owner.getPassword());

        Owner savedOwner = ownerRepository.save(newOwner);

        return new ResponseEntity<>(savedOwner, HttpStatus.CREATED);
    }

}
