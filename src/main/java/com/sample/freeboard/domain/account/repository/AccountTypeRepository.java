package com.sample.freeboard.domain.account.repository;

import com.sample.freeboard.domain.account.domain.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTypeRepository extends JpaRepository<AccountType, Long> {
    AccountType findByTypeName(String typeName);
}
