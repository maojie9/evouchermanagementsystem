package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.CustomerRepository;

@Service
public class CustomerDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String mobileNoString) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByMobileNoString(mobileNoString);

        if(customer == null) {
            throw new UsernameNotFoundException(mobileNoString);
        }

        List<GrantedAuthority> authorityList = new ArrayList<>();

        return new User(customer.getMobileNoString(), customer.getPassword(), authorityList );

    }

}
