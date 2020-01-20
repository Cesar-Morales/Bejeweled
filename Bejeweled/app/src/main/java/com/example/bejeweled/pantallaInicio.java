package com.example.bejeweled;

import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

public class pantallaInicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_inicio);

    }

    public void onClickSalir(View view) {
        this.finish();
    }

    public void onClickR(View view) {
        //Hacer un intent a ranking pasandole como parametro el string con los datos
        Intent i= new Intent (this,Ranking.class);
        startActivity(i);
    }

    public void onClickNJ(View view) {
        //iniciar el juego que esta en main Activity
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
