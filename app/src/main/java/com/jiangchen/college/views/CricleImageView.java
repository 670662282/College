package com.jiangchen.college.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.jiangchen.college.R;

/**
 * Created by Dell- on 2015/12/8 0008.
 * 绘制圆形imageView控件
 */
public class CricleImageView extends ImageView {

    private int width;
    private int height;
    private int size;
    private Paint p;
    //内边距
    private int inStroke;
    //外边距
    private int outStroke;

    public CricleImageView(Context context) {
        super(context);
        init(null);
    }

    public CricleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CricleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        if (attrs != null) {

            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.CricleImageView);
            final int N = array.getIndexCount();

            for (int i = 0; i < N; i++) {
                int index = array.getIndex(i);
                switch (index) {
                    case R.styleable.CricleImageView_cimg_instrokewidth:
                        inStroke = array.getDimensionPixelSize(index, 0);
                        break;
                    case R.styleable.CricleImageView_cimg_outstrokewidth:
                        outStroke = array.getDimensionPixelSize(index, 0);
                        break;
                }
            }

        }
    }


    //获取imageView控件的宽和高
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();

        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap srcbmp = ((BitmapDrawable) drawable).getBitmap();
            if (srcbmp != null) {
                //获取处理好的图片
                srcbmp = createBitmap(srcbmp);
                //一个size直径矩形 src可为空
               // Rect src = new Rect(0, 0, size, size);
                // ？？？
                //画到dst区域
                Rect dst = new Rect((width - size) / 2, (height - size) / 2, (width + size) / 2, (height + size) / 2);
                canvas.drawBitmap(srcbmp, null, dst, null);

                //==================开始绘制圆形边距
                p = new Paint();
                p.setColor(Color.WHITE);
                p.setAntiAlias(true);
                //设置边距为instroke 空心的
                p.setStrokeWidth(inStroke);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawCircle(width / 2, height / 2, (size + inStroke) / 2, p);
                //绘制外边距用的颜色
                p.setARGB(150, 255, 255, 255);
                //绘制外边距 原直径加上2个内边距的距离
                canvas.drawCircle(width / 2, height / 2, (size + 2 * inStroke + outStroke) / 2, p);
            } else {
                super.onDraw(canvas);

            }
        } else {
            super.onDraw(canvas);
        }

    }

    //把原图像处理成圆形
    private Bitmap createBitmap(Bitmap srcBitmap) {

        //直径 为原图像宽或者高的最小值 然后去掉内外边距
        size = (width < height ? width : height) - 2 * (inStroke + outStroke);
        //创建输出图形的格式 最高色彩
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        //创建画板 在outputBmp上画图
        Canvas canVas = new Canvas(output);
        //创建画笔
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        //设置抗锯齿
        p.setAntiAlias(true);
        //画板先绘制成透明
        canVas.drawARGB(0, 0, 0, 0);
        //再吧画板绘制成圆形 半径为size/2
        canVas.drawCircle(size / 2, size / 2, size / 2, p);

        //设置当两个图形相交时的模式，SRC_IN为取SRC图形相交的部分，多余的将被去掉
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //前景 绘制一个矩形 从坐标(0,0)开始 绘制一个原图像宽高的矩形 也就是原图片
        Rect src = new Rect(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
        //背景 绘制一个矩形 从坐标(0,0)开始 绘制一个宽和高为size的矩形
        Rect dst = new Rect(0, 0, size, size);

//        参数：src 可以为空，不为空时，canvas将bitmap画到该区域；
//        参数：dst 不为空，显示bitmap的显示区域。

        /**
         *   把源图形 通过canVas画板 画出去掉内外边距的圆形图片
         *   圆形图片和原图片重合在一起，多余的将会被去掉
         *   显示到一个去掉内外边距的矩形中。
         */
        canVas.drawBitmap(srcBitmap, null, dst, p);

        return output;
    }


    public int getInStroke() {
        return inStroke;
    }

    public void setInStroke(int inStroke) {
        this.inStroke = inStroke;
    }

    public int getOutStroke() {
        return outStroke;
    }

    public void setOutStroke(int outStroke) {
        this.outStroke = outStroke;
    }
}
