package com.example.myproject.AdditionalEvent.Navigation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myproject.R;


public class NavigationLayout extends FrameLayout
{
    public NavigationLayout(Context context, ConstraintLayout parent)
    {
        super(context);
        initView(context,parent);
    }

    public void initView(final Context context,ConstraintLayout parent)
    {
        // надуваем любой xml файл разметки
        View view= LayoutInflater.from(context).inflate(R.layout.view_drawer_layout,parent,true);
    }
}