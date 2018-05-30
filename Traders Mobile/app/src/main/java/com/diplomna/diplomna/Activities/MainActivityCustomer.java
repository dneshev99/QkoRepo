package com.diplomna.diplomna.Activities;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.diplomna.diplomna.R;

public class MainActivityCustomer extends ListActivity {

    public ListView TempListView;
    public Button backButton;
    private int level = 0;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    //get strings from database or hardcode
    private String[] MainStr = {"Ядки", "Зеленчуци", "Плодове"};
    private String[] Qdki = {"Фъстъци", "Кашу", "Бадеми"};
    private String[] Plod = {"Банан", "Ябълка", "Портокал"};
    private String[] Zelenchuk = {"Домат", "Краставица", "Зеле"};
    private String[] RefStr;

    public void setMainStr(String[] mainStr) {
        MainStr = mainStr;
    }

    public String[] getMainStr() {
        return MainStr;
    }

    public void setRefStr(String[] refStr) {
        RefStr = refStr;
    }

    public String[] getZelenchuk() {
        return Zelenchuk;
    }

    public void setZelenchuk(String[] zelenchuk) {
        Zelenchuk = zelenchuk;
    }

    public String[] getPlod() {
        return Plod;
    }

    public void setPlod(String[] plod) {
        Plod = plod;
    }

    public String[] getQdki() {
        return Qdki;
    }

    public void setQdki(String[] qdki) {
        Qdki = qdki;
    }

    public String[] getRefStr() {
        return RefStr;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_customer);

        SetListViewFromStrArr(getMainStr());
        setLevel(0);
    }

    void init(){
        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getLevel() == 0){
                    //go back to home screen? log out prompt?
                } else if (getLevel() == 1){
                    SetListViewFromStrArr(getMainStr());
                    setLevel(0);
                }
            }
        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int pos, long id) {
        super.onListItemClick(l, v, pos, id);

        //Main listView loading secondary
        if (getLevel() == 0) {
            switch (pos) {
                case 0: {
                    SetListViewFromStrArr(getQdki());
                    setRefStr(getQdki());
                    break;
                }
                case 1: {
                    SetListViewFromStrArr(getZelenchuk());
                    setRefStr(getZelenchuk());
                    break;
                }
                case 2: {
                    SetListViewFromStrArr(getPlod());
                    setRefStr(getPlod());
                    break;
                }
            }
            //indication for being inside secondary listView
            setLevel(1);
        }
        else if (getLevel() == 1){
            //Load special items from database
            //After choosing a special item from the database return level to 0 for later or keep as is?
            //setLevel(0);
        } else {
            SetListViewFromStrArr(getRefStr());
        }
    }

    void SetListViewFromStrArr(String[] str){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getListView().getContext(), R.layout.activity_main_customer, str);
        getListView().setAdapter(adapter);
    }
}
