package com.highstreet.lib.datasource

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource


class DataSourceFactory<T>(private var dataLoader: PagedDataLoader<T>) :
    DataSource.Factory<Int, T>() {

    val sourceLiveData = MutableLiveData<PagedDataSource<T>>()

    override fun create(): DataSource<Int, T> {
        val dataSource = PagedDataSource(dataLoader)
        sourceLiveData.postValue(dataSource)
        return dataSource
    }

//    override fun create(): PagedDataSource<T>? {
//        val dataSource = PagedDataSource(dataLoader)
//        sourceLiveData.postValue(dataSource)
//        return dataSource
//    }
}
