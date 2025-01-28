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
import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.UserType;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.CustomerRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.OwnerRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.response.AuthResponse;
import com.maojie.evouchersystem.evouchermanagementsystem.service.CustomerDetailsService;
import com.maojie.evouchersystem.evouchermanagementsystem.service.OwnerDetailsService;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private CustomerDetailsService customerDetailsService;
    @Autowired
    private OwnerDetailsService ownerDetailsService;

    @PostMapping("/signup/customer")
    public ResponseEntity<AuthResponse> registerCustomer(@RequestBody Customer customer) throws Exception{ 
        Customer isCustomerExist = customerRepository.findByMobileNoString(customer.getMobileNoString());

        if(isCustomerExist != null) {
            throw new Exception("This customer is exist, please login instead");
        }
        
        Customer newCustomer = new Customer();

        newCustomer.setMobileNoString(customer.getMobileNoString());
        newCustomer.setPassword(customer.getPassword());
        newCustomer.setStatus(DBStatus.ACTIVE.statusCode);

        customerRepository.save(newCustomer);

        Authentication auth = new UsernamePasswordAuthenticationToken(customer.getMobileNoString(), customer.getPassword());
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
        Owner isOwnerExist = ownerRepository.findByUserName(owner.getUserName());

        if(isOwnerExist != null) {
            throw new Exception("This owner is exist, please login instead");
        }

        Owner newOwner = new Owner();

        newOwner.setUserName(owner.getUserName());
        newOwner.setPassword(owner.getPassword());
        newOwner.setStatus(DBStatus.ACTIVE.statusCode);

        ownerRepository.save(newOwner);

        Authentication auth = new UsernamePasswordAuthenticationToken(owner.getUserName(), owner.getPassword());
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
