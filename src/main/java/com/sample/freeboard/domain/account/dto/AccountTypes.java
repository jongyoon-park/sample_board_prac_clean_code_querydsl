package com.sample.freeboard.domain.account.dto;

import com.sample.freeboard.global.collection.FirstClassCollection;
import lombok.Getter;

import java.util.List;

@Getter
public class AccountTypes implements FirstClassCollection {
    private List<AccountTypeDTO> accountTypeList;

    public AccountTypes(List<AccountTypeDTO> accountTypeList) {
        this.accountTypeList = accountTypeList;
    }

    @Override
    public int getCount() {
        return accountTypeList.size();
    }
}
