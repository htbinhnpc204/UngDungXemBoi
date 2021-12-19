package com.htbinh.ungdungxemboi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView heartImage, MaleDoBpicker, FemaleDoBpicker;
    Dialog dialog;

    TextView MaleDoB, FemaleDoB;
    EditText MaleName, FemaleName;

    Button Submit, Cancel;

    int selectedYear = 2000;
    int selectedMonth = 0;
    int selectedDayOfMonth = 1;

    Connection.Response response;
    Document raw;
    public Map<String, String> data;

    DatePickerDialog.OnDateSetListener maleDateSetListener;
    DatePickerDialog.OnDateSetListener femaleDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createDialog();

        heartImage = findViewById(R.id.heartImage);

        maleDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                MaleDoB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            }
        };

        femaleDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                FemaleDoB.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            }
        };


        heartImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    private void createDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_form);

        MaleName = dialog.findViewById(R.id.edtNameMale);
        FemaleName = dialog.findViewById(R.id.edtNameFMale);
        MaleDoB = dialog.findViewById(R.id.DoBMale);
        FemaleDoB = dialog.findViewById(R.id.DoBFMale);
        MaleDoBpicker = dialog.findViewById(R.id.btnSelectDoBMale);
        FemaleDoBpicker = dialog.findViewById(R.id.btnSelectDoBFMale);
        Submit = dialog.findViewById(R.id.btnSubmit);
        Cancel = dialog.findViewById(R.id.btnCancel);

        MaleDoBpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = createDatePickerDialog(maleDateSetListener);
                datePickerDialog.show();
            }
        });

        FemaleDoBpicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = createDatePickerDialog(femaleDateSetListener);
                datePickerDialog.show();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(MaleName.getText().toString().equals("") || FemaleName.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ tên của 2 bên thông gia ☺", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Trong đây mới xử lý nhiều nà
                String[] ngayNam = MaleDoB.getText().toString().split("-");
                String[] ngayNu= FemaleDoB.getText().toString().split("-");
                data = new HashMap<>();
                data.put("action", "vnk_boitinhyeu");
                data.put("namenam", MaleName.getText().toString());
                data.put("ngaynam", ngayNam[0]);
                data.put("thangnam", ngayNam[1]);
                data.put("namnam", ngayNam[2]);
                data.put("namenu", FemaleName.getText().toString());
                data.put("ngaynu", ngayNu[0]);
                data.put("thangnu", ngayNu[1]);
                data.put("namnu", ngayNu[2]);
//                Thread request = new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//                request.start();
//
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Intent i = new Intent(MainActivity.this, KetQua.class);
//                        i.putExtra("html", response.body());
//                        startActivity(i);
//                    }
//                }, 2000);
                new getData().execute();
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dissmissDialog();
            }
        });

        Window window = dialog.getWindow();

        if (window == null) {
            return;
        }

        window.setWindowAnimations(R.style.AnimationForCustomDialog);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }

    private DatePickerDialog createDatePickerDialog(DatePickerDialog.OnDateSetListener listener){
        return new DatePickerDialog(this, listener, selectedYear, selectedMonth, selectedDayOfMonth);
    }

    private void openDialog() {
        dialog.show();
    }

    private void dissmissDialog() {
        dialog.dismiss();
    }

    private class getData extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Đang tính toán vui lòng chờ ♥", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try  {
                response = Jsoup.connect(getResources().getString(R.string.src_link))
                        .data(data).cookie("cookie","cookie")
                        .method(Connection.Method.POST)
                        .execute();
            } catch (Exception e) {
                Log.i("Error", e.toString());
            }
            return response.body();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent i = new Intent(MainActivity.this, KetQua.class);
            i.putExtra("html", s);
            startActivity(i);
        }
    }
}

