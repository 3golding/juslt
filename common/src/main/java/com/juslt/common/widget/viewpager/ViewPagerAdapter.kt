package com.juslt.common.widget.viewpager

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import java.util.ArrayList

/**
 * Created by wx on 2018/5/29.
 */
class ViewPagerAdapter(private val mContext: Context, imgs: ArrayList<View>) : PagerAdapter() {
    private val imageList by lazy { ArrayList<View>() }

    init {
        imageList.addAll(imgs)
    }

    override fun getCount(): Int {
        if(imageList.size==1){
            return 1
        }
        return Int.MAX_VALUE
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view == any
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var pos = position%imageList.size
        if(pos<0){
            pos += imageList.size
        }
        val imageView = imageList[pos]
        val viewParent = imageView.parent
        if(viewParent!=null){
            val parent = viewParent as ViewGroup
            parent.removeView(imageView)
        }
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

    }
}