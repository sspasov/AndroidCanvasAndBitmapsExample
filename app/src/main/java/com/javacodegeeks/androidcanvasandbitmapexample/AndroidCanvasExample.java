package com.javacodegeeks.androidcanvasandbitmapexample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.javacodegeeks.androidcanvasandbitmapexample.R;

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

    protected void onSaveInstanceState(Bundle paramBundle) {
        paramBundle.putParcelableArrayList("savedBitmaps", this.customCanvas.getAddedBitmaps());
        paramBundle.putInt("oldw", this.customCanvas.getWidth());
        paramBundle.putInt("oldh", this.customCanvas.getHeight());
        super.onSaveInstanceState(paramBundle);
    }

    public void onWindowFocusChanged(boolean paramBoolean) {
        if ((this.oldw != 0) && (this.oldh != 0)) {
            float f1 = this.customCanvas.getWidth() / this.oldw;
            Log.d("DIMENTIONS", "w: " + this.customCanvas.getWidth() + " oldw: " + this.oldw + " coef: " + f1);
            float f2 = this.customCanvas.getHeight() / this.oldh;
            Log.d("DIMENTIONS", "h: " + this.customCanvas.getHeight() + " oldh: " + this.oldh + " coef: " + f2);
            for (int i = 0; i < this.arrayList.size(); i++) {
                this.arrayList.get(i).setX(f1 * this.arrayList.get(i).getX());
                this.arrayList.get(i).setY(f2 * this.arrayList.get(i).getY());
            }
            this.customCanvas.setAddedBitmaps(this.arrayList);
        }
        super.onWindowFocusChanged(paramBoolean);
    }
}