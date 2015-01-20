package com.geomarketv3.validation.validator;

import java.util.regex.Pattern;


import com.example.geomarketv3.R;
import com.geomarketv3.validation.AbstractValidator;
import android.content.Context;
import android.util.Patterns;
public class UrlValidator extends AbstractValidator {

    private static final Pattern WEB_URL_PATTERN = Patterns.WEB_URL;
    private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_url;

    public UrlValidator(Context c) {
        super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
    }

    public UrlValidator(Context c, int errorMessageRes) {
        super(c, errorMessageRes);
    }

    @Override
    public boolean isValid(String url) {
        return WEB_URL_PATTERN.matcher(url).matches();
    }
}
