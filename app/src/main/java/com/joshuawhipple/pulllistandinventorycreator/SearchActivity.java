package com.joshuawhipple.pulllistandinventorycreator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText userSearch = (EditText)findViewById(R.id.userSearchTitle);
        Button btnSearch = (Button)findViewById(R.id.btnSearch);

        //
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utility.isEmpty(userSearch)) {
                    Intent intent = new Intent(getApplicationContext(),SearchResultsActivity.class);

                    //data is going to be sent to searchResultsActivity
                    intent.putExtra("userSearch",userSearch.getText().toString());

                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Please enter an item name.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.default_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when clicked, the home icon goes to the mainActivity and closes this activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        return super.onOptionsItemSelected(item);
    }
}
