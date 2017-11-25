package br.edu.ifsp.sbv.desafiodolook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Guilherme on 15/11/2017.
 */

public class LoginActivity extends Activity{

    private static final String TAG = "LoginActivity";

    private Context mContext = LoginActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: starting in Login.");

        //Set fonts of texts
        ImageView imgLogo = (ImageView)findViewById(R.id.imgLogoLogin);
        Button btnLogar = (Button)findViewById(R.id.btnLogar);
        TextView txtLinkBackLogin = (TextView) findViewById(R.id.txtLinkBackLogin);
        EditText edtUser = (EditText) findViewById(R.id.edtUser);
        EditText edtPassword = (EditText) findViewById(R.id.edtPassword);

        //Set fonts of texts
        txtLinkBackLogin.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/gravitybold.otf"));
        btnLogar.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotoblack.ttf"));
        edtUser.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));
        edtPassword.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));

        //Set Listeners of elements
        btnLogar.setOnClickListener(new View.OnClickListener() {
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

        txtLinkBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HomeActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, HomeActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
