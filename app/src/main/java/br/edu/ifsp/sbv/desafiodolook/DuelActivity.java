package br.edu.ifsp.sbv.desafiodolook;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.sbv.desafiodolook.adapter.PhotoAdapter;
import br.edu.ifsp.sbv.desafiodolook.connection.VolleySingleton;
import br.edu.ifsp.sbv.desafiodolook.model.Album;

/**
 * Created by Adriel on 12/1/2017.
 */

public class DuelActivity extends AppCompatActivity {

    private static final String TAG = "DuelActivity";
    private static final int ACTIVITY_NUM = 5;

    private Context mContext = DuelActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duel);
        Log.d(TAG, "onCreate: starting in Duel.");

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarBack);
        ImageView icoBack = (ImageView)toolbar.findViewById(R.id.ico_bar_back);
        TextView txtTitle = (TextView)toolbar.findViewById(R.id.toolbar_title_back);
        NetworkImageView netImgViewLeft = (NetworkImageView) findViewById(R.id.netImgViewDuelLeft);
        NetworkImageView netImgViewRight = (NetworkImageView) findViewById(R.id.netImgViewDuelRight);

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("photoDuelLeft") && extras.containsKey("photoDuelRight")) {
            netImgViewLeft.setImageUrl(extras.getSerializable("photoDuelLeft").toString() , VolleySingleton.getInstance(mContext).getImageLoader());
            netImgViewRight.setImageUrl(extras.getSerializable("photoDuelRight").toString() , VolleySingleton.getInstance(mContext).getImageLoader());
        }else
            Toast.makeText(mContext, "Erro!", Toast.LENGTH_SHORT).show();


//        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL",0);
//        int userID = preferences.getInt("userID", 0);
//
//        //String url="http://www.appointweb.com/desafioDoLookApp/controller/album/get_album.php";
//        String url="http://appointweb.com/Imagem/testImagens.json";
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        List<Album> listPhotos = new ArrayList<>();
//
//                        try {
//                            JSONObject jsonPhotos =
//                                    response.getJSONObject("users");
//                            JSONArray jsonPhoto =
//                                    jsonPhotos.getJSONArray("userInfo");
//
//                            for (int i = 0; i < jsonPhoto.length(); i++) {
//                                JSONObject jsonPhotoItem =
//                                        jsonPhoto.getJSONObject(i);
//                                Integer userInfoID  =
//                                        Integer.parseInt(jsonPhotoItem.getString("userInfoID"));
//                                String thumbnail =
//                                        jsonPhotoItem.getString("urlPicture");
//
//                                Album photo = new Album(userInfoID,userInfoID, thumbnail);
//                                listPhotos.add(photo);
//                            }
//                        } catch (Exception e){
//                            e.printStackTrace();
//                        }
//
//                        lvPhotos.setAdapter(new PhotoAdapter(getApplicationContext(), listPhotos));
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Erro!", Toast.LENGTH_SHORT).show();
//                    }
//                });
//        //add request to queue
//        requestQueue.add(jsonObjectRequest);

        txtTitle.setText("Duel");
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
