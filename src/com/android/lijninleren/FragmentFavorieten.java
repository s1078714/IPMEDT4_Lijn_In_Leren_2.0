package com.android.lijninleren;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class FragmentFavorieten extends Fragment {
 
    public static Fragment newInstance(Context context) {
    	FragmentFavorieten f = new FragmentFavorieten();
 
        return f;
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_favorieten, null);
        
        // code
        
        return root;
    }
 
}