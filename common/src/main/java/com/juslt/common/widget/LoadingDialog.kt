package com.juslt.common.widget

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatDialogFragment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.juslt.common.R


class LoadingDialog : AppCompatDialogFragment() {
    private var loadingIv: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pop_loading, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loadingIv = view!!.findViewById<View>(R.id.iv_loading) as ImageView

        val animation = AnimationUtils.loadAnimation(activity, R.anim.loading_rotate)
        val lInterpolator = LinearInterpolator()
        animation.interpolator = lInterpolator
        loadingIv!!.animation = animation
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog

        dialog.setCanceledOnTouchOutside(false)//设置点击Dialog外部任意区域关闭Dialog

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        dialog.window!!.setLayout((dm.widthPixels * 0.2).toInt(), (dm.widthPixels * 0.2).toInt())

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val params = dialog.window!!.attributes
        params.dimAmount = 0f
        dialog.window!!.attributes = params

    }
}
