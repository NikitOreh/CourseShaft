package com.example.courseapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Document doc;
    private Thread secThread;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private void getWeb() {
        try {
            doc = Jsoup.connect("https://cbr.ru/currency_base/daily/").get();
            Elements table = doc.select("table.data");

            if (table.size() > 0) {
                Element our_table = table.get(0);
                Elements elements_from_table = our_table.select("tr");

                for (Element row : elements_from_table) {
                    Elements columns = row.select("td");
                    if (columns.size() >= 5) {
                        String currencyName = columns.get(3).text();
                        String exchangeRate = columns.get(4).text();
                        Log.d("MyLog", "Валюта: " + currencyName);
                        Log.d("MyLog", "Курс: " + exchangeRate);
                        Log.d("MyLog", "------------------------");
                    }
                }
            } else {
                Log.e("MyLog", "Table not found on the page");
            }
        } catch (IOException e) {
            Log.e("MyLog", "Error fetching data from the web", e);
            throw new RuntimeException(e);
        }
    }
}