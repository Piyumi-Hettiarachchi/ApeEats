package com.example.ApeEats;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ProductList extends AppCompatActivity {

    LinearLayout l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        TextView title;

        title = findViewById(R.id.title);
        title.setText("My Product");


    l1 = findViewById(R.id.DynamicLayout);


        for(int i=0;i<1;i++){
        generateLayout();
    }
}

    private void generateLayout() {
        ImageView iv = new ImageView(this);
        iv.setImageResource(R.drawable.image);
        int width = 200;
        int height = 200;
        LinearLayout.LayoutParams pSize = new LinearLayout.LayoutParams(width,height,2);
        pSize.gravity= Gravity.CENTER;
        iv.setLayoutParams(pSize);


        LinearLayout layout1 = new LinearLayout(this);

        layout1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,14));
        layout1.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.MarginLayoutParams params = (LinearLayout.MarginLayoutParams) layout1.getLayoutParams();
        params.topMargin = 10;


        layout1.setBackgroundColor(getResources().getColor(R.color.green1));

        //children of layout1
        LinearLayout layout2 = new LinearLayout(this);
        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,11));
        layout2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout layout3 = new LinearLayout(this);
        layout3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT,1));
        layout3.setOrientation(LinearLayout.VERTICAL);

        l1.addView(layout1);
        layout1.addView(iv);
        layout1.addView(layout2);
        layout1.addView(layout3);

        //children of layout2 LinearLayout

        TextView tv1 = new TextView(this);
        TextView tv2 = new TextView(this);

        LinearLayout.LayoutParams lpt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lpt.gravity= Gravity.CENTER_VERTICAL;
        lpt.topMargin=50;
        tv1.setLayoutParams(lpt);

        tv1.setText("Food1");
        tv2.setText("description");

        tv1.setTextSize(20);
        tv2.setTextSize(16);

        layout2.addView(tv1);
        layout2.addView(tv2);

        //children of layout3 LinearLayout

        Button btn1 = new Button(this);
        Button btn2 = new Button(this);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity= Gravity.CENTER;
        lp.topMargin= 10;
        lp.bottomMargin= 10;
        btn1.setLayoutParams(lp);
        btn2.setLayoutParams(lp);


        btn1.setBackground(getResources().getDrawable(R.drawable.relative_border_outline));
        btn2.setBackground(getResources().getDrawable(R.drawable.relativeborder));


        btn1.setText("Edit");
        btn2.setText("Delete");

        layout3.addView(btn1);
        layout3.addView(btn2);
    }

}