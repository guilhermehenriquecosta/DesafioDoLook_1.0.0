package br.edu.ifsp.sbv.desafiodolook.connection;

/**
 * Created by Adriel on 11/20/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebserviceTask extends AsyncTask<Object, Void, JSONObject> {
    private HttpURLConnection objConexao;
    private URL objURL;
    private OutputStream objFluxoSaida;
    private BufferedReader objLeitorBuffer;
    private JSONObject objDadosEnvio = new JSONObject();
    private ProgressDialog objDialogoProcesso;
    private Context objContexto;
    private Exception objExcecaoAssincrona;
    public RespostaAssincrona objDelegataRespostaAssincrona = null;

    public WebserviceTask(Context objContexto, RespostaAssincrona objDelegataRespostaAssincrona) {
        this.objContexto = objContexto;
        this.objDelegataRespostaAssincrona = objDelegataRespostaAssincrona;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        objDialogoProcesso = ProgressDialog.show(objContexto, "Por favor Aguarde ...", "Obtendo dados ...");
    }

    @Override
    protected JSONObject doInBackground(Object... params) {
        try {
            objURL = new URL((String) params[0]);
            objDadosEnvio = (JSONObject) params[1];
            objConexao = (HttpURLConnection) objURL.openConnection();

            objConexao.setReadTimeout(10000);
            objConexao.setConnectTimeout(15000);
            objConexao.setDoOutput(true);
            objConexao.setDoInput(true);
            objConexao.setRequestMethod("POST");
            objConexao.addRequestProperty("Accept", "application/json");
            objConexao.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            objConexao.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            objConexao.setFixedLengthStreamingMode(objDadosEnvio.toString().getBytes().length);

            objConexao.connect();

            objFluxoSaida = new BufferedOutputStream(objConexao.getOutputStream());

            objFluxoSaida.write(objDadosEnvio.toString().getBytes());
            objFluxoSaida.flush();
            objFluxoSaida.close();

            objLeitorBuffer = new BufferedReader(new InputStreamReader(objConexao.getInputStream()));

            String strLinhaEntrada;

            StringBuffer stbBuffer = new StringBuffer();

            while ((strLinhaEntrada = objLeitorBuffer.readLine()) != null) {
                stbBuffer.append(strLinhaEntrada + "\n");
            }

            objLeitorBuffer.close();

            return new JSONObject(stbBuffer.toString());
        } catch (Exception objException) {
            objExcecaoAssincrona = objException;
        }
        finally {
            objConexao.disconnect();
        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject objResponse){
        super.onPostExecute(objResponse);
        objDialogoProcesso.dismiss();

        if(objExcecaoAssincrona != null)
            objDelegataRespostaAssincrona.erroAssincrono(this.objContexto, objExcecaoAssincrona);
        else
            objDelegataRespostaAssincrona.fimProcessamento(this.objContexto, objResponse);
    }

    public interface RespostaAssincrona {
        void fimProcessamento(Context objContexto, JSONObject objDadosRetorno);
        void erroAssincrono(Context objContexto, Exception objExcecaoAssincrona);
    }
}