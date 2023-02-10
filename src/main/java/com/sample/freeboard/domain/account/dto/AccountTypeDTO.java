package com.sample.freeboard.domain.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountTypeDTO {
    private Long typeId;
    private String typeName;
}
