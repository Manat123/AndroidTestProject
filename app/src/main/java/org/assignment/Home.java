package org.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.assignment.adapter.UserAdapter;

import org.assignment.modals.Users;
import org.assignment.utils.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    RecyclerView userList;
    ArrayList<Users> userArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userList = findViewById(R.id.user_list);

        getUserData();
    }

    private void getUserData() {
        StringRequest request = new StringRequest(Request.Method.GET,
                URL.USER + "?page=2", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray userArray = json.optJSONArray("data");
                    userData(userArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Home.this, "Something went wrong",
                        Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        queue.start();
    }

    private void userData(JSONArray userArray) throws JSONException {
        if (userArray.length() > 0) {
            for (int userList = 0; userList < userArray.length(); userList++) {
                JSONObject jsonObject = userArray.getJSONObject(userList);
                int id = jsonObject.getInt("id");
                String email = jsonObject.getString("email");
                String first_name = jsonObject.getString("first_name");
                String last_name = jsonObject.getString("last_name");
                String avatar = jsonObject.getString("avatar");
                userArrayList.add(new Users(id, first_name, last_name, email,avatar));
            }
        }
        UserAdapter userAdapter = new UserAdapter(userArrayList, this);
        userList.setAdapter(userAdapter);
        userList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        startActivity(new Intent(this, LoginActivity.class));
        return super.onOptionsItemSelected(item);
    }
}