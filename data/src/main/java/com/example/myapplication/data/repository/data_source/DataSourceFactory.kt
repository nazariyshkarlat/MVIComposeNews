package com.example.myapplication.data.repository.data_source

class NewsDataSourceFactoryImpl(
    private val cloudNewsDataSource: CloudNewsDataSource
) : NewsDataSourceFactory {

    override fun create(priority: NewsDataSourceFactory.Priority) =
        if (priority == NewsDataSourceFactory.Priority.CLOUD)
            cloudNewsDataSource
        else
            cloudNewsDataSource
    //TODO
}

interface NewsDataSourceFactory {

    enum class Priority {
        CLOUD,
        CACHE
    }

    fun create(priority: Priority): NewsDataSource
}