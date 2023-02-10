package com.sample.freeboard.domain.account.service;

import com.sample.freeboard.domain.account.dto.AccountTypes;
import com.sample.freeboard.domain.account.dto.AccountTypeDTO;
import com.sample.freeboard.domain.account.domain.TypeEnum;
import com.sample.freeboard.domain.account.domain.AccountType;
import com.sample.freeboard.global.errorhandler.exception.BadRequestException;
import com.sample.freeboard.global.errorhandler.exception.NotFoundException;
import com.sample.freeboard.domain.account.repository.AccountTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.sample.freeboard.global.constant.Message.DENY_REQUEST;
import static com.sample.freeboard.global.constant.Message.NOT_EXIST_ACCOUNT_TYPE;

@Service
@AllArgsConstructor
public class AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;
    private final int NOT_EXIST_ACCOUNT_TYPE_LENGTH = 0;

    public void setAccountTypes() {
        isExistAccountTypes();
        List<AccountType> accountTypes = setNewAccoutTypeList();

        accountTypeRepository.saveAll(accountTypes);
    }

    public AccountType getAccountTypeByTypeName(String typeName) {
        AccountType findAccount = accountTypeRepository.findByTypeName(typeName);

        if (Objects.isNull(findAccount)) {
            throw new BadRequestException(NOT_EXIST_ACCOUNT_TYPE);
        }

        return findAccount;
    }

    public AccountTypes getAccountTypes() {
        List<AccountTypeDTO> accountTypes = new ArrayList<>();
        accountTypeRepository.findAll()
                .forEach(accountType -> accountTypes.add(new AccountTypeDTO(accountType.getTypeId(), accountType.getTypeName())));
        return new AccountTypes(accountTypes);
    }

    private List<AccountType> setNewAccoutTypeList() {
        AccountType lessee = new AccountType(TypeEnum.LESSEE.name());
        AccountType lessor = new AccountType(TypeEnum.LESSOR.name());
        AccountType realtor = new AccountType(TypeEnum.REALTOR.name());

        List<AccountType> accountTypes = new ArrayList<>();
        accountTypes.add(lessee);
        accountTypes.add(lessor);
        accountTypes.add(realtor);

        return accountTypes;
    }

    private void isExistAccountTypes() {
        List<AccountType> findAll = accountTypeRepository.findAll();
        if (Objects.isNull(findAll) || findAll.size() == NOT_EXIST_ACCOUNT_TYPE_LENGTH) {
            return;
        }

        throw new NotFoundException(DENY_REQUEST);
    }
}
