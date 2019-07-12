package com.santander.login.utils

import javax.inject.Inject

class LoginValidator{

    private val USER_MIN_CHARS = 3
    private val USER_MAX_CHARS = 50
    private val PASS_FIX_CHARS = 6

    fun validateUser(username :String?) :Boolean {
        if (username == null) {
            return false
        } else if(username.length < USER_MIN_CHARS || username.length > USER_MAX_CHARS)
            return false
        else{
            return username.matches("[a-zA-Z0-9]*".toRegex())
        }
    }

    /**
     * Password validation.
     * The password should be fixed length at 6 digits.
     * The password should be numeric.
     * @param password to validate
     * @return true if password is valid, false otherwise
     */
    fun validatePassword(password: String?): Boolean {
        return if (password == null || password.length != PASS_FIX_CHARS) {
            false
        } else {
            password.matches("[0-9]*".toRegex())
        }
    }
}