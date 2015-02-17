package com.mostcho.androidcanvasandbitmapexample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mostcho.androidcanvasandbitmapexample.R;

import java.util.ArrayList;

public class CanvasView extends View
{
    private static final String TAG = CanvasView.class.getSimpleName();
    private static final float TOLERANCE = 5.0F;
    private ArrayList<CustomBitmap> addedBitmaps;
    private float canvasX;
    private float canvasXend;
    private float canvasY;
    private float canvasYend;
    Context context;
    boolean hasItemOnFocus;
    int itemOnFocus;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private float mX;
    private float mY;

    public CanvasView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        setSaveEnabled(true);
        this.mPaint = new Paint();
        this.addedBitmaps = new ArrayList<CustomBitmap>();
        this.hasItemOnFocus = false;
    }

    private void moveTouch(float x, float y) {
        float f1 = Math.abs(x - mX);
        float f2 = Math.abs(y - mY);
        if ((f1 >= TOLERANCE) || (f2 >= TOLERANCE)) {
            mX = x;
            mY = y;
            if ( (x >= canvasX)
                    && (x < canvasXend - addedBitmaps.get(itemOnFocus).getBitmap().getWidth() / 2)
                    && (y >= canvasY)
                    && (y < canvasYend - addedBitmaps.get(itemOnFocus).getBitmap().getHeight() / 2) ) {
                Log.d("MOVE", "x: " + x + " y: " + y);
                if (hasItemOnFocus) {
                    addedBitmaps.get(itemOnFocus).setX(mX);
                    addedBitmaps.get(itemOnFocus).setY(mY);
                    Log.d(TAG, itemOnFocus + "");
                }
            }
        }
    }

    private void startTouch(float x, float y) {
        for (int i = 0; i<addedBitmaps.size(); i++) {
            if (this.addedBitmaps.get(i).isInBitmap(x, y)) {
                itemOnFocus = i;
                Log.d(TAG, itemOnFocus + "");
                hasItemOnFocus = true;
                break;
            }
            mX = x;
            mY = y;
            hasItemOnFocus = false;
        }
    }

    private void endTouch() {
        hasItemOnFocus = false;
        if (!addedBitmaps.isEmpty()) {
            Toast.makeText(this.context, "Bitmap " + itemOnFocus + " set on cords X: " + addedBitmaps.get(itemOnFocus).getX() + " Y: " + addedBitmaps.get(this.itemOnFocus).getY(), Toast.LENGTH_SHORT).show();
        }
        itemOnFocus = 0;
    }

    public void addNewElement() {
        CustomBitmap localCustomBitmap = new CustomBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher), getWidth()/2, getHeight()/2);
        addedBitmaps.add(localCustomBitmap);
        invalidate();
    }

    public void clearCanvas() {
        addedBitmaps.clear();
        itemOnFocus = 0;
        hasItemOnFocus = false;
        invalidate();
    }

    public ArrayList<CustomBitmap> getAddedBitmaps() {
        return this.addedBitmaps;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!addedBitmaps.isEmpty()) {
            for (int i = 0; i < addedBitmaps.size(); i++) {
                canvas.drawBitmap(addedBitmaps.get(i).getBitmap(),
                        addedBitmaps.get(i).getX(),
                        addedBitmaps.get(i).getY(),
                        mPaint);
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        canvasX = getX();
        canvasXend = (canvasX + w);
        canvasY = getY();
        canvasYend = (canvasY + h);
        Log.d("INITIAL", "canvasX: " + canvasX + " canvasXend: " + canvasXend);
        Log.d("INITIAL", "canvasY: " + canvasY + " canvasYend: " +canvasYend);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                moveTouch(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                endTouch();
                invalidate();
                break;
        }
        return true;
    }

    public void setAddedBitmaps(ArrayList<CustomBitmap> arrayList) {
        this.addedBitmaps = arrayList;
    }
}