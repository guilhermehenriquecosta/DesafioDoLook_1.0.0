package br.edu.ifsp.sbv.desafiodolook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import br.edu.ifsp.sbv.desafiodolook.model.Duel;

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

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarBack);
        ImageView icoBack = (ImageView)toolbar.findViewById(R.id.ico_bar_back);
        TextView txtTitle = (TextView)toolbar.findViewById(R.id.toolbar_title_back);
        NetworkImageView netImgViewLeft = (NetworkImageView) findViewById(R.id.netImgViewDuelLeft);
        NetworkImageView netImgViewRight = (NetworkImageView) findViewById(R.id.netImgViewDuelRight);
        FloatingActionButton btnVoteLeft = (FloatingActionButton) findViewById(R.id.ico_confirmLeft);
        FloatingActionButton btnVoteTie = (FloatingActionButton) findViewById(R.id.ico_confirmTie);
        FloatingActionButton btnVoteRight = (FloatingActionButton) findViewById(R.id.ico_confirmRight);

        Intent intent = getIntent();
        final Duel duelSelect = (Duel) intent.getSerializableExtra("duelSelect");

        if (duelSelect != null){
            netImgViewLeft.setImageUrl(duelSelect.getAlbumLeft().getUrlPicture() , VolleySingleton.getInstance(mContext).getImageLoader());
            netImgViewRight.setImageUrl(duelSelect.getAlbumRight().getUrlPicture() , VolleySingleton.getInstance(mContext).getImageLoader());
        }else
            Toast.makeText(mContext, "Erro!", Toast.LENGTH_SHORT).show();

        btnVoteLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "ID: " + duelSelect.getDuelID() + "v: 0",Toast.LENGTH_SHORT ).show();
            }
        });

        btnVoteTie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "ID: " + duelSelect.getDuelID() + "v: 2",Toast.LENGTH_SHORT ).show();
            }
        });

        btnVoteRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "ID: " + duelSelect.getDuelID() + "v: 1",Toast.LENGTH_SHORT ).show();
            }
        });

        txtTitle.setText(R.string.strDuel);
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
