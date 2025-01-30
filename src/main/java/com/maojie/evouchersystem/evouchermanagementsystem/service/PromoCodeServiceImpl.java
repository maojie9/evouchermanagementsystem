package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.maojie.evouchersystem.evouchermanagementsystem.domain.DBStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.domain.OrderStatus;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Customer;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Sorder;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PaymentMethodDiscount;
import com.maojie.evouchersystem.evouchermanagementsystem.model.PromoCode;
import com.maojie.evouchersystem.evouchermanagementsystem.model.Voucher;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.OrderRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.PromoCodeRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.VoucherListRepository;
import com.maojie.evouchersystem.evouchermanagementsystem.repository.VoucherRepository;

@RestController
public class PromoCodeServiceImpl implements PromoCodeService{
    
    @Autowired
    private PromoCodeRepository promoCodeRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private VoucherListRepository voucherListRepository;
    @Autowired
    private OrderRepository orderRepository;
    
    @Override
    @Transactional
    public Boolean createPromoCode(Customer customer, Sorder order) {
        // Generate unique Promo code
        while(true) {
            String promoCodeString = generatePromoCode();
            List<PromoCode> isVoucherListExist = promoCodeRepository.findByPromoCode(promoCodeString);

            if(isVoucherListExist!= null && !isVoucherListExist.isEmpty()){
                continue;
            }

            PromoCode newPromoCode = new PromoCode();
            newPromoCode.setSorder(order);
            newPromoCode.setPromoCode(promoCodeString);

            Optional<VoucherList> assignedVoucherList = voucherListRepository.findById(order.getPaymentMethodDiscount().getVoucherList().getId());

            if(assignedVoucherList.isEmpty() || 
                assignedVoucherList.get().getStatus() != DBStatus.ACTIVE ||
                assignedVoucherList.get().getVoucherExpiryDate().compareTo(new Date()) < 0) {
                    order.setReason("Unable to obtain the voucher to process payment");
                    order.setStatus(OrderStatus.REJECT);
                    orderRepository.save(order);
                    return false;
            }

            // Obtain number of vouchers are purchased before by the customer
            int numberOfVoucherAlreadyPurchasedByCustomer = 0;
            int numberOfVoucherAlreadyPurchased = 0;

            List<PaymentMethodDiscount> paymentMethodDiscounts = assignedVoucherList.get().getPaymentMethodDiscounts();

            for(PaymentMethodDiscount pmd : paymentMethodDiscounts) {
                List <Sorder> numberOfOrdersPurchasedByCustomer = orderRepository.findByPaymentMethodDiscountAndStatusAndCustomer(pmd, OrderStatus.ACCEPTED, customer);
                for(Sorder o : numberOfOrdersPurchasedByCustomer) {
                    numberOfVoucherAlreadyPurchasedByCustomer = numberOfVoucherAlreadyPurchasedByCustomer + o.getNoOfVoucher();
                }
                
                List <Sorder> numberOfOrdersPurchased = orderRepository.findByPaymentMethodDiscountAndStatus(pmd, OrderStatus.ACCEPTED);
                for(Sorder o : numberOfOrdersPurchased) {
                    numberOfVoucherAlreadyPurchased = numberOfVoucherAlreadyPurchased + o.getNoOfVoucher();
                }
            }

            if(numberOfVoucherAlreadyPurchasedByCustomer + order.getNoOfVoucher() > assignedVoucherList.get().getVoucherLimitPerCustomer()) {
                order.setReason("Unable to process the transaction due to number of voucher customer allocated is more than the number of vouchers customer can buy.");
                order.setStatus(OrderStatus.REJECT);
                orderRepository.save(order);
                return false;
            }

            // Obtaim the total number of voucher purchased
            if(numberOfVoucherAlreadyPurchased + order.getNoOfVoucher() > assignedVoucherList.get().getVoucherQuantity()) {
                order.setReason("Unable to process the transaction due to vouchers had been sold out. Money will be refunded");
                order.setStatus(OrderStatus.REJECT);
                orderRepository.save(order);
                return false;
            }

            // Generate new vouchers 
            order.setStatus(OrderStatus.ACCEPTED);
            orderRepository.save(order);
            PromoCode savedPromoCode = promoCodeRepository.save(newPromoCode);

            List<Voucher> vouchers = new ArrayList<>();
            for(int i = 0; i < order.getNoOfVoucher(); i++) {
                Voucher voucher = new Voucher();
                voucher.setCurrentCustomer(customer);
                voucher.setPromoCode(savedPromoCode);
                voucher.setStatus(DBStatus.ACTIVE);
                vouchers.add(voucher);
            }

            voucherRepository.saveAll(vouchers);
            return true;
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
