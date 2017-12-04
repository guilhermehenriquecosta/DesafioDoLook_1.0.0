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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.edu.ifsp.sbv.desafiodolook.adapter.DuelAdapter;
import br.edu.ifsp.sbv.desafiodolook.adapter.PhotoAdapter;
import br.edu.ifsp.sbv.desafiodolook.connection.VolleySingleton;
import br.edu.ifsp.sbv.desafiodolook.model.Album;
import br.edu.ifsp.sbv.desafiodolook.model.Duel;
import br.edu.ifsp.sbv.desafiodolook.model.Friend;
import br.edu.ifsp.sbv.desafiodolook.model.User;

/**
 * Created by Guilherme on 15/11/2017.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    private static final int ACTIVITY_NUM = 4;

    private Context mContext = ProfileActivity.this;
    private ListView lvPhotos;
    private User userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBack);
        TextView txtAddDel = (TextView) findViewById(R.id.txtAddDel);
        ImageView icoBack = (ImageView) toolbar.findViewById(R.id.ico_bar_back);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.toolbar_title_back);
        lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        TextView txtViewNameProfile = (TextView) findViewById(R.id.txtViewNameProfile);
        TextView txtViewEmailProfile = (TextView) findViewById(R.id.txtViewEmailProfile);

        Intent intent = getIntent();
        final Friend userSelect = (Friend) intent.getSerializableExtra("userSelect");
        final int userID;

        if (userSelect != null) {
            userID = userSelect.getUserFollow().getUserID();
            txtAddDel.setVisibility(View.VISIBLE);
        } else {
            SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL", 0);
            userID = preferences.getInt("userID", 0);
            txtAddDel.setVisibility(View.INVISIBLE);
        }

        getUserProfile(userID, txtViewNameProfile, txtViewEmailProfile);
//        txtViewNameProfile.setText(userProfile.getUserName());
//        txtViewEmailProfile.setText(userProfile.getEmail());

        String url="http://www.appointweb.com/desafioDoLookApp/controller/album/get_album.php?userID=" + userID;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<Album> listPhotos = new ArrayList<>();

                        try {
                            JSONObject jsonPhoto = response.getJSONObject("photo");
                            JSONArray jsonPhotos = jsonPhoto.getJSONArray("photos");

                            for (int i = 0; i < jsonPhotos.length(); i++) {
                                JSONObject jsonPhotoItem = jsonPhotos.getJSONObject(i);
                                int albumID = Integer.parseInt(jsonPhotoItem.getString("albumID"));
                                String urlPicture = jsonPhotoItem.getString("urlPicture");
                                String dateCreationStr = jsonPhotoItem.getString("dateCreation");
                                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date dataCreation = format.parse(dateCreationStr);
                                Album photo = new Album(albumID,userID, urlPicture,dataCreation);
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

    private void getUserProfile(int userID, final TextView txtViewNameProfile, final TextView txtViewEmailProfile){

        String url = "http://www.appointweb.com/desafioDoLookApp/controller/users/get_user.php?userID=" + userID;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonUser = response.getJSONObject("user");
                            JSONArray jsonUsers = jsonUser.getJSONArray("users");

                            JSONObject jsonUserItem = jsonUsers.getJSONObject(0);
                            int userID = Integer.parseInt(jsonUserItem.getString("userInfoID"));
                            String userName = jsonUserItem.getString("userName");
                            String email = jsonUserItem.getString("email");
                            String urlAvatar = jsonUserItem.getString("urlAvatar");

                            txtViewNameProfile.setText(userName);
                            txtViewEmailProfile.setText(email);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.strError + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);

    }
}