package com.ppz.framwork.base.list

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ppz.framwork.global.Constant
import com.ppz.framework.R
import com.ppz.framwork.base.BaseActivity
import com.ppz.framwork.base.BaseFragment
import com.ppz.framwork.base.BaseListBean
import com.ppz.framwork.base.list.adapter.BaseListAdapter
import com.ppz.framwork.base.list.adapter.BaseLoadMoreListAdapter
import com.ppz.framwork.ext.ui
import com.ppz.framwork.http.Result
import com.ppz.framwork.widget.EmptyView


abstract class BaseListFragment<T> : BaseFragment() {

    private val activity by lazy { getActivity() as BaseActivity }
    private val rvList by lazy { mView.findViewById<RecyclerView>(R.id.rv_list) }
    private val rflList by lazy { mView.findViewById<SwipeRefreshLayout>(R.id.rfl_list) }
    private lateinit var mAdapter: BaseQuickAdapter<T, *>
    private var mPage = Constant.PageSize.DEFAULT_PAGE
    private var mPageSize = Constant.PageSize.DEFAULT_PAGE_SIZE
    private var isLoadMore = false
    private lateinit var resultList: Result<BaseListBean<T>>
    private val emptyView by lazy {
        mView.findViewById<EmptyView>(R.id.empty_view).also {
            it.click { getListData() }
        }
    }


    override fun getLayout(container: ViewGroup?): Any {
        return R.layout.fragment_base_list
    }

    override fun init(savedInstanceState: Bundle?) {
        initData()
        initViews()
        onInit(savedInstanceState)
    }


    private fun initViews() {
        rflList.setColorSchemeColors(ContextCompat.getColor(activity, R.color.main))
        rflList.setOnRefreshListener {
            getListData()
        }
        if (isLoadMore) {
            mAdapter.loadMoreModule.setOnLoadMoreListener {
                loadMore()
            }
        }
        rvList.layoutManager = getLayoutManager()
        rvList.adapter = mAdapter
        if (isAutoLoad()) {
            rflList.isRefreshing = true
            getListData()
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            onItemClick(adapter, view, position)
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            onItemChildClickListener(adapter, view, position)
        }
        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            onItemLongClickListener(adapter, view, position)
        }
        mAdapter.setOnItemChildLongClickListener { adapter, view, position ->
            onItemChildLongClickListener(adapter, view, position)
        }

    }

    private fun initData() {
        mAdapter = getAdapter()
        isLoadMore = mAdapter is BaseLoadMoreListAdapter
        loadMoreAttach()
    }

    fun getListData() {
        mPage = Constant.PageSize.DEFAULT_PAGE
        mPageSize = Constant.PageSize.DEFAULT_PAGE_SIZE
        launch {
            getApi(mPage, mPageSize)
                .result {
                    if (it.isSuccess()) {
                        mAdapter.data.clear()
                        it.data?.itemList?.let { list -> mAdapter.data.addAll(list);checkShowEmptyView() }
                        loadMoreAttach()
                        mAdapter.notifyDataSetChanged()
                        rflList.isRefreshing = false
                    } else {
                        ui { rflList.isRefreshing = false;checkShowEmptyView() }
                    }
                }
        }
    }

    fun loadMore() {
        if (this::resultList.isInitialized) {
            resultList.data?.totalCount?.let {
                if (mAdapter.data.size >= it) {
                    mAdapter.loadMoreModule.loadMoreEnd()
                    loadMoreClose()
                }
                return
            }
        }
        mPage += 1
        launch {
            getApi(mPage, mPageSize)
                .result {
                    if (it.isSuccess()) {
                        it.data?.itemList?.let { list -> mAdapter.data.addAll(list) }
                        mAdapter.notifyDataSetChanged()
                        loadMoreComplete()
                        if (mAdapter.data.size >= it.data?.totalCount ?: 0) {
                            loadMoreClose()
                        }
                    } else {
                        mPage -= 1
                        loadMoreFail()
                    }
                }
        }
    }

    private fun checkShowEmptyView() {
        if (mAdapter.data.isNullOrEmpty()) {
            emptyView.show()
            emptyView.setText("暂无数据")
        } else {
            emptyView.hide()
            emptyView.setText("")
        }
    }


    private fun loadMoreAttach() {
        if (isLoadMore) {
            if (this::resultList.isInitialized) {
                resultList.data?.totalCount?.let {
                    if (mAdapter.data.size >= it) {
                        mAdapter.loadMoreModule.isEnableLoadMore = false
                    }
                    return
                }
            }
            mAdapter.loadMoreModule.isAutoLoadMore = true
            mAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
            mAdapter.loadMoreModule.isEnableLoadMore = true
        }
    }

    private fun loadMoreClose() {
        if (isLoadMore) {
            mAdapter.loadMoreModule.isEnableLoadMore = false
        }
    }

    private fun loadMoreComplete() {
        if (isLoadMore) {
            mAdapter.loadMoreModule.loadMoreComplete()
        }
    }

    private fun loadMoreFail() {
        if (isLoadMore) {
            mAdapter.loadMoreModule.loadMoreFail()
        }
    }


    abstract suspend fun getApi(page: Int, size: Int): Result<BaseListBean<T>>

    abstract fun getAdapter(): BaseListAdapter<T>

    fun getItemData(position: Int): T {
        return mAdapter.getItem(position)
    }

    open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(requireContext())
    }


    open fun isAutoLoad(): Boolean {
        return true
    }

    open fun isShowEmpty(): Boolean {
        return true
    }

    open fun onInit(savedInstanceState: Bundle?) {}

    open fun onItemClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}

    open fun onItemChildClickListener(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {}

    open fun onItemLongClickListener(adapter: BaseQuickAdapter<*, *>, view: View, position: Int): Boolean {
        return false
    }

    open fun onItemChildLongClickListener(adapter: BaseQuickAdapter<*, *>, view: View, position: Int): Boolean {
        return false
    }


}





