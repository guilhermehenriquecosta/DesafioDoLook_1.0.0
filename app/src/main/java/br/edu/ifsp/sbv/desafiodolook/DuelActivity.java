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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.sbv.desafiodolook.adapter.PhotoAdapter;
import br.edu.ifsp.sbv.desafiodolook.connection.VolleySingleton;
import br.edu.ifsp.sbv.desafiodolook.connection.WebserviceTask;
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
        } else {
            Toast.makeText(mContext, "Erro!", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL", 0);
        final int userID = preferences.getInt("userID", 0);

        if (userID == 0) {
            btnVoteLeft.setVisibility(View.INVISIBLE);
            btnVoteRight.setVisibility(View.INVISIBLE);
            btnVoteTie.setVisibility(View.INVISIBLE);
        } else {
            btnVoteLeft.setVisibility(View.VISIBLE);
            btnVoteRight.setVisibility(View.VISIBLE);
            btnVoteTie.setVisibility(View.VISIBLE);
        }

        btnVoteLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(duelSelect.getDuelID(), userID,0);
            }
        });

        btnVoteTie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(duelSelect.getDuelID(), userID,2);
            }
        });

        btnVoteRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(duelSelect.getDuelID(), userID,1);
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

    private void insert(int duelID, int userInfoID, int vote){
        try {
            JSONObject data = new JSONObject();

            data.put("duelID", duelID);
            data.put("userInfoID", userInfoID);
            data.put("vote", vote);

            new WebserviceTask(mContext, new WebserviceTask.RespostaAssincrona() {
                @Override
                public void fimProcessamento(Context objContexto, JSONObject ObjDadosRetorno) {
                    try
                    {
                        if(ObjDadosRetorno != null) {
                            if(ObjDadosRetorno.has("return") && !ObjDadosRetorno.isNull("return")) {
                                if(ObjDadosRetorno.get("return").equals("true")){
                                    Toast.makeText(mContext, R.string.strVoteSendSuccess, Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(mContext, R.string.strVoteSendFail, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(mContext, "Erro ao inserir voto!", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception ex) {
                        Toast.makeText(mContext, R.string.strError + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void erroAssincrono(Context objContexto, Exception ex) {
                    Toast.makeText(objContexto, R.string.strError + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).execute("http://www.appointweb.com/desafioDoLookApp/controller/vote/insert_vote.php", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
