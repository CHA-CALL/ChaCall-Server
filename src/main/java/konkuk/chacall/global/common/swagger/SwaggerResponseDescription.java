package konkuk.chacall.global.common.swagger;

import konkuk.chacall.global.common.exception.code.ErrorCode;
import lombok.Getter;

import java.util.LinkedHashSet;
import java.util.Set;

import static konkuk.chacall.global.common.exception.code.ErrorCode.*;

@Getter
public enum SwaggerResponseDescription {
//
    //Auth
    LOGIN(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    LOGOUT(new LinkedHashSet<>(Set.of(

    ))),
    ISSUE_TOKEN(new LinkedHashSet<>(Set.of(
            AUTH_INVALID_LOGIN_TOKEN_KEY
    ))),

    // User
    GET_USER_INFO(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    UPDATE_USER_INFO(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),

    // Owner
    OWNER_REGISTER_BANK_ACCOUNT(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            BANK_ACCOUNT_ALREADY_EXISTS_FOR_USER,
            BANK_ACCOUNT_ALREADY_EXISTS
    ))),
    OWNER_GET_BANK_ACCOUNT(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    OWNER_UPDATE_BANK_ACCOUNT(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            BANK_ACCOUNT_NOT_FOUND,
            BANK_ACCOUNT_ALREADY_EXISTS,
            BANK_ACCOUNT_FORBIDDEN
    ))),
    OWNER_DELETE_BANK_ACCOUNT(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            BANK_ACCOUNT_NOT_FOUND,
            BANK_ACCOUNT_FORBIDDEN
    ))),
    OWNER_REGISTER_CHAT_TEMPLATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    OWNER_GET_CHAT_TEMPLATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    OWNER_UPDATE_CHAT_TEMPLATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            CHAT_TEMPLATE_NOT_FOUND
    ))),
    OWNER_DELETE_CHAT_TEMPLATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            CHAT_TEMPLATE_NOT_FOUND
    ))),
    OWNER_GET_RESERVATION_HISTORY(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND
    ))),
    OWNER_GET_RESERVATION_DETAIL(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            RESERVATION_NOT_FOUND
    ))),


    // Member
    MEMBER_SAVED_FOOD_TRUCK(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            FOOD_TRUCK_NOT_FOUND,
            SAVED_FOOD_TRUCK_ALREADY_EXIST,
            SAVED_FOOD_TRUCK_NOT_FOUND
    ))),
    MEMBER_RATING(new LinkedHashSet<>(Set.of(
            FOOD_TRUCK_NOT_FOUND,
            RATING_NOT_FOUND,
            RATING_INVALID_SCORE,
            RATING_ALREADY_REGISTERED
    )))
  ;
    private final Set<ErrorCode> errorCodeList;

    SwaggerResponseDescription(Set<ErrorCode> errorCodeList) {
        // 공통 에러
        errorCodeList.addAll(new LinkedHashSet<>(Set.of(
                API_NOT_FOUND,
                API_METHOD_NOT_ALLOWED,
                API_SERVER_ERROR,

                API_MISSING_PARAM,
                API_INVALID_PARAM,
                API_INVALID_TYPE
        )));


        this.errorCodeList = errorCodeList;
    }
}
