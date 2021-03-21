package com.example.myapplication.di.features

import androidx.lifecycle.SavedStateHandle
import com.example.myapplication.configModule
import com.example.myapplication.data.net.NewsService
import com.example.myapplication.data.repository.NewsRepositoryImpl
import com.example.myapplication.data.repository.data_source.CloudNewsDataSource
import com.example.myapplication.data.repository.data_source.NewsDataSource
import com.example.myapplication.data.repository.data_source.NewsDataSourceFactory
import com.example.myapplication.data.repository.data_source.NewsDataSourceFactoryImpl
import com.example.myapplication.di.DI
import com.example.myapplication.di.getService
import com.example.myapplication.domain.NewsRepository
import com.example.myapplication.domain.interactor.NewsInteractor
import com.example.myapplication.domain.interactor.NewsInteractorImpl
import com.example.myapplication.ui.screens.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext
import org.koin.dsl.bind

fun createNewsModule(config: DI.Config) = configModule(configuration = config){

    viewModel {NewsViewModel(get(), get()) }

    single {
        NewsInteractorImpl(
            get()
        )
    } bind (NewsInteractor::class)

    single {
        NewsRepositoryImpl(
            get()
        )
    } bind (NewsRepository::class)

    single{
        CloudNewsDataSource(
            getService(NewsService::class.java),
            GlobalContext.get().get(),
        )
    } bind (NewsDataSource::class)

    single {
        NewsDataSourceFactoryImpl(
            get()
        )
    } bind (NewsDataSourceFactory::class)

}

