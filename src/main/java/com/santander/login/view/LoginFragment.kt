package com.santander.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.santander.globile.core.base.view.BaseFragment
import com.santander.login.R
import com.santander.login.navigation.LoginNavigator
import javax.inject.Inject

/**
 * Created by fernando on 22/2/19.
 */

class LoginFragment : BaseFragment<LoginViewModel>(), LoginInterface {

    @Inject
    lateinit var navigator : LoginNavigator

    override fun navigateGlobalPosition() {
        navigator.navigateGlobalPosition(rootView)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onBind(inflater: LayoutInflater, container: ViewGroup?): View {
        return DataBindingUtil.inflate<com.santander.login.databinding.FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login,
            container,
            false
        ).apply {
            viewModel = this@LoginFragment.viewModel
            lifecycleOwner = this@LoginFragment
            executePendingBindings()
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginInterface = this

    }
}
