package com.highstreet.lib.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PageKeyedDataSource
import android.arch.paging.PagedList
import android.support.annotation.NonNull
import com.highstreet.lib.common.RefreshResult
import com.highstreet.lib.datasource.DataSourceFactory
import com.highstreet.lib.datasource.PagedDataLoader

abstract class BaseListViewModel<T> : BaseViewModel(), PagedDataLoader<T> {

    open fun pageSize(): Int {
        return 20
    }

    private val data = ArrayList<T>()

    private val dataSourceFactory: DataSourceFactory<T> by lazy {
        DataSourceFactory(this)
    }

    private val loadLiveData = LivePagedListBuilder(dataSourceFactory, pageSize()).build()

    private val refreshLiveData: MutableLiveData<RefreshResult> = MutableLiveData()

    private val loadMoreLiveData: MutableLiveData<RefreshResult> by lazy { MutableLiveData<RefreshResult>() }

    private val notifyItemLiveData: MutableLiveData<Pair<Int, Any?>> by lazy { MutableLiveData<Pair<Int, Any?>>() }

    private val removeItemLiveData: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    override fun refreshInResume() {
        invalidate()
    }

    /**
     * 刷新
     */
    fun invalidate() {
        dataSourceFactory.sourceLiveData.value?.invalidate()
    }

    fun observeDataObserver(
            @NonNull owner: LifecycleOwner,
            data: (PagedList<T>) -> Unit,
            refreshResult: (RefreshResult) -> Unit,
            loadMoreResult: (RefreshResult) -> Unit
    ) {
        loadLiveData.observe(owner, Observer {
            it?.apply(data)
        })

        refreshLiveData.observe(owner, Observer {
            refreshResult(it!!)
        })

        loadMoreLiveData.observe(owner, Observer {
            loadMoreResult(it!!)
        })
    }

    fun observeAdapterObserver(
            @NonNull owner: LifecycleOwner,
            notifyItem: (Int, Any?) -> Unit,
            removeItem: (Int) -> Unit
    ) {
        notifyItemLiveData.observe(owner, Observer {
            it?.apply { notifyItem(first, second) }
        })

        removeItemLiveData.observe(owner, Observer {
            removeItem(it!!)
        })
    }

    final override fun loadInitial(
            params: PageKeyedDataSource.LoadInitialParams<Int>,
            callback: PageKeyedDataSource.LoadInitialCallback<Int, T>
    ) {
        refresh()
        data.clear()
        loadData(1) {
            when {
                it == null -> refreshLiveData.value = RefreshResult.FAILED
                it.isEmpty() -> refreshLiveData.value = RefreshResult.NO_DATA
                it.size < pageSize() -> {
                    data.addAll(it)
                    callback.onResult(it, null, null)
                    refreshLiveData.value = RefreshResult.NO_MORE
                }
                else -> {
                    data.addAll(it)
                    callback.onResult(it, null, 2)
                    refreshLiveData.value = RefreshResult.SUCCEED
                }
            }
        }
    }

    final override fun loadAfter(
            params: PageKeyedDataSource.LoadParams<Int>,
            callback: PageKeyedDataSource.LoadCallback<Int, T>
    ) {
        loadMore()
        loadData(params.key) {
            when {
                it == null -> loadMoreLiveData.value = RefreshResult.FAILED
                it.size < pageSize() -> {
                    data.addAll(it)
                    callback.onResult(it, null)
                    loadMoreLiveData.value = RefreshResult.NO_MORE
                }
                else -> {
                    data.addAll(it)
                    callback.onResult(it, params.key + 1)
                    loadMoreLiveData.value = RefreshResult.SUCCEED
                }
            }
        }
    }

    fun notifyItem(position: Int) {
        notifyItemLiveData.value = Pair(position, null)
    }

    fun notifyItem(position: Int, payload: Any?) {
        notifyItemLiveData.value = Pair(position, payload)
    }

    private fun removeItem(position: Int) {

    }

    override fun refresh() {
    }

    override fun loadMore() {
    }

    abstract fun loadData(page: Int, onResponse: (ArrayList<T>?) -> Unit)
}
