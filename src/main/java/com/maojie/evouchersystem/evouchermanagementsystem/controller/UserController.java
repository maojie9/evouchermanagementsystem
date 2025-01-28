package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.service.CustomerService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.OwnerService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    OwnerService ownerService;

    @GetMapping("/api/customers/profile")
    public ResponseEntity<Customer> getCustomerProfile(@RequestHeader("Authorization") String jwt) throws Exception{
        Customer customer = customerService.findCustomerByJwt(jwt);

        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    @GetMapping("/api/owners/profile")
    public ResponseEntity<Owner> getOwnerProfile(@RequestHeader("Authorization") String jwt) throws Exception{
        Owner owner = ownerService.findOwnerByJwt(jwt);

        return new ResponseEntity<Owner>(owner, HttpStatus.OK);
    }



}
