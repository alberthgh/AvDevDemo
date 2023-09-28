package com.albert.avdevdemo.junior

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.albert.avdevdemo.R

class MyDrawBitmapView : View {

    private lateinit var paint: Paint
    private lateinit var bitmap: Bitmap

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
        }
        bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.cat)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //不要在onDraw做任何分配内存的操作！！！！
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }
}