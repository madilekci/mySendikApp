package com.example.mysendikapp.etkinlik;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mysendikapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class etkinlikOlustur extends AppCompatActivity {
    String TAG = "ActivityEtkinlikOluştur";
    Button sendButton, btnGallery, btnSelectDate;
    ImageView iv;
    Bitmap bmp;
    public boolean isLoading = false;   // volley.StringRequest 4-5 kez gitmesin, kendini tekrar etmesin diye kullanılıyor.
    private static final int PICK_IMAGE_CAMERA_REQUEST = 99, PICK_IMAGE_REQUEST = 100, PERMISSION_REQUEST_CODE = 101; //Request kodlari

    String image64Post = "", userTokenPost = "", titlePost = "", contentPost = "", datePost = "", eventTypePost = "", isPhonePost = "", isFirmaAdiPost = "";       //Post atilacak parametreler
    String selectedDate = "", selectedTime = "";      //Secilen tarih ve saat degerleri
    Spinner sp;
    ArrayList<String> etkinlikTipleri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etkinlik_olustur);

        userTokenPost = getUserToken();
        iv = (ImageView) (findViewById(R.id.iv_etkinlikOlustur));

        ///
        this.etkinlikTipleri = new ArrayList<String>();
        etkinlikTipleri.add(0, getResources().getString(R.string.spinnerHint));
        getEventTypes();
        sp = (Spinner) findViewById(R.id.sp_etkinlikOlustur);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, etkinlikTipleri);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        ///


        sendButton = (Button) findViewById(R.id.btn_Olustur_etkinlik);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDataSet(v);
            }
        });

        btnGallery = (Button) findViewById(R.id.btn_fotografEkle_etkinlik);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fotografEkleOnClick(v);
            }
        });

        btnSelectDate = findViewById(R.id.btn_date_etkinlikOlustur);
        btnSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(v);
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
    private void getEventTypes() {
        String url = getResources().getString(R.string.etkinlikTipleriUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response to etkinlikTipleri >> " + response);
                        parseEventTypes(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(etkinlikOlustur.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        );

        // request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        jsonStringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));    // Yeniden istek gönderebilmek için uyulması gereken kurallar

        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);
    }

    public void parseEventTypes(String response) {
        try {
//                            JSONObject obj = new JSONObject(response);
            JSONObject obj = new JSONObject(response);
            JSONArray dataArray = obj.getJSONArray("data");
            Log.d(TAG, "Gelen etkinlik tipi sayısı : " + dataArray.length());
            if (dataArray.length() <= 0) {
                Toasty.warning(etkinlikOlustur.this, "Etkinlik tipleri alınamadı.", Toasty.LENGTH_SHORT).show();
                return;
            }

            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataobj = dataArray.getJSONObject(i);
                etkinlikTipleri.add(dataobj.getInt("id"), dataobj.getString("adi"));
            }
            printEventTypes();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void printEventTypes() {
        for (int i = 1; i < etkinlikTipleri.size(); i++) {
            Log.d(TAG, "id -> " + i + "  --  adi -> " + etkinlikTipleri.get(i));
        }
    }

    private void fetchingJSON() {
        sendButton.setText("Etkinlik talebiniz oluşturuluyor. Lütfen bekleyin ...");
        Log.d(TAG, "Fetching JSON ....");

        Log.d(TAG, "Image --> " + image64Post);
        Log.d(TAG, "Title --> " + titlePost);
        Log.d(TAG, "Content--> " + contentPost);
        Log.d(TAG, "userToken  --> " + userTokenPost);
        Log.d(TAG, "datePost  --> " + datePost);
        Log.d(TAG, "eventTypePost  --> " + eventTypePost);


        String url = getResources().getString(R.string.etkinlikOlusturUrl);    // Post atılan adres.
        StringRequest jsonStringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Response to etkinlikOlustur >> " + response);
                        parseJSONData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something when error occurred
                        Toast.makeText(etkinlikOlustur.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("baslik", titlePost);
                params.put("base64", image64Post);
                params.put("aciklama", contentPost);
                params.put("UyeToken", userTokenPost);
                params.put("tarih", datePost);
                params.put("EtkinlikTipi", eventTypePost);
                params.put("isTelefon", isFirmaAdiPost);
                params.put("isFirma", isPhonePost);
                return params;
            }
        };

        // request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        jsonStringRequest.setRetryPolicy(new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));    // Yeniden istek gönderebilmek için uyulması gereken kurallar

        jsonStringRequest.setShouldCache(false);        // "CacheTutulmasıDurumu=false"
        queue.add(jsonStringRequest);

    }

    private void parseJSONData(String response) {
        this.eventUploadedSuccessfully();
    }

    private void eventUploadedSuccessfully() {
        sendButton.setText("Etkinlik talebiniz olusturuldu ☺");
        isLoading = false;
        Log.d(TAG, "--- Etkinlik oluşturuldu ---");
        Toasty.success(this, "Etkinlik talebiniz olusturuldu ☺", Toasty.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                etkinlikOlustur.this.onBackPressed();
            }
        }, 1000);
    }

    ///////////////////////////////////
    ///////////////////////////////////

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(etkinlikOlustur.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(etkinlikOlustur.this, " Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(etkinlikOlustur.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(etkinlikOlustur.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(etkinlikOlustur.this, "Permission Granted Successfully! ", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(etkinlikOlustur.this, "Permission Denied 🙁 ", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    ///////////////////////////////////
    ///////////////////////////////////

    public void pickDate(View v) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(etkinlikOlustur.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Log.d(TAG, "" + day + "." + (month + 1) + "." + year);
                        selectedDate = "" + day + "." + (month + 1) + "." + year;
                        pickTime();
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();

    }

    public void pickTime() {
        // Şimdiki zaman bilgilerini alıyoruz. güncel saat, güncel dakika.
        final Calendar takvim = Calendar.getInstance();
        int saat = takvim.get(Calendar.HOUR_OF_DAY);
        int dakika = takvim.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(etkinlikOlustur.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // hourOfDay ve minute değerleri seçilen saat değerleridir.
                        // Edittextte bu değerleri gösteriyoruz.
                        selectedTime = hourOfDay + ":" + minute;
                        Log.d(TAG, selectedDate + " " + selectedTime);
                        btnSelectDate.setText(selectedDate + " " + selectedTime);
                    }
                }, saat, dakika, true);
        // true değeri 24 saatlik format için.
        // dialog penceresinin button bilgilerini ayarlıyoruz ve ekranda gösteriyoruz.
        tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Seç", tpd);
        tpd.setButton(TimePickerDialog.BUTTON_NEGATIVE, "İptal", tpd);

        tpd.show();


    }

    public void fotografEkleOnClick(View v) {
        final CharSequence[] items = {"Fotoğraf çek", "Galeriden bir fotoğraf seç", "İptal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(etkinlikOlustur.this);
        builder.setTitle("Fotoğraf Ekle");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items[which].equals("Fotoğraf çek")) {
                    openCamera();
                } else if (items[which].equals("Galeriden bir fotoğraf seç")) {
                    openGallery();
                } else if (items[which].equals("İptal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void openCamera() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        sendButton.setText("Lütfen bekleyin");
        sendButton.setClickable(false);
        Intent cmr = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cmr.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cmr, PICK_IMAGE_CAMERA_REQUEST);
        }
    }

    public void openGallery() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission()) {
                requestPermission();
            }
        }

        Intent glr = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(glr, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sendButton.setText("Lütfen bekleyin");
        sendButton.setClickable(false);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri filePath = data.getData();
                try {
                    //Getting the Bitmap from Gallery
                    bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                    bmp = rotateBitmap(bmp, 90);
                    Log.d(TAG, "bmp ->>" + bmp);
                    //Setting the Bitmap to ImageView
                    iv.setImageBitmap(bmp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        image64Post = getStringImage(bmp);
                        etkinlikOlustur.this.activateUploadButton();
                    }
                }, 250);
            } else {
                image64Post = "";
                activateUploadButton();
            }
        }
        if (requestCode == PICK_IMAGE_CAMERA_REQUEST && resultCode == RESULT_OK && data != null && data.getExtras() != null) {
            //Getting the Bitmap from Gallery
            Log.d(TAG, "onActivityResul PickImageFromCamera");
            Log.d(TAG, "data.getExtras --> " + data);

            bmp = (Bitmap) data.getExtras().get("data");
//            bmp = rotateBitmap(bmp, 90);


            //Setting the Bitmap to ImageView
            iv.setImageBitmap(bmp);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    image64Post = getStringImage(bmp);
                    etkinlikOlustur.this.activateUploadButton();
                }
            }, 250);
        } else {
            image64Post = "";
            activateUploadButton();
        }
    }

    public void activateUploadButton() {
        sendButton.setClickable(true);
        sendButton.setText("Etkinlik Oluştur");
        Log.d(TAG, "sendButton activated");
    }

    public void uploadDataSet(View v) {
        EditText txt_1 = (EditText) findViewById(R.id.tv_baslik_etkinlikOlustur);
        EditText txt_2 = (EditText) findViewById(R.id.tv_content_etkinlikOlustur);

        titlePost = txt_1.getText().toString();
        contentPost = txt_2.getText().toString();


        if (!titlePost.equals("") && !contentPost.equals("") && !selectedDate.equals("") && !selectedTime.equals("") && !isLoading && sp.getSelectedItemPosition() != 0) {      //Baslik veya icerik null degilse
            datePost = selectedDate + " " + selectedTime;
            eventTypePost = String.valueOf(sp.getSelectedItemPosition());

            CheckBox cbTel = (CheckBox) findViewById(R.id.cb_telefon_etkinlikOlustur);
            if (cbTel.isChecked()) {
                isPhonePost = "1";
            } else {
                isPhonePost = "0";
            }

            CheckBox cbFirma = (CheckBox) findViewById(R.id.cb_firmaAdi_etkinlikOlustur);
            if (cbFirma.isChecked()) {
                isFirmaAdiPost = "1";
            } else {
                isFirmaAdiPost = "0";
            }
            isLoading = true;
            fetchingJSON();
        } else {        //Kontrollerde sıkıntı varsa
            if (sp.getSelectedItem().toString().equals(getResources().getString(R.string.spinnerHint))) {
                Toasty.warning(this, "Lütfen etkinlik türünü seçin.", Toasty.LENGTH_SHORT).show();
            } else if (titlePost.equals("")) {
                Toast.makeText(this, "Lütfen etkinlik başlığını girin.", Toast.LENGTH_SHORT).show();
            } else if (contentPost.equals("")) {
                Toast.makeText(this, "Lütfen etkinlik açıklamasını girin.", Toast.LENGTH_SHORT).show();
            } else if (selectedTime.equals("") || selectedDate.equals("")) {
                Toast.makeText(this, "Lütfen tarih ve saat seçin.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);

        return temp;
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public String getUserToken() {
        SharedPreferences xd = getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = xd.edit();
        return xd.getString("userToken", "noTokens");
    }
}
