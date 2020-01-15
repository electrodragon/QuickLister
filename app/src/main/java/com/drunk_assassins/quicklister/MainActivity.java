package com.drunk_assassins.quicklister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String roll_no, name, email, phone_no, course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView courses_container = findViewById(R.id.courses_container);

        /* --------------  SPINNER CREATION START  ---------------------------------------*/
        /////////////////////////////////////////  Following Code Creates a Spinner of Course Names
        Spinner courses_spinner = findViewById(R.id.courses_spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[] {
                "Mobile Application Development", "CCTV", "Graphics", "Networking", "Safety"
        });
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courses_spinner.setAdapter(arrayAdapter);
        /* --------------  SPINNER CREATION END    ---------------------------------------*/


        /* ---------------  ITEM SELECTED LISTENER ON SPINNER START ---------------------------------------*/
        /////////////////////////////////////////  Setting onItemSelectedListener on courses_spinner
        courses_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                course = String.valueOf(parent.getItemAtPosition(position)); // Sets 'Selected Course' Name as value for Global Variable => course

                // ------ Creating HashMap ArrayList of usersData START ------------------
                List usersDataToHashMap = new ArrayList<HashMap<String, String>>();

                for (String[] userData: FireMethod.fetchArrayFromDB(position)) {
                    HashMap item = new HashMap<String, String>();

                    item.put("Roll no", userData[0]);
                    item.put("Name", userData[1]);
                    item.put("Email", userData[2]);
                    item.put("Phone No", userData[3]);
                    item.put("avatar", userData[4].split(":")[1].trim().equals("Male") ? R.drawable.male_avatar : R.drawable.female_avatar);

                    usersDataToHashMap.add(item);
                }
                // ------ Creating HashMap ArrayList of usersData END ------------------

                // Setting Adapter on courses_container (ListView)
                courses_container.setAdapter(new SimpleAdapter(MainActivity.this, usersDataToHashMap, R.layout.courses_list_schema,
                        new String[] {"avatar", "Roll no", "Name", "Email", "Phone No"},
                        new int[] {R.id.avatar, R.id.roll_no, R.id.name, R.id.email, R.id.phone_no}
                ));

                // Following Line Applies ContextMenu on Each Item in courses_Container
                registerForContextMenu(courses_container);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /* ---------------  ITEM SELECTED LISTENER ON SPINNER END ---------------------------------------*/

        /* ----------- SETTING LONG ITEM CLICK LISTENER ON ITEM IN COURSES_CONTAINER START --------------*/
        courses_container.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> myMap = (HashMap<String, String>) parent.getItemAtPosition(position);

                roll_no = myMap.get("Roll no").split(": ")[1];
                name = myMap.get("Name").split(": ")[1];
                email = myMap.get("Email").split(": ")[1];
                phone_no = myMap.get("Phone No").split(": ")[1];

                return false;
            }
        });

    } // onCreate Ends Here !

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Context Menu");
        menu.add(0, v.getId(), 0, "Call");
        menu.add(0, v.getId(), 0, "SMS");
        menu.add(0, v.getId(), 0, "EMAIL");
        menu.add(0, v.getId(), 0, "Share");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Call") {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone_no));
            startActivity(intent);
        } else if (item.getTitle() == "SMS") {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("sms:" + phone_no));
            intent.putExtra("sms_body", "Roll no: " + roll_no + "\nName: " + name);
            startActivity(intent);
        } else if (item.getTitle() == "EMAIL") {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Student Data !");
            intent.putExtra(Intent.EXTRA_TEXT, "Roll no: " + roll_no + "\nName: " + name);
            startActivity(intent);
        }
        return true;
    }
}
