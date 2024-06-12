package com.marco.poccropimagewithgesture.di


import com.marco.poccropimagewithgesture.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    // Home module
    viewModel { MainViewModel() }

}