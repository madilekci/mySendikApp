package com.example.mysendikapp.anketler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.mysendikapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class anketDoldur extends AppCompatActivity {

    anketModel aModel1;
    String TAG ="anketDoldur";
    TextView tv_soru,tv_soruNo;
    Button btnNext;
    ArrayList<String> verilenCevaplar;
    private static ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anket_doldur);

        aModel1 = new anketModel();
        verilenCevaplar = new ArrayList<>();
        tv_soru= (TextView) findViewById(R.id.tv_anketSorusu_anketDoldur);
        tv_soruNo=(TextView) findViewById(R.id.tv_soruNo_anketDoldur);

        initSampleAnketModel();
        setSoruTV(1);


        btnNext = (Button) findViewById(R.id.btn_next_anketDoldur);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickNextButton();
            }
        });

    }


    public void createSampleSorular(){
        ArrayList<String > sampleSorular = new ArrayList<>();
        sampleSorular.add("Sendikamıza üye olmaktan memnun musunuz ?");
        sampleSorular.add("Daha önce başka bir sendikanın üyesi olmuş muydunuz ?");
        sampleSorular.add("Oy sendika Sendika ?");
        aModel1.setSorular(sampleSorular);
    }
    public void createSampleCevaplar(){
        ArrayList< ArrayList<String> > sampleCevaplar = new ArrayList<>();
        ArrayList<String> cevaplar1 = new ArrayList<>();
        ArrayList<String> cevaplar2 = new ArrayList<>();
        ArrayList<String> cevaplar3 = new ArrayList<>();

        cevaplar1.add("cevap 1");
        cevaplar1.add("cevap 2");

        cevaplar2.add("cevap bir");
        cevaplar2.add("cevap iki");

        cevaplar3.add("1");
        cevaplar3.add("2");
    }
    public void initSampleAnketModel(){
        aModel1.setId("2");
        aModel1.setSummary("Anketin özeti dediğin , anketin amacını misyon ve vizyonunu anlatmalıdır.");
        aModel1.setTitle("ANKET BAŞLIĞI");
        createSampleSorular();
        createSampleCevaplar();
    }

    public void onClickNextButton(){
        Log.d(TAG,"nextButtonClicked");
        int soruNo = Integer.parseInt( ((TextView)findViewById(R.id.tv_soruNo_anketDoldur)).getText().toString());

        soruNo++;

        RadioGroup rg = findViewById(R.id.rg_evetHayir_anketDoldur);
        int selectedRadioButtonID = rg.getCheckedRadioButtonId();

        if (selectedRadioButtonID == -1) {      // If nothing is selected from Radio Group, then it return -1
            Toasty.warning(this,"Lütfen cevaplardan birini seçin",Toasty.LENGTH_SHORT).show();
            return;
        }else {
            RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
            String answer = selectedRadioButton.getText().toString();
            this.verilenCevaplar.add(answer);
            setSoruTV(soruNo);
            rg.clearCheck();
        }


    }

    public void setSoruTV(int soruNo){
        soruNo--;
        if(this.aModel1.getSorular().size()==soruNo){
            this.anketBitti();
            return;
        }else {
        tv_soruNo.setText(""+(soruNo+1) );
        Log.d(TAG,"soruNo --> "+soruNo);
        tv_soru.setText(this.aModel1.getSorular().get(soruNo));
        }
    }
    public void anketBitti(){
        printAnswersToConsole();
        showAlertDialog(this,"Anket sona erdi","Cevaplarınız daha sonra incelememiz için kaydedilmiştir. Anketimize katıldığınız için çok teşekkür ederiz.");
    }


    public void showAlertDialog(Context context, String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);
        // Setting Dialog Message
        alertDialog.setMessage(message);
        // Setting OK Button
        alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                anketDoldur.this.onBackPressed();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
    public void printAnswersToConsole(){
        Log.d(TAG,"________________");
        for (int i =0;i<this.verilenCevaplar.size();i++){
            Log.d(TAG,"Cevap "+(i+1)+" ---> "+this.verilenCevaplar.get(i));
        }
        Log.d(TAG,"________________");
    }
}
