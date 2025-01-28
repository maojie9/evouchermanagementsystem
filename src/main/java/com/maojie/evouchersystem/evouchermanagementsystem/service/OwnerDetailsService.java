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

import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.OwnerRepository;

@Service
public class OwnerDetailsService implements UserDetailsService {

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Owner owner = ownerRepository.findByUserName(username);

        if(owner == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorityList = new ArrayList<>();

        return new User(owner.getUserName(), owner.getPassword(), authorityList );

    }

}
