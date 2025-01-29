package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;

public interface VoucherListService {

    VoucherList createVoucherList(Owner owner, VoucherList voucherList);
    VoucherList updateVoucherList(Owner owner, VoucherList voucherList);

    List<VoucherList> retrieveVoucherListByOwner(Owner owner);
    VoucherList removeVoucherList(Owner owner, VoucherList voucherList);

}
