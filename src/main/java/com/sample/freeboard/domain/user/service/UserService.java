package com.sample.freeboard.domain.user.service;

import com.sample.freeboard.domain.account.domain.AccountType;
import com.sample.freeboard.domain.account.service.AccountTypeService;
import com.sample.freeboard.domain.user.dto.UserRequest;
import com.sample.freeboard.global.constant.Message;
import com.sample.freeboard.global.errorhandler.exception.BadRequestException;
import com.sample.freeboard.domain.user.dto.UserDTO;
import com.sample.freeboard.domain.user.domain.User;
import com.sample.freeboard.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AccountTypeService accountTypeService;
    private final int MAX_ID_LENGTH = 20;
    private final String EMPTY_SAPCE_STRING = " ";
    private final int ACCOUNT_TYPE_INDEX = 0;

    public User findUserByAccountId(String accountId) {
        return userRepository.findByAccountId(accountId);
    }

    public UserDTO joinUser(UserRequest userRequest) {
        User newUser = getNewUser(userRequest.getTypeName(), userRequest.getAccountId(), userRequest.getNickname());
        return createNewUser(newUser);
    }

    private UserDTO createNewUser(User newUser) {
        User user = userRepository.save(newUser);
        return new UserDTO(user.getUserId(), user.getNickname());
    }

    private User getNewUser(String typeName, String accountId, String nickname) {
        AccountType accountType = accountTypeService.getAccountTypeByTypeName(typeName);

        isValidateJoin(typeName, accountId, nickname);

        return new User(nickname, accountId, accountType);
    }

    private void isValidateJoin(String accountType, String accountId, String nickname) {
        if (isValuableJoin(accountType, accountId, nickname)) {
            return;
        }

        throw new BadRequestException(Message.BAD_REQUEST_JOIN_USER);
    }

    private boolean isValuableJoin(String accountType, String accountId, String nickname) {
        return isValuableAccountId(accountId)
                && isEqualTypeCompareAccountId(accountType, accountId)
                && isValuableNickname(nickname);
    }

    private boolean isValuableNickname(String nickname) {
        return StringUtils.hasText(nickname) && isAdmittedId(nickname) && isUniqueNickname(nickname);
    }

    private boolean isValuableAccountId(String accountId) {
        return StringUtils.hasText(accountId) && isAdmittedId(accountId) && isUniqueAccountId(accountId);
    }

    private boolean isEqualTypeCompareAccountId(String accountType, String accountId) {
        String[] accountIdSplit = accountId.split(EMPTY_SAPCE_STRING);
        return accountIdSplit[ACCOUNT_TYPE_INDEX].toUpperCase().equals(accountType);
    }

    private boolean isUniqueNickname(String nickname) {
        return Objects.isNull(userRepository.findByNickname(nickname));
    }

    private boolean isUniqueAccountId(String accountId) {
        return Objects.isNull(userRepository.findByAccountId(accountId));
    }

    private boolean isAdmittedId(String id) {
        return id.length() < MAX_ID_LENGTH;
    }

}
