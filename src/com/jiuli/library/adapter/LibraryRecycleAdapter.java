package com.jiuli.library.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by siberiawolf on 16/1/5.
 */
public abstract class LibraryRecycleAdapter<T> extends RecyclerView.Adapter {

    private List<T> data;
    private Context context;

    public LibraryRecycleAdapter(Context context,List<T> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public InnerViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        //返回一个ViewHolder
        InnerViewHolder innerViewHolder
                = new InnerViewHolder(LayoutInflater.from(context).inflate(getItemLayoutID(), viewGroup, false));
        return innerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        onBind((InnerViewHolder) holder, position);
    }

    public void onBind(InnerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 子类复写该方法传入item的id
     *
     * @return
     */
    public int getItemLayoutID() {
        return 0;
    }

    class InnerViewHolder extends RecyclerView.ViewHolder {

        private SparseArray<View> views = new SparseArray<View>();
        private View convertView;

        public InnerViewHolder(View view) {
            super(view);
            convertView = view;
        }

        public <T extends View> T getView(int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = convertView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
        }

    }

    public Object getItem(int position) {
        if (position >= data.size())
            return null;
        return data.get(position);
    }
}
