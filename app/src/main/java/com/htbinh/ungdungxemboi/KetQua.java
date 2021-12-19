package com.htbinh.ungdungxemboi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class KetQua extends AppCompatActivity {
    TextView rsNameMale, rsConGiapMale, rsDoBMale, rsBanMenhMale;
    TextView rsNameFemale, rsConGiapFemale, rsDoBFemale, rsBanMenhFemale;
    TextView nguHanhBody, cungMenhBody, canChiBody, conSoBody, hoTenBody, tongDiem, ketLuan;
    ImageView img;

    private void Mapping() {
        rsNameMale = findViewById(R.id.rsNameMale);
        rsConGiapMale = findViewById(R.id.rsConGiapMale);
        rsDoBMale = findViewById(R.id.rsDoBMale);
        rsBanMenhMale = findViewById(R.id.rsBanMenhMale);
        rsNameFemale = findViewById(R.id.rsNameFemale);
        rsConGiapFemale = findViewById(R.id.rsConGiapFemale);
        rsDoBFemale = findViewById(R.id.rsDoBFemale);
        rsBanMenhFemale = findViewById(R.id.rsBanMenhFemale);
        nguHanhBody = findViewById(R.id.nguHanhBody);
        cungMenhBody = findViewById(R.id.cungMenhBody);
        canChiBody = findViewById(R.id.canChiBody);
        conSoBody = findViewById(R.id.conSoBody);
        hoTenBody = findViewById(R.id.hoTenBody);
        tongDiem = findViewById(R.id.tongDiem);
        ketLuan = findViewById(R.id.ketLuan);
        img = findViewById(R.id.imgHeart);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ket_qua);
        Mapping();
        Intent i = getIntent();
        String html = i.getStringExtra("html");

        if (!html.equals("")) {
            Document raw = Jsoup.parse(html);
            ArrayList<String> Nam = new ArrayList<>();
            for (Element item :
                    raw.select("div.nam").first().select("li")) {
                Nam.add(item.select("span.red").get(0).text());
            }
            PeopleModel mNam = new PeopleModel(Nam.get(0), Nam.get(1), Nam.get(2).replaceAll("\\(.*?\\)", ""), Nam.get(3));
            ganDuLieuNam(mNam);
            ArrayList<String> Nu = new ArrayList<>();
            for (Element item :
                    raw.select("div.nu").first().select("li")) {
                Nu.add(item.select("span.red").get(0).text());
            }
            PeopleModel mNu = new PeopleModel(Nu.get(0), Nu.get(1), Nu.get(2).replaceAll("\\(.*?\\)", ""), Nu.get(3));
            ganDuLieuNu(mNu);
            ArrayList<String> Content = new ArrayList<>();
            for (Element item :
                    raw.select("div.box_luangiai")) {
                if(item.select("table").first() != null){
                    item.select("table").remove();
                }
                Content.add(item.select("div.content").toString());
            }
            Content.add(raw.select("div.ket_luan").select("h3").text());
            raw.select("div.ket_luan").select("h3").remove();
            Content.add(raw.select("div.ket_luan").text());

            ganDuLieuContent(Content);
        }
    }

    private void ganDuLieuNam(PeopleModel data) {
        rsNameMale.setText(data.getpName());
        rsConGiapMale.setText(data.getpAge());
        rsDoBMale.setText(data.getpDoB());
        rsBanMenhMale.setText(data.getBanMenh());
    }

    private void ganDuLieuNu(PeopleModel data) {
        rsNameFemale.setText(data.getpName());
        rsConGiapFemale.setText(data.getpAge());
        rsDoBFemale.setText(data.getpDoB());
        rsBanMenhFemale.setText(data.getBanMenh());
    }

    private void ganDuLieuContent(ArrayList<String> data) {
        nguHanhBody.setText(Html.fromHtml(data.get(0)));
        cungMenhBody.setText(Html.fromHtml(data.get(1)));
        canChiBody.setText(Html.fromHtml(data.get(2)));
        conSoBody.setText(Html.fromHtml(data.get(3)));
        hoTenBody.setText(Html.fromHtml(data.get(4)));
        tongDiem.setText(Html.fromHtml(data.get(5)));
        ketLuan.setText(Html.fromHtml(data.get(6)));
    }


}