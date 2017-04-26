package com.jiuli.library.adapter;

import android.view.ViewGroup;
import android.widget.CompoundButton;


public interface LibraryOnItemChildCheckedChangeListener {
    void onItemChildCheckedChanged(ViewGroup parent, CompoundButton childView, int position, boolean isChecked);
}