package com.sample.freeboard.domain.account.domain;

import com.sample.freeboard.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.sample.freeboard.domain.account.domain.TypeEnum.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity(name = "account_type")
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class AccountType {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "type_id")
    private Long typeId;
    @Column(name = "type_name")
    private String typeName;

    public AccountType(String typeName) {
        this.typeName = typeName;
    }

    public String convertTypeByKorean() {
        if (!StringUtils.hasText(typeName)) {
            return "";
        }

        if (typeName.equals(LESSEE.name())) {
            return LESSEE.getKorean();
        }

        if (typeName.equals(LESSOR.name())) {
            return LESSOR.getKorean();
        }

        return REALTOR.getKorean();
    }

    @OneToMany(mappedBy = "accountType")
    private List<User> userList = new ArrayList<>();
}