package com.santander.login.di

import com.santander.login.view.LoginFragment
import dagger.Subcomponent
import dagger.android.AndroidInjector

@Subcomponent
interface LoginFragmentSubcomponent : AndroidInjector<LoginFragment> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<LoginFragment>
}
