package br.edu.ifsp.sbv.desafiodolook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Guilherme on 21/11/2017.
 */

public class HomeActivity extends Activity {

    private static final String TAG = "HomeActivity";

    private Context mContext = HomeActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView imgLogo = (ImageView)findViewById(R.id.imgLogo);
        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        TextView txtLinkTest = (TextView) findViewById(R.id.txtLinkTest);

        txtLinkTest.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/gravitybold.otf"));
        btnLogin.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotoblack.ttf"));
        btnRegister.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotoblack.ttf"));

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                mContext.startActivity(intent);
            }
        });

        txtLinkTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLogged", true);
                editor.commit();
                Intent intent = new Intent(mContext, MainActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
