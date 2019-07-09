package com.example.mysendikapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import es.dmoral.toasty.Toasty;

public class anlasmaliYerlerActivity extends AppCompatActivity {
    Spinner spSehirler, spKategori;
    Button firmaListele;
    ScrollView svFirmaListesi;
    TextView tv_title1, tv_title2, tv_title3;
    TextView tv_content1, tv_content2, tv_content3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anlasmali_yerler);


        spSehirler = (Spinner) findViewById(R.id.sp_sehirler);
        spKategori = (Spinner) findViewById(R.id.sp_kategori);
        firmaListele = (Button) findViewById(R.id.btn_FirmaListele);

        tv_title1 = (TextView) findViewById(R.id.tv_baslik_1);
        tv_content1 = (TextView) findViewById(R.id.tv_content_1);

        tv_title2 = (TextView) findViewById(R.id.tv_baslik_2);
        tv_content2 = (TextView) findViewById(R.id.tv_content_2);

        tv_title3 = (TextView) findViewById(R.id.tv_baslik_3);
        tv_content3 = (TextView) findViewById(R.id.tv_content_3);

        svFirmaListesi = (ScrollView) findViewById(R.id.sv_firmaListesi);
        svFirmaListesi.setVisibility(View.INVISIBLE);

        firmaListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firmaListeleOnClick();
            }
        });


    }


    public void firmaListeleOnClick() {
        String tt1="", tt2="", tt3="", ct1="", ct2="", ct3="";

        Log.d("anlasmaliYerlerActivity", "kategori sp getSelectedItem --> " + spKategori.getSelectedItem());
        Log.d("anlasmaliYerlerActivity", "şehir sp getSelectedItem --> " + spSehirler.getSelectedItem());
        if (spKategori.getSelectedItem() == null || spSehirler.getSelectedItem() == null) {
            Toasty.warning(this, "Lütfen şehir ve sektör seçiniz", Toasty.LENGTH_SHORT).show();
        }else if(spSehirler.getSelectedItem().equals("Şehir Seçiniz")){
            Toasty.warning(this, "Lütfen şehir seçiniz", Toasty.LENGTH_SHORT).show();
        } else if (spKategori.getSelectedItem().equals("Sektör Seçiniz")) {
            Toasty.warning(this, "Lütfen sektör seçiniz", Toasty.LENGTH_SHORT).show();
        }else {
            switch (spKategori.getSelectedItem().toString()) {
                case "Sağlık":
                    tt1 = "Medikal Hastaneleri";
                    ct1 = "Sendikamızın bütün üyelerine tüm sağlık hizmetlerinde %25 indirim !";

                    tt2 = "Özel Sağlık Hastaneleri";
                    ct2 = "Üyelerimize özel muayene ücretinde %75 indirim";

                    tt3 = "XYZ Optik";
                    ct3 = "Tüm dereceli gözlüklerde %10 indirim";
                    tv_title3.setVisibility(View.VISIBLE);
                    tv_content3.setVisibility(View.VISIBLE);
                    break;
                case "Eğitim":
                    tt1 = "DFA Dershaneleri";
                    ct1 = "Sendikamız üyelerinin 1.derece yakınlarına bütün kaynak kitap ve test sınavı ücretlerinde %50 indirim";

                    tt2 = "Doğa Koleji";
                    ct2 = "Sendikamız üyelerinin bütün akrabalarına ekstra %10 erken kayıt indirimi";

                    tt3 = "Ailem Özel Eğitim Kurumları";
                    ct3 = "Sendikamız üyelerinin 1.derece akrabalarına servis ücretinde %50 indirim";
                    tv_title3.setVisibility(View.VISIBLE);
                    tv_content3.setVisibility(View.VISIBLE);
                    break;
                case "Hizmet":
                    tt1 = "Halk bankası";
                    ct1 = "Sendikamızın üyelerinden  mobil uygulamadan yapılan EFT/Havale işlemleri için ekstra ücret alınmayacak.";

                    tt2="Domino's Pizza";
                    ct2="Gel-al siparişlerde 1L içecek hediye";

                    tv_title3.setVisibility(View.INVISIBLE);
                    tv_content3.setVisibility(View.INVISIBLE);
                    break;
                case "Alışveriş":
                    tt1 = "LC Waikiki";
                    ct1 = "Sezon sonu ürünlerinde 3 alana 1 bedava !";

                    tt2 = "trendyol.com";
                    ct2 = "Üyelerimize özel 50₺ üzeri alışverişlerde kargo ücretsiz.";

                    tt3 = "Decathlon";
                    ct3 = "100₺ ve üzeri alışverişlerinizde kamp reyonunda kullanabileceğiniz 50₺ lik hediye çeki";
                    tv_title3.setVisibility(View.VISIBLE);
                    tv_content3.setVisibility(View.VISIBLE);
                    break;
            }
            tv_title1.setText(tt1);
            tv_content1.setText(ct1);

            tv_title2.setText(tt2);
            tv_content2.setText(ct2);

            tv_title3.setText(tt3);
            tv_content3.setText(ct3);

            svFirmaListesi.setVisibility(View.VISIBLE);
        }
    }
}
