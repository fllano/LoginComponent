package com.santander.login.view

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.santander.commonapisclient.ApiRepository
import com.santander.commonapisclient.api.ApiCallback
import com.santander.commonapisclient.api.ApiCode
import com.santander.commonapisclient.models.ErrorResponse
import com.santander.commonapisclient.models.LoginResponse
import com.santander.globile.core.base.view.BaseViewModel
import com.santander.login.R
import com.santander.login.navigation.LoginNavigator
import com.santander.login.utils.LoginValidator
import java.util.*
import javax.inject.Inject


/**
 * Created by fernando on 25/2/19.
 */

open class LoginViewModel @Inject constructor(private val apiRepository: ApiRepository, application: Application) : BaseViewModel(application) {

    var greetings = MutableLiveData<String>()

    var user = MutableLiveData<String>()
    var pass = MutableLiveData<String>()

    var userError = MutableLiveData<String>()
    var passError = MutableLiveData<String>()

    var loginRemembered: Boolean? = false
    var userName = MutableLiveData<String>()
    var pinNumber = MutableLiveData<String>()

    var loginInterface: LoginInterface? = null

    init {
        loadData()
    }

    object Keys {
        const val sharedKey ="scalKey"
        const val loginRememberedKey = "LoginRemembered"
        const val rememberedUserNameKey = "RememberedUserName"
    }

    private fun loadData() {

        loginRemembered = com.santander.globile.core.base.Security.getBooleanSharedPrefValue(getApplication(),Keys.sharedKey ,Keys.loginRememberedKey)
        userName.value = com.santander.globile.core.base.Security.getStringSharedPrefValue(getApplication(),Keys.sharedKey ,Keys.rememberedUserNameKey)
        pinNumber.value = ""
        greetings = MutableLiveData<String>().apply{postValue(getGreetings(Calendar.getInstance()))}
    }

    fun doLogin(v: View) {
        //TODO ANALYTICS TEST
        /*
        FunctionalAnalytics.init(this.getApplication<StarterApplication>().applicationContext)
        val eventMap = HashMap<String,Any?>()
        eventMap.put("item_id","functional_Android")
        eventMap.put("item_name","functional_view")
        eventMap.put("content_type","testType")
        eventMap.put("integer_value", 123456)
        logEvent("pageview",eventMap)
        */


        if (user.value == null && pass.value == null)
            return

        val validator = LoginValidator()

        if (!validator.validateUser(user.value!!) || !validator.validatePassword(pass.value!!)) {
            userError.postValue(getApplication<Application>().getString(R.string.login_invalid_title));
            passError.postValue(getApplication<Application>().getString(R.string.login_invalid_title));
            return
        }

        apiRepository.callLogin("fernando","123456", object: ApiCallback<LoginResponse, ErrorResponse> {

            override fun onResponse(code: ApiCode, response: LoginResponse?) {
                processLogin(response)
            }

            override fun onErrorResponse(code: ApiCode, response: ErrorResponse?) {

                //TODO
                //show alert
                //navigationHelper.showNativeAlert(R.string.login_invalid_title, R.string.login_invalid_text,R.string.dialog_try_again)
            }

            override fun onFailure(t: Throwable) {

                //show alert
                //navigationHelper.showNativeAlert(R.string.error, t.localizedMessage ,R.string.dialog_ok)
            }

        })

    }

    fun addPin(number: Char) {
        pinNumber.value = pinNumber.value!! + number

        if (pinNumber.value!!.length == 4)
            checkPin()
    }

    private fun checkPin() {

        loginInterface?.navigateGlobalPosition()
    }

    fun removePin() {

        val currentPin = pinNumber.value

        if (currentPin!!.isNotEmpty())
            pinNumber.value = currentPin.substring(0, currentPin.length - 1)
    }

    private fun processLogin(response: LoginResponse?) {

        response?.userName?.let {
            com.santander.globile.core.base.Security.setStringSharedPrefValue(getApplication(), Keys.sharedKey, Keys.rememberedUserNameKey, it)
            com.santander.globile.core.base.Security.setBooleanSharedPrefValue(getApplication(), Keys.sharedKey, Keys.rememberedUserNameKey, true)
        }

        loginInterface?.navigateGlobalPosition()
    }

    fun validateLogin(user: String?, pass: String?): Boolean {
        //show errors in layout
        userError.value = when (user?.length) {
            in 1 until 4    -> getApplication<Application>().getString(R.string.login_error_user)
            else            -> ""
        }
        passError.value = when (pass?.length) {
            in 1 until 6    -> getApplication<Application>().getString(R.string.login_error_pass)
            else            -> ""
        }

        //result
        val loginValidator = LoginValidator()
        return loginValidator.validateUser(user) && loginValidator.validatePassword(pass)
    }

    /**
     * Gets the greeting phrase depending on the time.
     * @calendar actual date
     */
    fun getGreetings(calendar:Calendar): String {
        val timeOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val result = when (timeOfDay) {
            in 6    until 12    -> getApplication<Application>().getString(R.string.login_good_morning)
            in 12   until 20    -> getApplication<Application>().getString(R.string.login_good_afternoon)
            else                -> getApplication<Application>().getString(R.string.login_good_evening)
        }
        return result
    }
}
