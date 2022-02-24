package com.sam.gogoeat.hilt

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.sam.gogoeat.view.support.PressBackHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class ActivityModule {
    @Provides
    fun provideBackHelper() = PressBackHelper()

    @Provides
    fun provideGA() = Firebase.analytics
}