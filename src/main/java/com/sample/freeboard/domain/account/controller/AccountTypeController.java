package com.sample.freeboard.domain.account.controller;

import com.sample.freeboard.domain.account.service.AccountTypeService;
import com.sample.freeboard.global.annotation.AnyoneCallable;
import com.sample.freeboard.domain.account.dto.AccountTypes;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("sample/account")
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    @PostMapping("")
    @AnyoneCallable
    public void setAccountTypes() {
        accountTypeService.setAccountTypes();
    }

    @GetMapping
    @AnyoneCallable
    public AccountTypes getAccountTypes() {
        return accountTypeService.getAccountTypes();
    }
}
