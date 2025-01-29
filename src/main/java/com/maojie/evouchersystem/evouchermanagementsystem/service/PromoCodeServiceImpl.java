package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PromoCode;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.PromoCodeRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.VoucherRepository;

@RestController
public class PromoCodeServiceImpl implements PromoCodeService{
    
    @Autowired
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    private VoucherRepository voucherRepository;

    @Override
    @Transactional
    public PromoCode createPromoCode(Customer customer, VoucherList voucherList, PromoCode promoCode) {
        // Generate unique Promo code
        while(true) {
            String promoCodeString = generatePromoCode();
            List<PromoCode> isVoucherListExist = promoCodeRepository.findByPromoCode(promoCodeString);

            if(isVoucherListExist!= null && !isVoucherListExist.isEmpty()){
                continue;
            }

            PromoCode newPromoCode = new PromoCode();
            newPromoCode.setCustomer(customer);
            newPromoCode.setVoucherList(voucherList);

            newPromoCode.setPromoCode(promoCodeString);
            newPromoCode.setVouchers(promoCode.getVouchers());
            newPromoCode.setStatus(DBStatus.ACTIVE.statusCode);

            PromoCode savedPromoCode = promoCodeRepository.save(newPromoCode);

            for (Voucher voucher : newPromoCode.getVouchers()) {
                voucher.setCurrentCustomer(customer);
                voucher.setPromoCode(savedPromoCode);
                voucher.setStatus(DBStatus.ACTIVE.statusCode);
            }

            voucherRepository.saveAll(newPromoCode.getVouchers());
            return savedPromoCode;


        }
    }

    // Create a 6 Digit 5 Alphablets promo code, my intent is to use regex to generate promo code
    private String generatePromoCode() {
        StringBuilder generatedString = new StringBuilder();

        // First 6 digit
        for(int i = 0 ; i < 6; i++){
            generatedString.append((char) (new Random().nextInt(10) + '0'));
        }

        // First 5 alphablet
        for(int i = 0 ; i < 5; i++){
            if(new Random().nextBoolean()) { // Upper Case
                generatedString.append((char) (new Random().nextInt(26) + 'A'));
            } else { // Lower Case
                generatedString.append((char) (new Random().nextInt(26) + 'a'));
            }
        }

        return generatedString.toString();
    }

}
