package com.jiuli.library.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class LibraryAdater<E> extends BaseAdapter {

	public Context context;
	public List<E> mList;
	public LibraryAdater(Context context,List<E> mList){
		this.context = context;
		this.mList = mList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public E getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int id) {
		// TODO Auto-generated method stub
		return id;
	}

	
	public static class ViewHolder {
	    // I added a generic return type to reduce the casting noise in client code  
	    @SuppressWarnings("unchecked")  
	    public static <T extends View> T get(View view, int id) {
	        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();  
	        if (viewHolder == null) {  
	            viewHolder = new SparseArray<View>();  
	            view.setTag(viewHolder);  
	        }  
	        View childView = viewHolder.get(id);  
	        if (childView == null) {  
	            childView = view.findViewById(id);  
	            viewHolder.put(id, childView);  
	        }  
	        return (T) childView;  
	    }  
	}
	
	

}
