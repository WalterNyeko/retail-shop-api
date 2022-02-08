package com.assignment.helpers;


import com.assignment.exceptions.BadRequestException;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Validator {

    private static final Logger LOG = Logger.getLogger(Validator.class.getName());

    public static void notNull(Object object, String message, Object... args) {
        if (object == null) {
            throwBadRequestException(String.format(message, args));
        }
    }

    public static void validEmployeeAndAffiliateDetails(Object employee, Object affiliate, String message) {
        if (employee != null && affiliate != null) {
            throwBadRequestException(message);
        }
    }

    public static void isGreaterThanZero(BigDecimal value, String message) {
        if (value == null || value.compareTo(BigDecimal.ZERO) < 1) {
            throw new BadRequestException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, String message) {
        if (CollectionUtils.isEmpty(collection)) {
            throwBadRequestException(message);
        }
    }

    public static <T extends Enum<T>> void validEnum(Class<T> clazz, String enumName, String message) {
        if (!EnumUtils.isValidEnum(clazz, enumName)) {
            throwBadRequestException(message);
        }
    }

    public static void validStringLength(String str, Integer minLength, Integer maxLength, String message) {
        if (str == null || (str.length() < minLength && str.length() > maxLength)) {
            throwBadRequestException(message);
        }
    }

    public static void validEmail(String email, String message) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (email == null || !pattern.matcher(email).matches()) {
            throwBadRequestException(message);
        }
    }

    public static void validPassword(String password, String message) {
        String regex = "^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        if (password == null || !pattern.matcher(password).matches()) {
            throwBadRequestException(message);
        }
    }

    public static <X extends Throwable> void throwException(Supplier<? extends X> exceptionSupplier) throws X {
        throw exceptionSupplier.get();
    }

    public static void throwBadRequestException(String message) {
        throwException(() -> new BadRequestException(message));
    }

    public static void validateDateIsInTheFuture(String date, String message) throws ParseException {
        if (date == null) throw new BadRequestException(message);
        Date current = new Date();
        String myFormatString = "dd/MM/yyyy";
        SimpleDateFormat df = new SimpleDateFormat(myFormatString);
        Date givenDate = df.parse(date);
        Long givenDateTime = givenDate.getTime();
        Date next = new Date(givenDateTime);
        if(!next.after(current) && !(next.equals(current))){
            throw new BadRequestException(message);
        }
    }
}
