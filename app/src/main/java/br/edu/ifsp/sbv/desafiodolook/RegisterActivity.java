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
import android.provider.Settings.Secure;

import br.edu.ifsp.sbv.desafiodolook.connection.WebserviceTask;
import br.edu.ifsp.sbv.desafiodolook.model.User;

/**
 * Created by Guilherme on 21/11/2017.
 */

public class RegisterActivity extends Activity {

    private static final String TAG = "LoginActivity";
    private Context mContext = RegisterActivity.this;
    private User user;
    public static JSONObject dataResult = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView imgLogo = (ImageView)findViewById(R.id.imgLogoRegister);
        Button btnRegistrar = (Button)findViewById(R.id.btnRegistrar);
        TextView txtLinkBackRegister = (TextView) findViewById(R.id.txtLinkBackRegister);
        final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        final EditText edtUserRegister = (EditText) findViewById(R.id.edtUserRegister);
        final EditText edtPasswordRegister = (EditText) findViewById(R.id.edtPasswordRegister);
        final EditText edtPasswordConfirm = (EditText) findViewById(R.id.edtPasswordConfirm);

        txtLinkBackRegister.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/gravitybold.otf"));
        btnRegistrar.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotoblack.ttf"));
        edtEmail.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));
        edtUserRegister.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));
        edtPasswordRegister.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));
        edtPasswordConfirm.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if (validateFields(edtEmail.getText().toString(),edtUserRegister.getText().toString(),edtPasswordRegister.getText().toString(),edtPasswordConfirm.getText().toString())) {
                        if (edtPasswordRegister.getText().toString().equals(edtPasswordConfirm.getText().toString())) {
                            JSONObject data = new JSONObject();
                            data.put("name_user", edtUserRegister.getText().toString());
                            data.put("email_user", edtEmail.getText().toString());
                            data.put("password_user", edtPasswordConfirm.getText().toString());
                            data.put("id_device", Secure.getString(getContentResolver(), Secure.ANDROID_ID));

                            new WebserviceTask(mContext, new WebserviceTask.RespostaAssincrona() {
                                @Override
                                public void fimProcessamento(Context objContexto, JSONObject ObjDadosRetorno) {
                                    try
                                    {
                                        if(ObjDadosRetorno != null) {
                                            if(ObjDadosRetorno.has("userInfo") && !ObjDadosRetorno.isNull("userInfo")) {
                                                if(ObjDadosRetorno.get("return").equals("exist")){
                                                    if (ObjDadosRetorno.getJSONObject("userInfo").has("email")) {
                                                        Toast.makeText(mContext, R.string.strErrorEmailExists, Toast.LENGTH_LONG).show();
                                                    } else {
                                                        Toast.makeText(mContext, R.string.strErrorUsernameExists, Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL", 0);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putBoolean("isLogged", true);
                                                    editor.putInt("userID", Integer.parseInt(ObjDadosRetorno.getJSONObject("userInfo").get("userInfoID").toString()));
                                                    editor.commit();
                                                    Intent intent = new Intent(mContext, MainActivity.class);
                                                    mContext.startActivity(intent);
                                                }
                                            } else {
                                                Toast.makeText(mContext, R.string.strErrorRegisterUser, Toast.LENGTH_LONG).show();
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
                            }).execute("http://www.appointweb.com/desafioDoLookApp/controller/users/create_user.php", data);
                        } else {
                            Toast.makeText(mContext, R.string.strPasswordsDifferents, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(mContext, R.string.strRegisterFieldsNull, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(mContext, R.string.strError + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        txtLinkBackRegister.setOnClickListener(new View.OnClickListener() {
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

    private boolean validateFields(String strEmail, String strUser, String strPassword, String strConfirmPassword) {
        if (strEmail.isEmpty() || strUser.isEmpty() || strPassword.isEmpty() || strConfirmPassword.isEmpty()) {
            return false;
        }
        return true;
    }
}