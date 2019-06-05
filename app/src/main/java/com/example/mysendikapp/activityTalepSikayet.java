package com.example.mysendikapp;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class activityTalepSikayet extends AppCompatActivity {
    String TAG = "activityTalepSikayet";
    Button sendButton,btnGallery;
    ImageView iv;
    private static final int PICK_IMAGE_REQUEST=100;
    Uri imageUri;

    private static final int PERMISSION_REQUEST_CODE = 101;
    Bitmap bmp;
    String myurl = "http://192.168.43.87/myimage/index.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_complaint);

        sendButton = (Button) findViewById(R.id.btn_talepGonder);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataSet(v);
            }
        });

        iv = (ImageView) (findViewById(R.id.iv_deneme_talep) );
        //  bmp = BitmapFactory.decodeResource(getResources(), R.id.iv_deneme_talep);

        btnGallery = (Button) findViewById(R.id.btn_fotografEkle_talep);
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
        if (ActivityCompat.shouldShowRequestPermissionRationale(activityTalepSikayet.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activityTalepSikayet.this, " Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activityTalepSikayet.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(activityTalepSikayet.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                    Toast.makeText(activityTalepSikayet.this, "Permission Granted Successfully! ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activityTalepSikayet.this, "Permission Denied 🙁 ", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.d(TAG, "bmp ->>"+bmp);
                //Setting the Bitmap to ImageView
                iv.setImageBitmap(bmp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void openGallery(View v){
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {

            } else {
                requestPermission();
            }
        }

        Intent glr = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(glr,PICK_IMAGE_REQUEST);
    }
    public String getUserToken(){
        SharedPreferences xd = getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("userToken","noTokens");
    }

    public void makeRequest(final String userToken, final String images,  final String talep){
        RequestQueue queue = Volley.newRequestQueue(activityTalepSikayet.this);

        String url = getResources().getString(R.string.talepSikayetUrl);    // Post atılan adres.
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Response to updateDataSet ->>"+response);
                        Toasty.success(activityTalepSikayet.this, response, Toast.LENGTH_SHORT, true).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toasty.error(activityTalepSikayet.this, "Error :" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("talep", talep);
                params.put("user_token", userToken);
                params.put("image", images);
                return params;
            }
        };
        postRequest.setShouldCache(false);
        queue.add(postRequest);
        Toast.makeText(this, "Talebiniz yetkili birimlere iletildi.", Toast.LENGTH_LONG).show();


    }
    public void uploadDataSet(View v) {

        EditText txt_1 = (EditText) findViewById(R.id.tv_claimComplaint);
        final String talep = txt_1.getText().toString();
        final String userToken=getUserToken();
        String images = getStringImage(bmp);
        if (!(talep.equals(""))) {
            Log.d(TAG,"imageStringBase64 ->> "+images);
            makeRequest(userToken,images,talep);
        } else {
            Toast.makeText(this, "Lütfen Gerekli Alanları Doldurun", Toast.LENGTH_LONG).show();
        }
    }

    public String getStringImage(Bitmap bitmap){
        Log.i("MyHitesh",""+bitmap);
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }




}


