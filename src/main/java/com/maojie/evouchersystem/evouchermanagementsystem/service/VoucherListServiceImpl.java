package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PaymentMethodDiscount;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.PaymentMethodDiscountRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.VoucherListRepository;

import jakarta.transaction.Transactional;

@RestController
public class VoucherListServiceImpl implements VoucherListService{
    
    @Autowired
    private VoucherListRepository voucherListRepository;

    @Autowired
    private PaymentMethodDiscountRepository paymentMethodDiscountRepository;

    @Override
    @Transactional
    public VoucherList createVoucherList(Owner owner, VoucherList voucherList) {

        VoucherList newVoucherList = new VoucherList();

        newVoucherList.setOwner(owner);

        newVoucherList.setVoucherTitle(voucherList.getVoucherTitle());
        newVoucherList.setVoucherDesctiption(voucherList.getVoucherDesctiption());
        newVoucherList.setVoucherExpiryDate(voucherList.getVoucherExpiryDate());
        newVoucherList.setVoucherImage(voucherList.getVoucherImage());
        newVoucherList.setVoucherAmount(voucherList.getVoucherAmount());
        newVoucherList.setVoucherQuantity(voucherList.getVoucherQuantity());
        newVoucherList.setVoucherLimitPerCustomer(voucherList.getVoucherLimitPerCustomer());
        newVoucherList.setVoucherGiftLimitPerCustomer(voucherList.getVoucherGiftLimitPerCustomer());

        newVoucherList.setPaymentMethodDiscounts(voucherList.getPaymentMethodDiscounts());
        newVoucherList.setStatus(DBStatus.ACTIVE.statusCode);

        VoucherList savedVoucherList = voucherListRepository.save(newVoucherList);

        for(PaymentMethodDiscount pmd : newVoucherList.getPaymentMethodDiscounts()){
            pmd.setVoucherList(savedVoucherList);
        }
        paymentMethodDiscountRepository.saveAll(newVoucherList.getPaymentMethodDiscounts());

        return savedVoucherList;

    }

    @Override
    @Transactional
    public VoucherList updateVoucherList(Owner owner, VoucherList voucherList) {
        VoucherList newVoucherList = new VoucherList();

        newVoucherList.setOwner(owner);
        newVoucherList.setId(voucherList.getId());

        newVoucherList.setVoucherTitle(voucherList.getVoucherTitle());
        newVoucherList.setVoucherDesctiption(voucherList.getVoucherDesctiption());
        newVoucherList.setVoucherExpiryDate(voucherList.getVoucherExpiryDate());
        newVoucherList.setVoucherImage(voucherList.getVoucherImage());
        newVoucherList.setVoucherAmount(voucherList.getVoucherAmount());
        newVoucherList.setVoucherQuantity(voucherList.getVoucherQuantity());
        newVoucherList.setVoucherLimitPerCustomer(voucherList.getVoucherLimitPerCustomer());
        newVoucherList.setVoucherGiftLimitPerCustomer(voucherList.getVoucherGiftLimitPerCustomer());

        newVoucherList.setPaymentMethodDiscounts(voucherList.getPaymentMethodDiscounts());
        newVoucherList.setStatus(DBStatus.ACTIVE.statusCode);

        VoucherList savedVoucherList = voucherListRepository.save(newVoucherList);

        for(PaymentMethodDiscount pmd : newVoucherList.getPaymentMethodDiscounts()){
            pmd.setVoucherList(savedVoucherList);
        }
        paymentMethodDiscountRepository.saveAll(newVoucherList.getPaymentMethodDiscounts());
        
        return savedVoucherList;
    }

    @Override
    public List<VoucherList> retrieveVoucherListByOwner(Owner owner) {
        return voucherListRepository.findByOwner(owner);
    }

    @Override
    public VoucherList removeVoucherList(Owner owner, VoucherList voucherList) {
        voucherList.setOwner(owner);
        voucherList.setStatus(DBStatus.INACTIVE.statusCode);
        return voucherListRepository.save(voucherList);

    }


}
