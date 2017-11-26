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
        Log.d(TAG, "onCreate: starting in Register.");

        //Set fonts of texts
        ImageView imgLogo = (ImageView)findViewById(R.id.imgLogoRegister);
        Button btnRegistrar = (Button)findViewById(R.id.btnRegistrar);
        TextView txtLinkBackRegister = (TextView) findViewById(R.id.txtLinkBackRegister);
        final EditText edtEmail = (EditText) findViewById(R.id.edtEmail);
        final EditText edtUserRegister = (EditText) findViewById(R.id.edtUserRegister);
        final EditText edtPasswordRegister = (EditText) findViewById(R.id.edtPasswordRegister);
        final EditText edtPasswordConfirm = (EditText) findViewById(R.id.edtPasswordConfirm);

        //Set fonts of texts
        txtLinkBackRegister.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/gravitybold.otf"));
        btnRegistrar.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotoblack.ttf"));
        edtEmail.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));
        edtUserRegister.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));
        edtPasswordRegister.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));
        edtPasswordConfirm.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/robotolight.ttf"));

        //Set Listeners of elements
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject data = new JSONObject();

                try
                {
                    data.put("name_user", edtUserRegister.getText().toString());
                    data.put("email_user", edtEmail.getText().toString());
                    data.put("password_user", edtPasswordConfirm.getText().toString());
                    data.put("id_device", Secure.getString(getContentResolver(), Secure.ANDROID_ID));

                    new WebserviceTask(mContext, new WebserviceTask.AsyncResponse() {
                        @Override
                        public void processFinish(Context context, String result) {
                            if(result != null) {
                                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                            }
                        }
                    }).execute("http://www.appointweb.com.br/desafioDoLookApp/controller/users/create_user.php", data);

                    data = new JSONObject();

                    data.put("email_user", edtUserRegister.getText().toString());
                    data.put("password_user", edtPasswordConfirm.getText().toString());

                    new WebserviceTask(mContext, new WebserviceTask.AsyncResponse() {
                        @Override
                        public void processFinish(Context context, String result) {
                            if(result != null) {
                                Toast.makeText(context, result, Toast.LENGTH_LONG).show();
                            }
                        }
                    }).execute("http://www.appointweb.com.br/desafioDoLookApp/controller/users/get_user.php", data);

                    if (!dataResult.get("return").toString().equals("false")) {
                        SharedPreferences preferences = getSharedPreferences("mYpREFERENCES_DDL", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isLogged", true);
                        editor.putInt("userID",Integer.parseInt(dataResult.get("userID").toString()));
                        editor.commit();
                        Intent intent = new Intent(mContext, MainActivity.class);
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "Usuário não encontrado", Toast.LENGTH_LONG).show();
                    }
                    dataResult = null;
                } catch (Exception ex) {
                    Toast.makeText(mContext, "Erro: " + ex.getMessage(), Toast.LENGTH_LONG).show();
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
}