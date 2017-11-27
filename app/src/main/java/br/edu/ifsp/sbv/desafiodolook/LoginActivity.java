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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import br.edu.ifsp.sbv.desafiodolook.connection.WebserviceTask;
import br.edu.ifsp.sbv.desafiodolook.model.User;

/**
 * Created by Guilherme on 15/11/2017.
 */

public class LoginActivity extends Activity{

    private static final String TAG = "LoginActivity";
    private Context mContext = LoginActivity.this;
    private User user;
    public static JSONObject dataResult = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: starting in Login.");

        //Set fonts of texts
        ImageView imgLogo = (ImageView)findViewById(R.id.imgLogoLogin);
        Button btnLogar = (Button)findViewById(R.id.btnLogar);
        TextView txtLinkBackLogin = (TextView) findViewById(R.id.txtLinkBackLogin);
        final EditText edtUser = (EditText) findViewById(R.id.edtUser);
        final EditText edtPassword = (EditText) findViewById(R.id.edtPassword);

        //Set fonts of texts
        txtLinkBackLogin.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/gravitybold.otf"));
        btnLogar.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotoblack.ttf"));
        edtUser.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));
        edtPassword.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));

        //Set Listeners of elements
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject data = new JSONObject();
//
                try
                {
                    data.put("email_user", edtUser.getText().toString());
                    data.put("password_user", edtPassword.getText().toString());

                    new WebserviceTask(mContext, new WebserviceTask.RespostaAssincrona() {
                        @Override
                        public void fimProcessamento(Context objContexto, JSONObject ObjDadosRetorno) {
                            try
                            {
                                if(ObjDadosRetorno != null) {
                                    Toast.makeText(objContexto, ObjDadosRetorno.get("return").toString(), Toast.LENGTH_LONG).show();

                                    if(ObjDadosRetorno.has("userInfo") && !ObjDadosRetorno.isNull("userInfo")) {

                                        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL", 0);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putBoolean("isLogged", true);
                                        editor.putInt("userID", Integer.parseInt(ObjDadosRetorno.getJSONObject("userInfo").get("userInfoID").toString()));
                                        editor.commit();
                                        Intent intent = new Intent(mContext, MainActivity.class);
                                        mContext.startActivity(intent);
                                    }
                                }
                            } catch (Exception objExcecao) {
                                Toast.makeText(mContext, "User não encontrado", Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void erroAssincrono(Context objContexto, Exception objExcecao) {
                            Toast.makeText(objContexto, "Erro: " + objExcecao.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }).execute("http://www.appointweb.com/Imagem/exuser.php", data);
                    //http://www.appointweb.com/Imagem/exuser.php
                    //http://www.appointweb.com.br/desafioDoLookApp/controller/users/get_user.php
                } catch(Exception ex) {
                    Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_LONG).show();
                }
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