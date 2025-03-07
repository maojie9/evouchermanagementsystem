package com.maojie.evouchersystem.evouchermanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.config.JwtProvider;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.UserType;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.response.AuthResponse;
import com.maojie.evouchersystem.evouchermanagementsystem.service.CustomerDetailsService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.CustomerService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.OwnerDetailsService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.OwnerService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomerDetailsService customerDetailsService;
    @Autowired
    private OwnerDetailsService ownerDetailsService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OwnerService ownerService;

    @PostMapping("/signup/customer")
    public ResponseEntity<AuthResponse> registerCustomer(@RequestBody Customer customer) throws Exception{ 
        Customer newCustomer = customerService.createCustomer(customer);

        Authentication auth = new UsernamePasswordAuthenticationToken(newCustomer.getMobileNoString(), newCustomer.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateCustomerToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Register success for the customer");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
    
    @PostMapping("/signup/owner")
    public ResponseEntity<AuthResponse> registerOwner(@RequestBody Owner owner) throws Exception{
        Owner newOwner = ownerService.createOwner(owner);

        Authentication auth = new UsernamePasswordAuthenticationToken(newOwner.getUserName(), newOwner.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateOwnerToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Register success for the owner");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signin/customer")
    public ResponseEntity<AuthResponse> loginCustomer(@RequestBody Customer customer) throws Exception{ 
        Authentication auth = autenticate(UserType.CUSTOMER, customer.getMobileNoString(), customer.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateCustomerToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login success for the customer");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }
    
    @PostMapping("/signin/owner")
    public ResponseEntity<AuthResponse> loginOwner(@RequestBody Owner owner) throws Exception{ 
        Authentication auth = autenticate(UserType.OWNER, owner.getUserName(), owner.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateOwnerToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login success for the owner");

        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    private Authentication autenticate(UserType userType, String userName, String password) throws Exception {
        UserDetails userDetails;

        switch(userType) {
            case CUSTOMER:
                try {
                    userDetails = customerDetailsService.loadUserByUsername(userName);
                    if(userDetails == null || (!userDetails.getPassword().equals(password))) {
                        throw new Exception("Invalid mobile number or password, unable to sign in");
                    }
                } catch (Exception e) {
                    throw new BadCredentialsException("Invalid mobile number or password, unable to sign in");
                }
                break;
            case OWNER:
                try {
                    userDetails = ownerDetailsService.loadUserByUsername(userName);
                    if(userDetails == null || (!userDetails.getPassword().equals(password))) {
                        throw new BadCredentialsException("Invalid username or password, unable to sign in");
                    }
                } catch (Exception e) {
                    throw new BadCredentialsException("Invalid username or password, unable to sign in");
                }
                break;
            default:
              throw new Exception("Unknown user type, unable to sign in");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        
    }

}
