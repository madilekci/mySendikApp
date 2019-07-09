package com.example.mysendikapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.etkinlik.etkinlikOlustur;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ActivityTalepSikayet extends AppCompatActivity  {
    String TAG = "ActivityTalepSikayet";

    String konuPost="", aciklamaPost="", userTokenPost="", image64Post="", isNameVisiblePost="1";

    private static final int PICK_IMAGE_CAMERA_REQUEST = 99,PICK_IMAGE_GALLERY_REQUEST =100, PERMISSION_REQUEST_CODE = 101;;
    Button sendButton,btnGallery;
    String mCurrentPhotoPath;
    ImageView iv;
    Spinner sp;
    public boolean isLoading = false;
    Bitmap bmp;
    CheckBox cbBilgiGizle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userTokenPost=getUserToken();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talep_sikayet);

        cbBilgiGizle = (CheckBox) findViewById(R.id.cb_bilgiGizle_talepSikayet);

        ///
        sp = (Spinner) findViewById(R.id.sp_talep);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.talepSikayetItems,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        ///

        sendButton = (Button) findViewById(R.id.btn_talepGonder);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = sp.getSelectedItem().toString();
                Log.d(TAG,"spinner --> "+text);
                uploadDataSet(v);
            }
        });

        iv = (ImageView) (findViewById(R.id.iv_talep) );
        btnGallery = (Button) findViewById(R.id.btn_fotografEkle_talep);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotografEkleOnClick(v);
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

    ///////////////////////////////////
    ///////////////////////////////////

    private void requestPermission() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(ActivityTalepSikayet.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(ActivityTalepSikayet.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
        if(ContextCompat.checkSelfPermission(ActivityTalepSikayet.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_DENIED ) {
            ActivityCompat.requestPermissions(ActivityTalepSikayet.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ActivityTalepSikayet.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
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
                    Toast.makeText(ActivityTalepSikayet.this, "Gerekli izinleri sağladığınız için teşekkürler", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivityTalepSikayet.this, "Gerekli izinleri vermediğiniz için uygulama doğru çalışmayabilir. 🙁 ", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
    ///////////////////////////////////
    ///////////////////////////////////

    public void uploadDataSet(View v) {

        EditText txt_0 = (EditText) findViewById(R.id.tv_konu_claimComplaint);
        EditText txt_1 = (EditText) findViewById(R.id.tv_aciklama_claimComplaint);

        konuPost = txt_0.getText().toString();
        aciklamaPost = txt_1.getText().toString();

        if (!konuPost.equals("") && !aciklamaPost.equals("") && !isLoading ) {
            isLoading=true;
            if (cbBilgiGizle.isChecked()){
                isNameVisiblePost="0";
            }else {
                isNameVisiblePost="1";
            }
            makeRequest();
        } else {
            Toast.makeText(this, "Lütfen Gerekli Alanları Doldurun", Toast.LENGTH_LONG).show();
        }
    }
    public void makeRequest(){
        sendButton.setClickable(false);
        sendButton.setText("Lütfen bekleyin...");
        RequestQueue queue = Volley.newRequestQueue(ActivityTalepSikayet.this);
        Log.d(TAG,"--- makeRequest ---");
        Log.d(TAG,"params : ");
        Log.d(TAG,"--> "+konuPost);
        Log.d(TAG,"--> "+aciklamaPost);
        Log.d(TAG,"--> "+image64Post);
        Log.d(TAG,"--> "+userTokenPost);
        Log.d(TAG,"--- makeRequest ---");
        String url = getResources().getString(R.string.talepSikayetUrl);    // Post atılan adres.
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Response to updateDataSet ->>"+response);
                        demandUploadedSuccessfully(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG,"An error occured  ->>"+error.getMessage());
                        Toasty.error(ActivityTalepSikayet.this, "Error :" + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("konu", konuPost);
                params.put("aciklama", aciklamaPost);
                params.put("UyeToken", userTokenPost);
                params.put("base64", image64Post);
                params.put("isName", isNameVisiblePost);
                return params;
            }
        };
        postRequest.setShouldCache(false);
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0,-1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));    // Yeniden istek gönderebilmek için uyulması gereken kurallar
        queue.add(postRequest);

    }
    public void demandUploadedSuccessfully(String response){
        isLoading=false;
        sendButton.setText("Talebiniz iletildi ☺");
        Toast.makeText(this, "Talebiniz yetkili birimlere iletildi. En kısa sürede size dönüş yapılacağından emin olabilirsiniz.", Toast.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityTalepSikayet.this.onBackPressed();
            }
        }, 1000);
    }

    ///////////////////////////////////
    ///////////////////////////////////

    public void fotografEkleOnClick(View v){
        final CharSequence[] items = {"Fotoğraf çek","Galeriden bir fotoğraf seç","İptal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTalepSikayet.this);
        builder.setTitle("Fotoğraf Ekle");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(items[which].equals("Fotoğraf çek")){
                    openCamera();
                }else if (items[which].equals("Galeriden bir fotoğraf seç")){
                    openGallery();
                }else if(items[which].equals("İptal")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    public void openCamera(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        sendButton.setText("Lütfen bekleyin");
        sendButton.setClickable(false);
        Intent cmr = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cmr.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.mysendikapp", photoFile);
                cmr.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cmr, PICK_IMAGE_CAMERA_REQUEST);
            }
        }
    }
    public void openGallery(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        sendButton.setText("Lütfen bekleyin");
        sendButton.setClickable(false);
        Intent glr = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(glr, PICK_IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bmp = rotateBitmap(bmp, 90);
                Log.d(TAG, "bmp ->>" + bmp);
                //Setting the Bitmap to ImageView
                iv.setImageBitmap(bmp);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        image64Post = getStringImage(bmp);
                        ActivityTalepSikayet.this.activateUploadButton();
                    }
                }, 250);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            ActivityTalepSikayet.this.activateUploadButton();
        }

        if (requestCode == PICK_IMAGE_CAMERA_REQUEST && resultCode == RESULT_OK ){
            try {
                //Getting the Bitmap from Gallery
                Log.d(TAG, "onActivityResul PickImageFromCamera");
                File file = new File(mCurrentPhotoPath);
                bmp = (Bitmap) MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(file));
                if (bmp != null) {
                    bmp = rotateBitmap(bmp,90);
                    iv.setImageBitmap(bmp);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            image64Post = getStringImage(bmp);
                            ActivityTalepSikayet.this.activateUploadButton();
                        }
                    }, 250);
                }
            } catch (IOException e) {
                image64Post = "";
                activateUploadButton();
                e.printStackTrace();
            }
        }else {
            ActivityTalepSikayet.this.activateUploadButton(); }
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);

        return temp;
    }
    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
    public void activateUploadButton(){
        sendButton.setClickable(true);
        sendButton.setText("GÖNDER");
        Log.d(TAG,"sendButton activated");
    }
    public String getUserToken(){
        SharedPreferences xd = getSharedPreferences("sharedPref",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("userToken","noTokens");
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }




}


