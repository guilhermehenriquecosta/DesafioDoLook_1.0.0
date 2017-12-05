package br.edu.ifsp.sbv.desafiodolook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.CircularNetworkImageView;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.sbv.desafiodolook.R;
import br.edu.ifsp.sbv.desafiodolook.FooterNavigationViewHelper;
import br.edu.ifsp.sbv.desafiodolook.adapter.FriendAdapter;
import br.edu.ifsp.sbv.desafiodolook.adapter.RankingAdapter;
import br.edu.ifsp.sbv.desafiodolook.connection.VolleySingleton;
import br.edu.ifsp.sbv.desafiodolook.model.Friend;
import br.edu.ifsp.sbv.desafiodolook.model.User;

/**
 * Created by Guilherme on 10/11/2017.
 */

public class RankingActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "RankingActivity";
    private static final int ACTIVITY_NUM = 3;

    private Context mContext = RankingActivity.this;
    private GridView mGridView;
    private ListView lvFriends;
    private ListView lvRanking;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        lvRanking = (ListView) findViewById(R.id.lvRanking);
        mGridView = (GridView) findViewById(R.id.gridView);
        mGridView.setVisibility(View.INVISIBLE);
        lvFriends = (ListView) findViewById(R.id.lvFriends);
        lvFriends.setVisibility(View.INVISIBLE);

        String url="http://www.appointweb.com/desafioDoLookApp/controller/users/get_ranking.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final List<User> listRanking = new ArrayList<>();

                        try {
                            JSONObject jsonRanking = response.getJSONObject("ranking");
                            JSONArray jsonUsers = jsonRanking.getJSONArray("users");

                            for (int i = 0; i < jsonUsers.length(); i++) {
                                JSONObject jsonUserItem = jsonUsers.getJSONObject(i);

                                int userInfoID  = Integer.parseInt(jsonUserItem.getString("userInfoID"));
                                String userName = jsonUserItem.getString("userName");
                                String email = jsonUserItem.getString("email");
                                String urlAvatar = jsonUserItem.getString("urlAvatar");
                                int win  = Integer.parseInt(jsonUserItem.getString("win"));
                                int tie  = Integer.parseInt(jsonUserItem.getString("tie"));
                                int loss  = Integer.parseInt(jsonUserItem.getString("loss"));
                                float per  = Float.parseFloat(jsonUserItem.getString("per"));

                                User user = new User(userInfoID, email, userName, urlAvatar, win, tie, loss, per);
                                listRanking.add(user);
                            }
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        lvRanking.setAdapter(new RankingAdapter(getApplicationContext(), listRanking));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.strError + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);

        txtTitle.setText(R.string.strRanking);
        txtTitle.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/sweetsensations.ttf"));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView txtViewNameProfile = (TextView) headerView.findViewById(R.id.txtViewNameNav);
        TextView txtViewEmailProfile = (TextView) headerView.findViewById(R.id.txtViewEmailNav);
        CircularNetworkImageView netImgViewProfile = (CircularNetworkImageView) headerView.findViewById(R.id.netImgViewProfileNav);

        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL", 0);
        int userID = preferences.getInt("userID", 0);
        getUserProfile(userID, txtViewNameProfile, txtViewEmailProfile, netImgViewProfile);

        setupFooterNavigationView();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_my_looks) {
            SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL",0);
            int userID = preferences.getInt("userID",0);

            if (userID == 0) {
                Toast.makeText(mContext, R.string.strErrorRegister, Toast.LENGTH_SHORT).show();
            } else {
                Intent intent1 = new Intent(mContext, ProfileActivity.class);
                mContext.startActivity(intent1);
            }
        } else if (id == R.id.nav_about) {
            Intent intent1 = new Intent(mContext, AboutActivity.class);
            mContext.startActivity(intent1);
        } else if (id == R.id.nav_logoff) {
            SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLogged", false);
            editor.putInt("userID", 0);
            editor.commit();
            Intent intent1 = new Intent(mContext, HomeActivity.class);
            mContext.startActivity(intent1);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupFooterNavigationView(){
        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL",0);
        int userID = preferences.getInt("userID",0);

        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.footerNavigation);
        FooterNavigationViewHelper.setupFooterNavigationView(bottomNavigationViewEx);
        FooterNavigationViewHelper.enableNavigation(mContext,bottomNavigationViewEx,userID);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    private void getUserProfile(int userID, final TextView txtViewNameProfile, final TextView txtViewEmailProfile, final CircularNetworkImageView netImgViewProfile){

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
                            netImgViewProfile.setImageUrl(urlAvatar, VolleySingleton.getInstance(mContext).getImageLoader());

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

//    @Override
//    protected void onStop() {
//        super.onStop();
////        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL",0);
////        boolean isLogged = preferences.getBoolean("isLogged",false);
////        if (!isLogged) {
////            finish();
////        }
//        finish();
//    }
}

