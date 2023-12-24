package com.example.courseapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Document doc;
    private Thread secThread;
    private Runnable runnable;
    private ListView listView;
    private CustomArrayAdapter adapter;

    private List<ListItemClass> arrayList;
    private List<ListItemClass> originalList; // Добавим переменную для хранения исходного списка

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        adapter = new CustomArrayAdapter(this, R.layout.list_item_1, arrayList, getLayoutInflater());
        listView.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText.toLowerCase().trim());
                if (TextUtils.isEmpty(newText)) {
                    // Если текст пустой, восстанавливаем исходный список
                    arrayList.clear();
                    arrayList.addAll(originalList);
                    adapter.notifyDataSetChanged(); // Обновляем список
                }
                return true;
            }
        });

        secThread = new Thread(new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        });
        secThread.start();
    }

    private void getWeb() {
        try {
            doc = Jsoup.connect("https://cbr.ru/currency_base/daily/").get();
            Elements tables = doc.getElementsByTag("tbody");
            Element our_table = tables.get(0);
            Elements elements_from_table = our_table.children();
            Element dollar = elements_from_table.get(0);
            Elements dollar_elements = dollar.children();
            for (int i = 0; i < our_table.childrenSize(); i++) {
                ListItemClass items = new ListItemClass();
                items.setData_1(our_table.children().get(i).child(1).text());
                items.setData_2(our_table.children().get(i).child(2).text());
                items.setData_3(our_table.children().get(i).child(3).text());
                items.setData_4(our_table.children().get(i).child(4).text());
                arrayList.add(items);
            }
            // Сохраняем исходный список данных
            originalList = new ArrayList<>(arrayList);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (IOException | IndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
