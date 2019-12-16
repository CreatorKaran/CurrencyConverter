package com.example.currencyconverter.InputFilter;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecimalInputFilter implements InputFilter {
    Pattern pattern;

    public DecimalInputFilter(int digitsBeforeZero,int digitsAfterZero) {
        pattern= Pattern.compile("[0-9]{0," + (digitsBeforeZero-1) + "}+((\\.[0-9]{0," + (digitsAfterZero-1) + "})?)||(\\.)?");
    }


    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher=pattern.matcher(dest);
        if(!matcher.matches())
            return "";
        return null;
    }
}
