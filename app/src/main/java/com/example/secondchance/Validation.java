package com.example.secondchance;

import android.widget.EditText;
import java.util.regex.Pattern;

public class Validation {

    private static final String EMAIL_REGEX = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
    private static final String PASSWORD_REGEX = "\\d{4,}";
    private static final String PHONE_REGEX = "^[0][5]\\d{8}$";




    // Error Messages
    private static final String REQUIRED_MSG = "required";
    private static final String EMAIL_MSG = "invalid email";
    private static final String PHONE_MSG = "invalid phone number";
    private static final String PASSWORD_MSG = "too short password";


    public static boolean isEmailAddress(EditText editText, boolean required) {
        return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
    }

    public static boolean isPhoneNumber(EditText editText, boolean required) {
        return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
    }

    public static boolean isPassword(EditText editText, boolean required) {
        return isValid(editText, PASSWORD_REGEX, PASSWORD_MSG, required);
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // text required and editText is blank, so return false
        if ( required && !hasText(editText) ) return false;

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        };

        return true;
    }

    // check the input field has any text or not
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (text.length() == 0) {
            editText.setError(REQUIRED_MSG);
            return false;
        }

        return true;
    }

    public static boolean checkAllfields(String fn,String ln,String email, String pass,String phone)
    {
        if(fn.length()>0&&ln.length()>0&&email.length()>0&&pass.length()>=4&&phone.length()==10){
        return true;
        }
        return false;

    }


}