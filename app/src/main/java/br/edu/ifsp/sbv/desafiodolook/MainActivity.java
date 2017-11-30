package br.edu.ifsp.sbv.desafiodolook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.sbv.desafiodolook.adapter.DuelAdapter;
import br.edu.ifsp.sbv.desafiodolook.model.Album;
import br.edu.ifsp.sbv.desafiodolook.model.Duel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 1;
    public static JSONObject dataResult = new JSONObject();

    private Context mContext = MainActivity.this;
    private GridView mGridView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL",0);
        boolean isLogged = preferences.getBoolean("isLogged",false);

        if (isLogged) {
            setContentView(R.layout.activity_main);
            Log.d(TAG,"onCreate: starting in Main.");

            Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
            TextView txtTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);

            mGridView = (GridView) findViewById(R.id.gridView);

            String url="http://www.appointweb.com/desafioDoLookApp/controller/duel/get_duel.php";

            requestQueue = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            List<Duel> listDuel = new ArrayList<>();

                            try {
                                JSONObject jsonDuels =
                                        response.getJSONObject("duels");
                                JSONArray jsonDuel =
                                        jsonDuels.getJSONArray("duel");

                                for (int i = 0; i < jsonDuel.length(); i++) {
                                    JSONObject jsonCarroItem =
                                            jsonDuel.getJSONObject(i);
                                    Integer duelID  =
                                            Integer.parseInt(jsonCarroItem.getString("duelID"));
                                    Integer leftAlbumID  =
                                            Integer.parseInt(jsonCarroItem.getString("leftAlbumID"));
                                    Integer rightAlbumID  =
                                            Integer.parseInt(jsonCarroItem.getString("rightAlbumID"));
                                    String urlPicture1 =
                                            jsonCarroItem.getString("urlPicture1");
                                    String urlPicture2 =
                                            jsonCarroItem.getString("urlPicture2");
                                    Integer userInfoID1  =
                                            Integer.parseInt(jsonCarroItem.getString("userInfoID1"));
                                    Integer userInfoID2  =
                                            Integer.parseInt(jsonCarroItem.getString("userInfoID2"));

                                    Duel duel = new Duel(duelID, new Album(leftAlbumID, userInfoID1, urlPicture1),
                                                                new Album(rightAlbumID, userInfoID2, urlPicture2));
                                    listDuel.add(duel);
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                            }

                            //aa();
                            //setListAdapter(new UserInfoAdapter(getApplicationContext(), listAlbum));
                            //lvCabos.setAdapter(new AlbumAdapter(getApplicationContext(), listAlbum));
                            mGridView.setAdapter(new DuelAdapter(getApplicationContext(),listDuel));

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Erro!", Toast.LENGTH_SHORT).show();
                        }
                    });
            //add request to queue
            requestQueue.add(jsonObjectRequest);

            txtTitle.setText("Desafios");
            txtTitle.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/sweetsensations.ttf"));

            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);

            DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            setupFooterNavigationView();
        } else {
            Intent intent = new Intent(mContext, HomeActivity.class);
            mContext.startActivity(intent);
            Log.d(TAG, "onCreate: starting in Home.");
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL",0);
        boolean isLogged = preferences.getBoolean("isLogged",false);

        if (isLogged) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button_green, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_looks) {
            Intent intent1 = new Intent(mContext, ProfileActivity.class);
            mContext.startActivity(intent1);
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupFooterNavigationView(){
        Log.d(TAG,"setupFooterNavigationView: setting up FooterNavigationView.");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.footerNavigation);
        FooterNavigationViewHelper.setupFooterNavigationView(bottomNavigationViewEx);
        FooterNavigationViewHelper.enableNavigation(mContext,bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL",0);
        boolean isLogged = preferences.getBoolean("isLogged",false);

        if (!isLogged) {
            finish();
        }
    }
}