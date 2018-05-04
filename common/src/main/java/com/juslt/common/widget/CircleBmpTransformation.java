package com.juslt.common.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.ColorInt;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;


/**
 * Created by User on 2016/12/7.
 */

public class CircleBmpTransformation implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;

    @ColorInt
    private int color;

    private int borderWidth;

    public CircleBmpTransformation(Context context, @ColorInt int color, int borderWidth) {
        mBitmapPool = Glide.get(context).getBitmapPool();
        this.color = color;
        this.borderWidth = borderWidth;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {

        //创建和输出大小一样的画布
        int size = Math.min(outWidth,outHeight);
        Bitmap resultBitmap = mBitmapPool.get(size, size, Bitmap.Config.ARGB_8888);
        if (resultBitmap == null) {
            resultBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(resultBitmap);

        Paint bitmapPaint = new Paint();
        Bitmap bitmap = resource.get();
        BitmapShader shader = new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);

        scale(shader,bitmap.getWidth(),bitmap.getHeight(),size);

        bitmapPaint.setShader(shader);
        bitmapPaint.setAntiAlias(true);

        float center = size/2f;
        canvas.drawCircle(center, center, center-borderWidth, bitmapPaint);//需要减去边界

        if(borderWidth!=0){
            Paint borderPaint = new Paint();
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(color);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(borderWidth);
            canvas.drawCircle(center,center,center-borderWidth/2,borderPaint);
        }

        return BitmapResource.obtain(resultBitmap, mBitmapPool);
    }

    @Override public String getId() {
        return "CircleWhiteBorderTransformation-"+color+"-"+borderWidth;
    }

    private void scale(BitmapShader shader,int srcWidth,int srcHeight,int targetSize){
        Matrix matrix = new Matrix();

        if(srcWidth>srcHeight){
            matrix.setScale(targetSize*1f/srcWidth,targetSize*1f/srcWidth);
        }else {
            matrix.setScale(targetSize*1f/srcHeight,targetSize*1f/srcHeight);
        }

        shader.setLocalMatrix(matrix);
    }
}
