package com.highstreet.lib.ui

import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.RecyclerView
import android.view.View
import com.highstreet.lib.R
import com.highstreet.lib.adapter.BaseItem
import com.highstreet.lib.adapter.BasePagedAdapter
import com.highstreet.lib.adapter.OnItemClickListener
import com.highstreet.lib.common.RefreshResult
import com.highstreet.lib.extensions.init
import com.highstreet.lib.view.RefreshLayout
import com.highstreet.lib.view.StateView
import com.highstreet.lib.viewmodel.BaseListViewModel
import java.lang.reflect.ParameterizedType

/**
 * @author Yang Shihao
 * @date 2018/11/18
 */
abstract class BaseListFragment<T : BaseItem, VM : BaseListViewModel<T>> : BaseFragment(),
        OnItemClickListener<T> {

    val viewModel: VM by lazy {
        val parameterizedType = javaClass.genericSuperclass as ParameterizedType
        val cla = parameterizedType.actualTypeArguments[1] as Class<VM>
        ViewModelProviders.of(this).get(cla)
    }

    private var refreshLayout: RefreshLayout? = null
    private var stateView: StateView? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BasePagedAdapter<T>

    override fun getLayoutId() = R.layout.activity_base_list

    override fun initView() {
        refreshLayout = f(R.id.baseRefreshLayout)
        recyclerView = f(R.id.baseRecyclerView)!!
        stateView = f(R.id.baseEmptyView)
        adapter = createAdapter()
        adapter.itemClickListener = this
        recyclerView.init(adapter)
        refreshLayout?.setOnRefreshListener {
            viewModel.invalidate()
        }
    }

    override fun initData() {
        viewModel.observeDataObserver(this,
                { adapter.submitList(it) },
                { refreshFinished(it) },
                { loadMoreFinished(it) })

        viewModel.observeAdapterObserver(this,
                { position, payload ->
                    adapter.notifyItemChanged(position, payload)
                },
                {
                })
    }

    override fun itemClicked(view: View, item: T, position: Int) {

    }

    open fun refreshFinished(result: RefreshResult) {
        refreshLayout?.isRefreshing = false
        stateView?.apply {
            when (result) {
                RefreshResult.SUCCEED -> state = StateView.Status.DISMISS
                RefreshResult.FAILED -> state = StateView.Status.LOAD_FAILED
                RefreshResult.NO_DATA -> state = StateView.Status.NO_DATA
                RefreshResult.NO_MORE -> {
                    state = StateView.Status.DISMISS
                    toast(R.string.base_t_no_more)
                }
            }
        }
    }

    private fun loadMoreFinished(result: RefreshResult) {
        when (result) {
            RefreshResult.SUCCEED -> {
            }
            RefreshResult.FAILED -> {
            }
            RefreshResult.NO_MORE -> toast(R.string.base_t_no_more)
        }
    }

    protected fun getStateView() = stateView

    abstract fun createAdapter(): BasePagedAdapter<T>
}