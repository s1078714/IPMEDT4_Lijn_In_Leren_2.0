package com.android.lijninleren;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;



public class SearchActivity extends Activity {
    
   // List view
    ListView lv;
    
   // Listview Adapter
   ArrayAdapter<String> adapter;
    
   // Search EditText
   EditText inputSearch;
    
    
   // ArrayList for Listview
   ArrayList<HashMap<String, String>> productList;

   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.search_interface);
        
       // Listview Data
       String products[] = {"Dell Inspiron", "HTC One X", "HTC Wildfire S", "HTC Sense", "HTC Sensation XE",
                               "iPhone 4S", "Samsung Galaxy Note 800",
                               "Samsung Galaxy S3", "MacBook Air", "Mac Mini", "MacBook Pro"};
        
       lv = (ListView) findViewById(R.id.list_view2);
       inputSearch = (EditText) findViewById(R.id.inputSearch);
        
       // Adding items to listview
       adapter = new ArrayAdapter<String>(this, R.layout.lijst_item, R.id.product_name, products);
       lv.setAdapter(adapter);       
       
       
        /////////////////////////////////////////////////////////////
       inputSearch.addTextChangedListener(new TextWatcher() {
           
           @Override
           public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
               // When user changed the Text
               SearchActivity.this.adapter.getFilter().filter(cs);   
           }
            
           @Override
           public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                   int arg3) {
               // TODO Auto-generated method stub
                
           }
            
           @Override
           public void afterTextChanged(Editable arg0) {
               // TODO Auto-generated method stub                          
           }
           
           
       });
       
       lv.setOnItemClickListener(new OnItemClickListener() {
    	   @Override
    	   public void onItemClick(AdapterView parent, View view,int position, long _id) {
    	   String values = adapter.getItem(position);

    	   // TODO Auto-generated method stub
    	   Intent i =null;

    	   if (values=="Dell Inspiron") {
    	   i=new Intent(SearchActivity.this, DellInspiron.class);
    	   startActivity(i);}
    	

    	   }
    	   });
   }





   
   
   
}
