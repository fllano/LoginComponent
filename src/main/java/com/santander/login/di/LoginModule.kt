package com.santander.login.di

import androidx.lifecycle.ViewModel
import com.santander.globile.core.di.ViewModelKey
import com.santander.login.view.LoginFragment
import com.santander.login.view.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [LoginFragmentSubcomponent::class])
abstract class LoginModule {

    @Binds
    @IntoMap
    @ClassKey(LoginFragment::class)
    internal abstract fun bindLoginFragmentInjectorFactory(factory: LoginFragmentSubcomponent.Factory): AndroidInjector.Factory<*>

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel
}
