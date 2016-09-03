package demo.itcaset.cn.myapplication.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 罗晓飞
 */
public class QuickIndexBar extends View {
    private static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    private int mMeasuredHeight;
    private int mMeasuredWidth;
    private Paint mPaint;
    private int mCellWith;
    private float mCellHeight;
    private int mIndex;
    private int mTouchIndex = -1;

    /**
     * 外界的回调监听
     */
    public interface OnLetterUpdateListener{
        void onLetterUpdate(String letter);
    }
    private OnLetterUpdateListener listener;

    public void setLetterUpdateListener(OnLetterUpdateListener listener){
        this.listener=listener;
    }

    public QuickIndexBar(Context context) {
        this(context,null);
    }

    public QuickIndexBar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QuickIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(25);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);
    }
    @Override
    protected void onDraw(Canvas canvas) {

        //获取每个字母所占的宽高
        mCellWith = mMeasuredWidth;
        mCellHeight = mMeasuredHeight/LETTERS.length*1.0f;
        for (int i = 0; i <LETTERS.length ; i++) {
            //获取每个字母的绘制坐标
            float x = mCellWith *1f/ 2 - mPaint.measureText(LETTERS[i]) / 2;
            //获取每个字母的高度
            Rect rect = new Rect();
            mPaint.getTextBounds(LETTERS[i],0,LETTERS[i].length(),rect);
            int r = rect.height();
            float y = mCellHeight * i + mCellHeight / 2 + r / 2;
            //绘制控件
            mPaint.setColor(mTouchIndex==i?Color.BLACK:Color.WHITE);
            canvas.drawText(LETTERS[i],x,y,mPaint);
    }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mMeasuredHeight = getMeasuredHeight();
        mMeasuredWidth = getMeasuredWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(MotionEventCompat.getActionMasked(event)){
            case MotionEvent.ACTION_DOWN:
                mIndex = (int) (event.getY() / mCellHeight);
                if (mIndex >=0&& mIndex <=LETTERS.length){
                    if (mIndex!= mTouchIndex){
                        //如果有人调用监听就弹出一个土司
                        if (listener!=null){
                            listener.onLetterUpdate(LETTERS[mIndex]);
                        }
                        mTouchIndex =mIndex;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                mIndex = (int) (event.getY() / mCellHeight);
                if (mIndex >=0&& mIndex <=LETTERS.length){
                    if (mIndex!= mTouchIndex){
                        if (listener!=null){
                            listener.onLetterUpdate(LETTERS[mIndex]);
                        }
                        mTouchIndex =mIndex;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mTouchIndex =-1;
                break;

        }
        invalidate();
        return true;
    }
}
