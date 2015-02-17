package com.mostcho.androidcanvasandbitmapexample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class AndroidCanvasExample extends Activity {
    ArrayList<CustomBitmap> arrayList;
    private CanvasView customCanvas;
    int oldh;
    int oldw;

    public void addNew(View paramView) {
        this.customCanvas.addNewElement();
    }

    public void clearCanvas(View paramView) {
        this.customCanvas.clearCanvas();
    }

    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_main);
        this.customCanvas = ((CanvasView)findViewById(R.id.signature_canvas));
        if (paramBundle != null) {
            this.oldw = paramBundle.getInt("oldw");
            this.oldh = paramBundle.getInt("oldh");
            this.arrayList = paramBundle.getParcelableArrayList("savedBitmaps");
        }
    }

    protected void onSaveInstanceState(@NonNull Bundle paramBundle) {
        paramBundle.putParcelableArrayList("savedBitmaps", this.customCanvas.getAddedBitmaps());
        paramBundle.putInt("oldw", this.customCanvas.getWidth());
        paramBundle.putInt("oldh", this.customCanvas.getHeight());
        super.onSaveInstanceState(paramBundle);
    }

    public void onWindowFocusChanged(boolean paramBoolean) {
        if ((this.oldw != 0) && (this.oldh != 0)) {
//            float f1 = this.customCanvas.getWidth() / this.oldw;  // incorrect

            float currentWidth = this.customCanvas.getWidth(); // get current canvas width
            float currentHeight = this.customCanvas.getHeight();  // get current canvas height

//            Log.d("DIMENTIONS", "w: " + this.customCanvas.getWidth() + " oldw: " + this.oldw + " coef: " + f1);
//            float f2 = this.customCanvas.getHeight() / this.oldh; // incorrect
//
//            Log.d("DIMENTIONS", "h: " + this.customCanvas.getHeight() + " oldh: " + this.oldh + " coef: " + f2);

            for (int i = 0; i < this.arrayList.size(); i++) {
                CustomBitmap item = this.arrayList.get(i); // and the problem with X and Y equals 0 disappears :)

                float coefficientX = this.oldw / (item.getX() + item.getHalfWidth()); // add getHalfWidth to find the center X of the image
                float coefficientY = this.oldh / (item.getY() + item.getHalfHeight()); // add getHalfHeight to find the center Y of the image

                Log.d("DIMENTIONS", "w: " + currentWidth + " oldw: " + this.oldw + " coef: " + coefficientX);
                Log.d("DIMENTIONS", "h: " + currentHeight + " oldh: " + this.oldh + " coef: " + coefficientY);

                float nextX = currentWidth / coefficientX;
                float nextY = currentHeight / coefficientY;

                item.setX(nextX);
                item.setY(nextY);
                this.arrayList.set(i, item);
            }
            this.customCanvas.setAddedBitmaps(this.arrayList);
        }
        super.onWindowFocusChanged(paramBoolean);
    }
}