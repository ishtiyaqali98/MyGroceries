package com.aliindustries.groceryshoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

public class createList extends AppCompatActivity {

    EditText editText;
   MaterialButton materialButton;
   DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        editText = (EditText) findViewById(R.id.edittext1);
        materialButton = (MaterialButton) findViewById(R.id.submitbtn);
        myDb = DatabaseHelper.getInstance(createList.this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.ic_launcher_background));
        }

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String v_title = editText.getText().toString().trim();
                v_title = v_title.replace("'", "");

                boolean bc = v_title.matches(".*[a-zA-Z].*");
                if(bc == false && !myDb.titleExists(v_title) ) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Enter a shopping list", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                else if (bc == true && myDb.titleExists(v_title) == true) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "This shopping list already exists!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    Boolean a = myDb.insertData(v_title, getResources().getString(R.string.itemidentifier), 0, 0);
                    if (a == true) {
                        startActivity(new Intent(createList.this, MainActivity.class));

                    } else {
                        Toast.makeText(getApplicationContext(), "data not inserted", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });


    }
    public void backbtn4(View v) {

        startActivity(new Intent(createList.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

    }


}
