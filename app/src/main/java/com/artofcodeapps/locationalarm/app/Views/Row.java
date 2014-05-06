package com.artofcodeapps.locationalarm.app.Views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.artofcodeapps.locationalarm.app.Views.textviewoptions.TwOptionsBuilder;

/**
 * Created by Pete on 14.4.2014.
 */
//todo this could probably be done in xml, would be more clear
public class Row extends LinearLayout {
    private float[] weights;
    private LinearLayout leftColumn;
    private LinearLayout rightColumn;
    int baseline;

    public Row(Context context, float[] weights){
        super(context);
        //  this.weights = weights;
        createColumns(context);
        LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(params);
       // this.setPadding(10, 10, 10, 10);
    }

    private void createColumns(Context context){
        this.leftColumn = new LinearLayout(context);
        this.rightColumn = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.FILL_PARENT, 10);
        LinearLayout.LayoutParams rightparams = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
        leftColumn.setLayoutParams(params);
       // leftColumn.setBackgroundColor(Color.GREEN);
        rightColumn.setLayoutParams(rightparams);
        leftColumn.setGravity(Gravity.LEFT);
        rightColumn.setGravity(Gravity.RIGHT);
      // rightColumn.setBackgroundColor(Color.YELLOW);
        this.addView(leftColumn);
        this.addView(rightColumn);
    }

    public TextView insertText(String text, int height, int weightIndex){
        TwOptionsBuilder build = new TwOptionsBuilder(text, getContext());
        build.fontSize(TypedValue.COMPLEX_UNIT_SP, 18)
                .ellipsize(TextUtils.TruncateAt.END)
                .singleLine(true);
        TextView tw = build.getTextView();
        tw.setLayoutParams(getParams(height, weightIndex));
        leftColumn.addView(tw);
        this.baseline = tw.getBaseline();
        return tw;
    }

    public ImageButton insertImageButton(int resID, int height, int weightIndex){
        ImageButton btn = new ImageButton(getContext());
        btn.setImageResource(resID);
        LinearLayout.LayoutParams params = getParams(height, weightIndex);
        //  params.gravity = Gravity.CENTER_VERTICAL;
        btn.setLayoutParams(params);
        btn.setBackground(null); //todo support older apis
        btn.setBaselineAlignBottom(true);
        //   btn.setBaseline(baseline);
        rightColumn.addView(btn);
        return btn;
    }

    private LinearLayout.LayoutParams getParams(int height, int weightIndex){
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, height);
        params.setMargins(1,1, 1, 1);
        params.gravity = Gravity.CENTER_VERTICAL;
        return params;
    }
}

