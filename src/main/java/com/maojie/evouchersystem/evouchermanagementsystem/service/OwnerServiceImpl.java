package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.config.JwtProvider;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.OwnerRepository;

@RestController
public class OwnerServiceImpl implements OwnerService{

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public Owner findOwnerByJwt(String jwt) throws Exception {
        String userName = JwtProvider.getUserNameFromToken(jwt);
        Owner owner = ownerRepository.findByUserName(userName);

        if(owner == null || owner.getStatus() != DBStatus.ACTIVE) {
            throw new Exception("Username not found");
        }

        return owner;

    }

    @Override
    public Owner findOwnerByID(UUID id) throws Exception {
         Optional<Owner> owner = ownerRepository.findById(id);
        if(owner.isEmpty() || owner.get().getStatus() != DBStatus.ACTIVE){
            throw new Exception("Username not found");
        }
        return owner.get();
    }

    @Override
    public Owner createOwner(Owner owner) throws Exception {
        Owner isOwnerExist = ownerRepository.findByUserName(owner.getUserName());
        if(isOwnerExist != null) {
            throw new Exception("This owner is exist, please login instead");
        }

        Owner newOwner = new Owner();

        newOwner.setUserName(owner.getUserName());
        newOwner.setPassword(owner.getPassword());
        newOwner.setStatus(DBStatus.ACTIVE);

        return ownerRepository.save(newOwner);
    }

    @Override
    public Owner updatePassword(Owner owner, String newPassword) {
        owner.setPassword(newPassword);
        return ownerRepository.save(owner);

    }

   

}
