package com.sample.freeboard.domain.account.domain;

public enum TypeEnum {
    LESSOR("임대인"),
    LESSEE("임차인"),
    REALTOR("공인중개사");

    private final String korean;
    public String getKorean() {
        return korean;
    }

    TypeEnum(String korean) {
        this.korean = korean;
    }



}
