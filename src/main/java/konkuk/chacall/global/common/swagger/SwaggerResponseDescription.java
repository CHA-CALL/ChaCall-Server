package konkuk.chacall.global.common.swagger;

import jakarta.xml.bind.annotation.XmlType;
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
            USER_FORBIDDEN,
            BANK_ACCOUNT_ALREADY_EXISTS_FOR_USER,
            BANK_ACCOUNT_ALREADY_EXISTS
    ))),
    OWNER_GET_BANK_ACCOUNT(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN
    ))),
    OWNER_UPDATE_BANK_ACCOUNT(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN,
            BANK_ACCOUNT_NOT_FOUND,
            BANK_ACCOUNT_ALREADY_EXISTS,
            BANK_ACCOUNT_FORBIDDEN
    ))),
    OWNER_DELETE_BANK_ACCOUNT(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN,
            BANK_ACCOUNT_NOT_FOUND,
            BANK_ACCOUNT_FORBIDDEN
    ))),
    OWNER_REGISTER_CHAT_TEMPLATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN
    ))),
    OWNER_GET_CHAT_TEMPLATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN
    ))),
    OWNER_UPDATE_CHAT_TEMPLATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN,
            CHAT_TEMPLATE_NOT_FOUND
    ))),
    OWNER_DELETE_CHAT_TEMPLATE(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN,
            CHAT_TEMPLATE_NOT_FOUND
    ))),
    OWNER_GET_RESERVATION_HISTORY(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN
    ))),
    OWNER_GET_RESERVATION_DETAIL(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN,
            RESERVATION_NOT_FOUND
    ))),
    OWNER_GET_FOOD_TRUCK(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN
    ))),
    OWNER_DELETE_FOOD_TRUCK(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN,
            FOOD_TRUCK_NOT_FOUND
    ))),


    // Member
    MEMBER_SAVED_FOOD_TRUCK(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN,
            FOOD_TRUCK_NOT_FOUND,
            SAVED_FOOD_TRUCK_ALREADY_EXIST,
            SAVED_FOOD_TRUCK_NOT_FOUND
    ))),
    MEMBER_GET_SAVED_FOOD_TRUCKS(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN
    ))),
    MEMBER_RATING(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN,
            FOOD_TRUCK_NOT_FOUND,
            RATING_NOT_FOUND,
            RATING_ALREADY_REGISTERED
    ))),
    MEMBER_GET_RESERVATIONS_FOR_RATING(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN
    ))),


    // Region
    GET_REGIONS(new LinkedHashSet<>(Set.of(
            PARENT_REGION_NOT_FOUND
    ))),

    // Reservation
    CREATE_RESERVATION(new LinkedHashSet<>(Set.of(
            USER_NOT_FOUND,
            USER_FORBIDDEN,
            FOOD_TRUCK_NOT_FOUND,
            INVALID_DATE_INPUT
    ))),

    // Default
    DEFAULT(new LinkedHashSet<>())
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
