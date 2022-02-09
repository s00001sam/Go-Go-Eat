package com.sam.gogoeat.hilt

import androidx.fragment.app.Fragment
import com.sam.gogoeat.view.support.PriceSpinner
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@InstallIn(FragmentComponent::class)
@Module
class FragmentModule {
    @Provides
    fun providePriceSpinner(fragment: Fragment) = PriceSpinner(fragment)
}