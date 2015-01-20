package com.geomarketv3.validation.validator;

import java.util.regex.Pattern;


import com.example.geomarketv3.R;
import com.geomarketv3.validation.AbstractValidator;
import com.geomarketv3.validation.ValidatorException;
import android.content.Context;
import android.util.Patterns;
/**
 * Validator to check if Phone number is correct.
 * Created by throrin19 on 13/06/13.
 */
public class PhoneValidator extends AbstractValidator {

    private static final Pattern PHONE_PATTERN = Patterns.PHONE;
    private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_phone;

    public PhoneValidator(Context c) {
        super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
    }

    public PhoneValidator(Context c, int errorMessageRes) {
        super(c, errorMessageRes);
    }

    @Override
    public boolean isValid(String phone) throws ValidatorException {
        return PHONE_PATTERN.matcher(phone).matches();
    }
}
