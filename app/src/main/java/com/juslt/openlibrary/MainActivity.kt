package com.juslt.openlibrary

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val pannelPieChat = PieChat(this)
        val colors = arrayListOf(R.color.colorPurple,R.color.colorYellow,R.color.colorGreen,R.color.color_fdb741)
        val percentages = arrayListOf(20f,30f,15f,35f)
        panel.initAttr(colors ,percentages )
//        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
//        fl_container.addView(pannelPieChat,params)
    }
}
