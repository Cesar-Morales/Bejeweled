package com.example.bejeweled;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class IngresaNombre extends AppCompatActivity {

    private EditText etName;
    private String puntaje;
    private int numero;
    private String razon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresa_nombre);

        etName = (EditText)findViewById(R.id.nombre);
        numero = (int) (Math.random() * 1000) + 1;
    }


    public void volver(View view) {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(IngresaNombre.this);
        a_builder.setMessage("")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("¿Usted quiere terminar sin guardar su puntaje?");
        alert.show();
    }

    public void guardar(View view) {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(IngresaNombre.this);
        a_builder.setMessage("")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        guardarDatos();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("¿Esta seguro que quiere guardar este nombre?");
        alert.show();
    }


    private void guardarDatos(){
        puntaje = getIntent().getStringExtra("score");
        razon = getIntent().getStringExtra("razon");
        String nombre = etName.getText().toString();
        if (!nombre.isEmpty()){
            //Creo la base de datos y la abro para escritura/lectura
            SQLite admin = new SQLite(this, "jugadores", null, 1);
            SQLiteDatabase db =  admin.getWritableDatabase();

            if(razon == "2"){
                String cod = getIntent().getStringExtra("codigo");
                db.delete("jugadores", "codigo" + "=" + cod, null);
            }

            //Creo registro a ingresar

            ContentValues registro = new ContentValues();
            registro.put("codigo", puntaje+nombre+numero);
            registro.put("nombre", nombre);
            registro.put("puntaje", puntaje);
            db.insert("jugadores", null, registro);
            db.close();
            Intent i= new Intent (this,Ranking.class);
            startActivity(i);
            this.finish();
        } else {
            Toast.makeText(IngresaNombre.this, "No ingreso ningun nombre", Toast.LENGTH_SHORT).show();
        }
    }
}
