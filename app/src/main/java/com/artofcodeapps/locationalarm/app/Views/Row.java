package com.artofcodeapps.locationalarm.app.Views;

import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artofcodeapps.locationalarm.app.Views.textviewoptions.TwOptionsBuilder;

/**
 * Created by Pete on 14.4.2014.
 */
public class Row extends LinearLayout {
    private float[] weights;

    public Row(Context context, float[] weights){
        super(context);
        this.weights = weights;
    }

    public TextView insertText(String text, int height, int weightIndex){
        TwOptionsBuilder build = new TwOptionsBuilder(text, getContext());
        build.fontSize(TypedValue.COMPLEX_UNIT_SP, 16)
                .ellipsize(TextUtils.TruncateAt.END)
                .singleLine(true);
        TextView tw = build.getTextView();
        tw.setLayoutParams(getParams(height, weightIndex));
        this.addView(tw);
        return tw;
    }

    public Button insertButton(int text, int height, int weightIndex){
        Button btn = new Button(getContext());
        btn.setText(text);
        btn.setLayoutParams(getParams(height, weightIndex));
        this.addView(btn);
        return btn;
    }

    private LinearLayout.LayoutParams getParams(int height, int weightIndex){
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(0, height, weights[weightIndex]);
        params.setMargins(1,1, 1, 1);
        return params;
    }


}

