package com.juslt.common.widget.dialog

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.juslt.common.R
import com.juslt.common.rv.BaseRvFooterAdapter
import com.juslt.common.rv.BaseViewHolder
import com.juslt.common.rv.UIEventInterface
import org.jetbrains.anko.find

/**
 * Created by Administrator on 2018/6/8.
 */
class JusltListPopWindow(context: Context, width: Int, height: Int, attributeSet: AttributeSet? = null, def: Int = 0) : PopupWindow(context, attributeSet, def) {
    val adapter by lazy {
        PopListAdapter(UIEventInterface { event, index ->
            callback!!.callback(event as ItemInfo, index)
            dismiss()
        })
    }
    var callback: Callback? = null

    init {
        this.width = width
        this.height = height
        contentView = LayoutInflater.from(context).inflate(R.layout.pop_window_list, null)
        isFocusable = true
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(0x00000000))

        val recyclerView = contentView.find<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    fun updateList(dataList: ArrayList<ItemInfo>, callback: Callback) {
        adapter.update(dataList)
        this.callback = callback
    }

    class PopListAdapter(uiEventInterface: UIEventInterface, loadMore: LoadMore? = null) : BaseRvFooterAdapter(uiEventInterface, loadMore) {
        override fun update(obj: Any?, hasMore: Boolean) {
            dataList.clear()
            dataList.addAll(obj as Collection<Any>)
            notifyByFooter(hasMore)
        }

        override fun onCreateViewHolderAfterFooter(parent: ViewGroup?, viewType: Int): BaseViewHolder {
            return PopListHolder.create(parent!!, this)
        }
    }

    class PopListHolder(view: View, adapter: BaseRvFooterAdapter) : BaseViewHolder(view, adapter) {
        companion object {
            fun create(view: ViewGroup, adapter: BaseRvFooterAdapter): PopListHolder {
                return PopListHolder(inflateItemView(R.layout.item_pop_list, view), adapter)
            }
        }

        val tvContent by lazy { itemView.find<TextView>(R.id.tv_content) }
        override fun update(obj: Any?, position: Int) {
            val data = obj as ItemInfo
            tvContent.text = data.content
            tvContent.setOnClickListener {
                mAdapter.event(data, position)
            }

        }

        override fun reset() {
        }
    }

    interface Callback {
        fun callback(data: ItemInfo, pos: Int)
    }
    class ItemInfo(
            val content: String,
            val id:Int
    )
}