package com.example.bejeweled;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Ranking extends AppCompatActivity {

    public TextView textView1;
    public TextView textView2;
    public TextView textView3;
    public TextView textView4;
    public TextView textView5;
    public TextView textView6;
    public TextView textView7;
    public TextView textView8;
    public TextView textView9;
    public TextView textView10;
    public TextView[] textViews;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        textView1 = findViewById(R.id.txt1);
        textView2 = findViewById(R.id.txt2);
        textView3 = findViewById(R.id.txt3);
        textView4 = findViewById(R.id.txt4);
        textView5 = findViewById(R.id.txt5);
        textView6 = findViewById(R.id.txt6);
        textView7 = findViewById(R.id.txt7);
        textView8 = findViewById(R.id.txt8);
        textView9 = findViewById(R.id.txt9);
        textView10 = findViewById(R.id.txt10);

        TextView[] arreglodeText = {textView1, textView2, textView3, textView4, textView5, textView6, textView7, textView8, textView9, textView10};

        //Creo la base de datos y la abro para escritura/lectura
        SQLite admin = new SQLite(this, "jugadores", null, 1);
        SQLiteDatabase db = admin.getWritableDatabase();

        //CONSULTA A BASE DE DATOS
        Cursor registros = db.rawQuery("SELECT * FROM jugadores ORDER BY puntaje DESC", null);
        if (registros.moveToFirst()) {
            //cuantos registros borro
            int i = 0;
            do {
                if(i<10) {
                    String codigo = String.valueOf(i + 1);
                    String nombre = registros.getString(1);
                    String puntaje = registros.getString(2);
                    arreglodeText[i].setText(codigo + " " + nombre + " " + " " + puntaje);
                    i++;
                }
            }while (registros.moveToNext());
            while( i < 10) {
                arreglodeText[i].setText(String.valueOf(i+1) + " VACIO");
                i++;
            }
        } else {
            for (int j = 0; j < 10; j++) {
                arreglodeText[j].setText(String.valueOf(j+1) + " VACIO");
            }
        }
        db.close();
        textViews=arreglodeText;
    }

    public void onClikTerminar(View view) {
        this.finish();
    }


    public void onClickCompartir(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textViews[0].getText()+"\n"+textViews[1].getText()+"\n"+textViews[2].getText()+
                "\n"+textViews[3].getText()+"\n"+textViews[4].getText()+"\n"+textViews[5].getText()+"\n"+textViews[6].getText()
                +"\n"+textViews[7].getText()+"\n"+textViews[8].getText()+"\n"+textViews[9].getText()+"\n");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}