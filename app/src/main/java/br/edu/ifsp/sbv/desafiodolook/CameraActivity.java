package br.edu.ifsp.sbv.desafiodolook;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.edu.ifsp.sbv.desafiodolook.R;
import br.edu.ifsp.sbv.desafiodolook.FooterNavigationViewHelper;
import br.edu.ifsp.sbv.desafiodolook.connection.WebserviceTask;

import static android.R.attr.data;

/**
 * Created by Guilherme on 10/11/2017.
 */

public class CameraActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CameraActivity";
    private static final int ACTIVITY_NUM = 0;
    private static final int PERMISSAO_REQUEST = 2;
    private static final int CAMERA = 4;
    private File arquivoFoto = null;

    private Context mContext = CameraActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started.");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView txtTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        txtTitle.setText("Camera");
        txtTitle.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/sweetsensations.ttf"));

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setupFooterNavigationView();

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                arquivoFoto = criarArquivo();
            } catch (IOException ex) {

            }
            if (arquivoFoto != null) {
                Uri photoURI = FileProvider.getUriForFile(getBaseContext(), getBaseContext().getApplicationContext().getPackageName() + ".provider", arquivoFoto);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(arquivoFoto))
            );
            ajustaFoto(arquivoFoto);
        }
        Toast.makeText(mContext, "Informe seu username e sua senha para acessar.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
    }

    private File criarArquivo() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File pasta = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imagem = new File(pasta.getPath() + File.separator + "JPG_" + timeStamp + ".jpg");
        return imagem;
    }

    protected void ajustaFoto( File arquivo ){
        getContentResolver().notifyChange( Uri.fromFile( arquivo ) , null);
        ContentResolver cr = getContentResolver();
        JSONObject data = new JSONObject();
        Bitmap bitmap = null;
        int w = 0;
        int h = 0;

        // redimensiona a imagem
        Integer lateral = 256; // tamanho final da dimensao maior da imagem
        try {
            // joga a imagem em uma variavel
            bitmap = android.provider.MediaStore.Images.Media.getBitmap( cr, Uri.fromFile( arquivo ) );

            //BitmapDrawable bmpd = new BitmapDrawable(bitmap);

            // cria um stream pra salvar o arquivo
            FileOutputStream out = new FileOutputStream( arquivo.getPath() );
            ByteArrayOutputStream byteArrayOutputStreamObject  = new ByteArrayOutputStream();

            // uma nova instancia do bitmap rotacionado
            //Bitmap bmp = bmpd.getBitmap();
            Bitmap bmp = bitmap;

                    //define um indice = 1 pois se der erro vai manter a imagem como esta.
            Integer idx = 1;

            // reupera as dimensoes da imagem
            w = bmp.getWidth();
            h = bmp.getHeight();

            // verifica qual a maior dimensao e divide pela lateral final para definir qual o indice de reducao
            if ( w >= h){
                idx = w / lateral;
            } else {
                idx = h / lateral;
            }

            // acplica o indice de reducao nas novas dimensoes
            w = w / idx;
            h = h / idx;

            // cria nova instancia da imagem ja redimensionada
            Bitmap bmpReduzido = Bitmap.createScaledBitmap(bmp, w, h, true);

            // salva a imagem reduzida no disco
            bmpReduzido.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStreamObject);

            byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

            final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

            data.put("encoded_string", ConvertImage);
            data.put("userInfoID", 1);

            new WebserviceTask(mContext, new WebserviceTask.RespostaAssincrona() {
                @Override
                public void fimProcessamento(Context objContexto, JSONObject ObjDadosRetorno) {
                    try
                    {
                        if(ObjDadosRetorno != null) {
                            //Toast.makeText(objContexto, ObjDadosRetorno.get("return").toString(), Toast.LENGTH_LONG).show();

                            if(ObjDadosRetorno.has("status") && !ObjDadosRetorno.isNull("status")) {

                                if(ObjDadosRetorno.get("status").equals("true")){
                                    Toast.makeText(mContext, "SUCESSO!", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(mContext, "ERRO!", Toast.LENGTH_LONG).show();
                                }

                            }else
                                Toast.makeText(mContext, "Erro ao criar usúario!", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(mContext, "Erro: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void erroAssincrono(Context objContexto, Exception ex) {
                    Toast.makeText(objContexto, "Erro: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).execute("http://www.appointweb.com/desafioDoLookApp/controller/album/create_album.php", data);
        } catch (FileNotFoundException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

