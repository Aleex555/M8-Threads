package com.example.threads;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    String error = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button boton1 = (Button) findViewById(R.id.button);
        TextView text = (TextView) findViewById(R.id.text1);
        ImageView img = (ImageView) findViewById(R.id.imageView);



        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        String result = getDataFromUrl("https://api.myip.com/");
                        Log.i("Prueba",result);
                        String urldisplay = "curl https://randomfox.ca/floof/";
                        Bitmap bitmap = null;
                        try {
                            InputStream in = new java.net.URL(urldisplay).openStream();
                            bitmap = BitmapFactory.decodeStream(in);
                        } catch (Exception e) {
                            Log.e("Error", (e.getMessage() != null) ? e.getMessage() : "Mensaje de error desconocido");

                            e.printStackTrace();
                        }
                        Handler handler = new Handler(Looper.getMainLooper());
                        Bitmap finalBitmap = bitmap;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                img.setImageBitmap(finalBitmap);
                                text.setText(result);

                            }
                        });

                    }
                });

            }
        });

    }

    private String getDataFromUrl(String demoIdUrl) {
        String result = null;
        int resCode;
        InputStream in;
        try {
            URL url = new URL(demoIdUrl);
            URLConnection urlConn = url.openConnection();

            HttpsURLConnection httpsConn = (HttpsURLConnection) urlConn;
            httpsConn.setAllowUserInteraction(false);
            httpsConn.setInstanceFollowRedirects(true);
            httpsConn.setRequestMethod("GET");
            httpsConn.connect();
            resCode = httpsConn.getResponseCode();

            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpsConn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                in.close();
                result = sb.toString();
            } else {
                error += resCode;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}