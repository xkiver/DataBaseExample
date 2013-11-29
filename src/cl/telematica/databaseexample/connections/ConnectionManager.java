package cl.telematica.databaseexample.connections;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import cl.telematica.databaseexample.interfaces.DownloadListener;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectionManager extends AsyncTask<String, Void, String> {
	
	private static final String DEBUG_TAG = "ConnectionManager";
	private DownloadListener listener;
	
	private int readTimeOut;
	private int connectTimeOut;
	private String requestMethod;
	
	public ConnectionManager(DownloadListener listener, int readTimeOut, int connectTimeOut, String requestMethod){
		this.listener = listener;
		this.readTimeOut = readTimeOut;
		this.connectTimeOut = connectTimeOut;
		this.requestMethod = requestMethod;
	}
    
	@Override
	protected void onPreExecute(){
		if(listener != null)
			listener.onRequestStart();
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
            return downloadData(urls[0]);
        } catch (IOException e) {
            return null;
        }
	}
	
    @Override
    protected void onPostExecute(String result) {
    	if(listener != null){
    		if(result != null)
    			listener.onRequestComplete(result);
    		else
    			listener.onError("No response", 500);
    	}
    }
    
	// Dada una URL, se establece una HttpUrlConnection y se descarga
	// el contenido de la url como InputStream, lo cual posteriormente
	// se retorna como string.
	private String downloadData(String myurl) throws IOException {
		InputStream is = null;
	         
	    try {
	    	URL url = new URL(myurl);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setReadTimeout(readTimeOut /* milisegundos */);
	        conn.setConnectTimeout(connectTimeOut /* milisegundos */);
	        conn.setRequestMethod(requestMethod);
	        conn.setDoInput(true);
	        // Comienza la query
	        conn.connect();
	        int response = conn.getResponseCode();
	        Log.d(DEBUG_TAG, "La respuesta es: " + response);
	        is = conn.getInputStream();
	
	        // Se convierte el InputStream en string
	        String contentAsString = readIt(is);
	        return contentAsString;
	         
	     // Nos aseguramos de que el InputStream queda cerrado después
	     // de terminar de usarlo.
	     } finally {
	        if (is != null) {
	            is.close();
	        } 
	     }
	}
	
	// Se lee un InputStream y se convierte en String.
	public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
	    Reader reader = null;
		StringBuilder inputStringBuilder = new StringBuilder();
	    reader = new InputStreamReader(stream, "UTF-8");     
	    BufferedReader bufferedReader = new BufferedReader(reader);
	    String line = bufferedReader.readLine();
	    while(line != null){
            inputStringBuilder.append(line);
            inputStringBuilder.append('\n');
            line = bufferedReader.readLine();
        }
	    return inputStringBuilder.toString();
	}

}