package br.edu.ifsp.sbv.desafiodolook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.sbv.desafiodolook.adapter.DuelAdapter;
import br.edu.ifsp.sbv.desafiodolook.adapter.PhotoAdapter;
import br.edu.ifsp.sbv.desafiodolook.model.Album;
import br.edu.ifsp.sbv.desafiodolook.model.Duel;

/**
 * Created by Guilherme on 15/11/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;

    private Context mContext = ProfileActivity.this;
    private ListView lvPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarBack);
        ImageView icoBack = (ImageView)toolbar.findViewById(R.id.ico_bar_back);
        TextView txtTitle = (TextView)toolbar.findViewById(R.id.toolbar_title_back);
        lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL",0);
        int userID = preferences.getInt("userID", 0);

        String url="http://appointweb.com/Imagem/testImagens.json";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Album> listPhotos = new ArrayList<>();

                        try {
                            JSONObject jsonPhotos = response.getJSONObject("users");
                            JSONArray jsonPhoto = jsonPhotos.getJSONArray("userInfo");

                            for (int i = 0; i < jsonPhoto.length(); i++) {
                                JSONObject jsonPhotoItem = jsonPhoto.getJSONObject(i);
                                Integer userInfoID = Integer.parseInt(jsonPhotoItem.getString("userInfoID"));
                                String thumbnail = jsonPhotoItem.getString("urlPicture");

                                Album photo = new Album(userInfoID,userInfoID, thumbnail);
                                listPhotos.add(photo);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        lvPhotos.setAdapter(new PhotoAdapter(getApplicationContext(), listPhotos));

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.strError + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);

        txtTitle.setText(R.string.strProfile);
        txtTitle.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/sweetsensations.ttf"));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        icoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}