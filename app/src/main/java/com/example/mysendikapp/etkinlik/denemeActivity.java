package com.example.mysendikapp.etkinlik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class denemeActivity extends AppCompatActivity {

    String TAG = "ActivityEtkinlikOluştur";
    Button sendButton,btnGallery;
    ImageView iv;
    Bitmap bmp;
    public boolean isLoading = false;
    private static ProgressDialog mProgressDialog;
    private static final int PICK_IMAGE_REQUEST=100;
    private static final int PERMISSION_REQUEST_CODE = 101;

    String image64,userToken,title,content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_olustur);

        sendButton = (Button) findViewById(R.id.btn_Olustur_etkinlik);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataSet(v);
            }
        });



        iv = (ImageView) (findViewById(R.id.iv_etkinlikOlustur) );
        btnGallery = (Button) findViewById(R.id.btn_fotografEkle_etkinlik);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(v);
            }
        });


        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                requestPermission();
            }
        }

    }


    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(denemeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(denemeActivity.this, " Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(denemeActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(denemeActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)   {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(denemeActivity.this, "Permission Granted Successfully! ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(denemeActivity.this, "Permission Denied 🙁 ", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sendButton.setClickable(false);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.d(TAG, "bmp ->>"+bmp);
                //Setting the Bitmap to ImageView
                iv.setImageBitmap(bmp);
//                iv.animate().rotation(90).setDuration(0);
                this.activateUploadButton(image64);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void activateUploadButton(String image){
        image64 = getStringImage(bmp);
        userToken=getUserToken();
        sendButton.setClickable(true);
    }


    public void uploadDataSet(View v) {

        EditText txt_1 = (EditText) findViewById(R.id.tv_baslik_etkinlikOlustur);
        EditText txt_2 = (EditText) findViewById(R.id.tv_content_etkinlikOlustur);

        title   = txt_1.getText().toString();
        content = txt_2.getText().toString();

        if (!(title.equals("") ) && !(content.equals("") && !(image64.equals("") ) ) ) {      //Baslik veya icerik bos degilse
            fetchingJSON();
        } else {
            Toast.makeText(this, "Lütfen Gerekli Alanları Doldurun", Toast.LENGTH_LONG).show();
        }
    }

    private void fetchingJSON() {
        isLoading = true;
        String url = getResources().getString(R.string.etkinlikOlusturUrl);    // Post atılan adres.

        Log.d(TAG,"userToken ->> "+userToken);
        Log.d(TAG,"title ->> "+title);
        Log.d(TAG,"image ->> "+image64);
        Log.d(TAG,"content ->> "+content);

        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Response ->> "+response);
                        parseJSONData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Log.d(TAG,"onErrorResponse ->> "+error.toString());
                        Toast.makeText(denemeActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("baslik", String.valueOf(title));
                params.put("base64", String.valueOf(image64));
                params.put("aciklama", String.valueOf(content));
                params.put("UyeToken", String.valueOf(userToken));

                return params;
            }
        };

        // request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);

    }
    public void parseJSONData(String response) {
        isLoading = false;
//            removeSimpleProgressDialog();
        Log.d(TAG,"Uploaded successfully ... ");
        Toasty.info(this,"Etkinlik oluşturma talebiniz iletildi.",Toasty.LENGTH_SHORT);
    }

    public void openGallery(View v){
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        Intent glr = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(glr,PICK_IMAGE_REQUEST);
    }
    public String getUserToken(){
        SharedPreferences xd = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("userToken","noTokens");
    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}
