package com.sample.freeboard.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest extends UserDTO {
    private String typeName;
    private String accountId;
}
