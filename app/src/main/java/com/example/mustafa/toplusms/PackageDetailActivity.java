package com.example.mustafa.toplusms;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;

public class PackageDetailActivity extends AppCompatActivity {

    ArrayList<String> valueList,tagList;
    ListView lvPackageInformation, lvPackageStatistic;
    ArrayList<PackageDetail> packageDetailList,packageValueList;
    PackageDetailAdapter packageDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);
        setTitle("Rapor Detayları");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        valueList = (ArrayList<String>) getIntent().getSerializableExtra("PackageList");
        tagList = new ArrayList<String>(Arrays.asList("Paket Numarası","Gönderme Tarihi","Mesaj Başlığı",
                "Durum", "Kontör", "Toplam","Başarılı","Başarısız","Zaman Aşımı", "Bekleyen", "Diğer", "İade"));

        packageDetailList = new ArrayList<PackageDetail>();
        for(int i=1;i<5;i++)
        {
            String value = valueList.get(i);
            if(i==3)
            {
                value = stateCode(value);
            }
            packageDetailList.add(new PackageDetail(tagList.get(i),value));
        }

        packageDetailAdapter = new PackageDetailAdapter(this,packageDetailList);
        lvPackageInformation = (ListView) findViewById(R.id.lvPackageInformation);
        lvPackageInformation.setAdapter(packageDetailAdapter);

        packageValueList = new ArrayList<PackageDetail>();
        for(int i=5;i<12;i++) {
            packageValueList.add(new PackageDetail(tagList.get(i),valueList.get(i)));
        }
        packageDetailAdapter = new PackageDetailAdapter(this,packageValueList);
        lvPackageStatistic = (ListView) findViewById(R.id.lvPackageStatistic);
        lvPackageStatistic.setAdapter(packageDetailAdapter);
    }

    public String stateCode(String code)
    {
        int state = Integer.parseInt(code);

        switch(state)
        {
            case 0: code = "GÖNDERİLMEDİ"; break;
            case 1: code = "İŞLENİYOR"; break;
            case 2: code = "GÖNDERİLDİ"; break;
            case 3: code = "BAŞARILI"; break;
            case 4: code = "BEKLEMEDE"; break;
            case 5: code = "ZAMANAŞIMI"; break;
            case 6: code = "BAŞARISIZ"; break;
            case 7: code = "REDDEDİLDİ"; break;
            case 11: code = "BİLİNMİYOR"; break;
            case 12: code = "HATYOK"; break;
            case 13: code = "HATALI"; break;
            default: code = "BİLİNMİYOR"; break;
        }
        return code;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               finish();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }
}
