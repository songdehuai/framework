package com.ppz.framwork.base.list

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.ppz.framwork.global.Constant
import com.ppz.framework.R
import com.ppz.framwork.base.BaseActivity
import com.ppz.framwork.base.BaseListBean
import com.ppz.framwork.base.list.adapter.BaseListAdapter
import com.ppz.framwork.base.list.adapter.BaseLoadMoreListAdapter
import com.ppz.framwork.ext.ui
import com.ppz.framwork.http.Result
import com.ppz.framwork.widget.EmptyView
import com.ppz.framwork.widget.TitleView


abstract class BaseListActivity<T> : BaseActivity() {

    private val rvList by lazy { findViewById<RecyclerView>(R.id.rv_list) }
    private val rflList by lazy { findViewById<SwipeRefreshLayout>(R.id.rfl_list) }
    private val titleView by lazy { findViewById<TitleView>(R.id.title_view) }
    private var mPage = Constant.PageSize.DEFAULT_PAGE
    private var mPageSize = Constant.PageSize.DEFAULT_PAGE_SIZE
    private lateinit var mAdapter: BaseQuickAdapter<T, *>
    private var isLoadMore = false
    private lateinit var resultList: Result<BaseListBean<T>>

    private val emptyView by lazy {
        findViewById<EmptyView>(R.id.empty_view).also {
            it.click { getListData() }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base_list)
        initData()
        initViews()
        onInit(savedInstanceState)
    }


    private fun initViews() {
        titleView.setTitle(getTitleText())
        titleView.leftClick { finish() }
        rflList.setColorSchemeColors(ContextCompat.getColor(this, R.color.main))
        rflList.setOnRefreshListener {
            getListData()
        }
        if (isLoadMore) {
            mAdapter.loadMoreModule.setOnLoadMoreListener {
                loadMore()
            }
        }
        rvList.adapter = mAdapter
        if (isAutoLoad()) {
            rflList.isRefreshing = true
            getListData()
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            onItemClick(adapter as BaseQuickAdapter<T, *>, view, position)
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            onItemChildClickListener(adapter as BaseQuickAdapter<T, *>, view, position)
        }
        mAdapter.setOnItemLongClickListener { adapter, view, position ->
            onItemLongClickListener(adapter as BaseQuickAdapter<T, *>, view, position)
        }
        mAdapter.setOnItemChildLongClickListener { adapter, view, position ->
            onItemChildLongClickListener(adapter as BaseQuickAdapter<T, *>, view, position)
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
        emptyView.setText("加载中")
        launch({
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
        }, {
            ui { rflList.isRefreshing = false;checkShowEmptyView() }
        })
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

        launch({
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
        }, {
            mPage -= 1
            loadMoreFail()
        })
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

    abstract fun getTitleText(): String

    abstract suspend fun getApi(page: Int, size: Int): Result<BaseListBean<T>>

    abstract fun getAdapter(): BaseListAdapter<T>

    fun getItemData(position: Int): T {
        return mAdapter.getItem(position)
    }

    open fun isAutoLoad(): Boolean {
        return true
    }

    open fun isShowEmpty(): Boolean {
        return true
    }

    open fun onInit(savedInstanceState: Bundle?) {}

    open fun onItemClick(adapter: BaseQuickAdapter<T, *>, view: View, position: Int) {}

    open fun onItemChildClickListener(adapter: BaseQuickAdapter<T, *>, view: View, position: Int) {}

    open fun onItemLongClickListener(adapter: BaseQuickAdapter<T, *>, view: View, position: Int): Boolean {
        return false
    }

    open fun onItemChildLongClickListener(adapter: BaseQuickAdapter<T, *>, view: View, position: Int): Boolean {
        return false
    }


}





