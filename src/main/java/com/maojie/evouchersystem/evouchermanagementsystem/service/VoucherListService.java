package com.maojie.evouchersystem.evouchermanagementsystem.service;

import java.util.List;
import java.util.UUID;

import com.maojie.evouchersystem.evouchermanagementsystem.model.Owner;
import com.maojie.evouchersystem.evouchermanagementsystem.model.VoucherList;

public interface VoucherListService {

    VoucherList createVoucherList(Owner owner, VoucherList voucherList) throws Exception;
    VoucherList updateVoucherList(Owner owner, VoucherList voucherList) throws Exception;

    List<VoucherList> retrieveVoucherListByOwner(Owner owner);
    VoucherList removeVoucherList(VoucherList voucherList, Owner owner) throws Exception;

    List<VoucherList> retrieveActiveVoucherListForCustomer();
    VoucherList retrieveVoucherListById(UUID voucherListId) throws Exception;

}
