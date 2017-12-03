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

        ImageView imgLogo = (ImageView)findViewById(R.id.imgLogoLogin);
        Button btnLogar = (Button)findViewById(R.id.btnLogar);
        TextView txtLinkBackLogin = (TextView) findViewById(R.id.txtLinkBackLogin);
        final EditText edtUser = (EditText) findViewById(R.id.edtUser);
        final EditText edtPassword = (EditText) findViewById(R.id.edtPassword);

        txtLinkBackLogin.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/gravitybold.otf"));
        btnLogar.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotoblack.ttf"));
        edtUser.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));
        edtPassword.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (validateFields(edtUser.getText().toString(),edtPassword.getText().toString())) {
                        JSONObject data = new JSONObject();
                        data.put("email_user", edtUser.getText().toString());
                        data.put("password_user", edtPassword.getText().toString());

                        new WebserviceTask(mContext, new WebserviceTask.RespostaAssincrona() {
                            @Override
                            public void fimProcessamento(Context objContexto, JSONObject ObjDadosRetorno) {
                                try
                                {
                                    if(ObjDadosRetorno != null) {
                                        if(ObjDadosRetorno.has("userInfo") && !ObjDadosRetorno.isNull("userInfo")) {
                                            SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL", 0);
                                            SharedPreferences.Editor editor = preferences.edit();
                                            editor.putBoolean("isLogged", true);
                                            editor.putInt("userID", Integer.parseInt(ObjDadosRetorno.getJSONObject("userInfo").get("userInfoID").toString()));
                                            editor.commit();
                                            Intent intent = new Intent(mContext, MainActivity.class);
                                            mContext.startActivity(intent);
                                        } else {
                                            Toast.makeText(mContext, R.string.strUserPasswordWrong, Toast.LENGTH_LONG).show();
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
                        }).execute("http://www.appointweb.com/desafioDoLookApp/controller/users/get_user.php", data);
                    } else {
                        Toast.makeText(mContext, R.string.strUserPasswordNull, Toast.LENGTH_LONG).show();
                    }
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

    private boolean validateFields(String strUser, String strPassword) {
        if (strUser.isEmpty() || strPassword.isEmpty()) {
            return false;
        }
        return true;
    }
}