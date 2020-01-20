package com.example.bejeweled;





import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;


import java.util.*;







public class MainActivity extends AppCompatActivity implements Dialogo.OnDialogListener{







    private Button bRef;
    private Handler handler = new Handler();
    private GridLayout gridLayout;
    private int[] ArrayImage = {R.drawable.blue, R.drawable.green, R.drawable.orange, R.drawable.purple, R.drawable.red, R.drawable.yellow, R.drawable.backgroun};
    private int[] ArrayImageDark = {R.drawable.bluedark, R.drawable.greendark, R.drawable.orangedark, R.drawable.purpledark, R.drawable.reddark, R.drawable.yellowdark};
    private Integer[] array = new Integer[64]; //GLOBAL
    private int pressA = 0;
    private int pressB = 0;
    final int life = 999999999;
    TextView txtScore;
    final int[] limitRight = {7, 15, 23, 31, 39, 47, 55, 63};
    final int[] limitLeft = {0, 8, 16, 24, 32, 40, 48, 56};
    final int[] limitDown = {56, 57, 58, 59, 60, 61, 62, 63};
    List lis1; //Lista de elementos a borrar
    int score;
    boolean siguiente;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create gridLayout
        txtScore = (TextView) findViewById(R.id.score);
        this.createTable();

        //delete later
        //txtbtn1 = (TextView) findViewById(R.id.txtbtn1);
        //txtbtn2 = (TextView) findViewById(R.id.txtbtn2);
        //Start the game
        bRef = findViewById(R.id.btnRefresh); //habilita/deshabilita el boton de reorden
        setSingleEvent();

    }







    private void deleteCombination(){
        //create grids without combinations
        while (movementTest()) {
            for (int j = 0; j < 64; j++) {
                deleteIfTrue(j);
            }
            fillGrid();
        }
    }








    private void createTable() {
        gridLayout = findViewById(R.id.gridlayout);
        for (int i = 0; i < 64; i++) {
            randomGrid(gridLayout, i);
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (siguiente){
                    //hago la cascada
                    waterfall();
                    //hago que caigan
                    recharge();
                    //relleno
                    fillGrid();
                    siguiente= false;
                    handler.postDelayed(this,1000);
                }
                else {
                    if (movementTest()) {
                        for (int j = 0; j < 64; j++) {
                            deleteIfTrue(j);
                        }
                        siguiente= true;
                        handler.postDelayed(this, 1000);

                    }
                    else{
                        setSingleEvent();
                    }
                }

            }
        }, 1000);
                                                                                                    //testLayout(gridLayout); //para probar refresh
    }







    private boolean deleteIfTrue(int index) {
        lis1 = horizontalTest(index);
        lis1.addAll(verticalTest(index));
        if ((lis1.size() > 2)) {
            removeGrid(lis1, gridLayout);
            //Toast.makeText(MainActivity.this, "Elementos borrados:" + cantList, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }







    private void setSingleEvent() {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final ImageView image = (ImageView) gridLayout.getChildAt(i);
            final int finalI = i;
            image.setBackgroundColor(Color.parseColor("#000000"));
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pressA < life) {
                        pressA += 1;
                        //txtbtn1.setText(String.valueOf(pressA));
                        image.setBackgroundColor(Color.parseColor("#404040"));
                        setSingleEvent2(finalI);
                    }
                }
            });
        }
    }










    private void setSingleEvent2(final int j) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final ImageView image = (ImageView) gridLayout.getChildAt(i);
            final int finalI = i;
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pressB < life) {                                                          // delete later
                        pressB += 1;                                                              // delete later
                        // txtbtn2.setText(String.valueOf(pressB));                               // delete later
                        if (validateMovement(j, finalI)) {
                            move(j, finalI);
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final boolean d1 = deleteIfTrue(j);                                //return true if it is delete and
                                    final boolean d2 = deleteIfTrue(finalI);                           //return false if it isnt delete
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if ((d1) || (d2)) {
                                                //hago la cascada
                                                waterfall();
                                                //hago que caigan
                                                recharge();
                                                //relleno
                                                fillGrid();
                                                siguiente = false;
                                                //si una vez que rellene hay juego
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (siguiente){
                                                            //hago la cascada
                                                            waterfall();
                                                            //hago que caigan
                                                            recharge();
                                                            //relleno
                                                            fillGrid();
                                                            siguiente= false;
                                                            handler.postDelayed(this,600);
                                                        }
                                                        else {
                                                            if (movementTest()) {
                                                                for (int j = 0; j < 64; j++) {
                                                                    deleteIfTrue(j);
                                                                }
                                                                siguiente= true;
                                                                handler.postDelayed(this, 600);

                                                            }
                                                            else{
                                                                setSingleEvent();
                                                            }
                                                        }

                                                    }
                                                }, 600);


                                            }
                                            if ((!d1) && (!d2)){                                                  //if nothing is delete
                                                Toast.makeText(MainActivity.this, "There aren't combinations. Try another grid", Toast.LENGTH_LONG).show();
                                                move(j, finalI);
                                            }
                                            setSingleEvent();
                                        }

                                    },600);

                                }
                            },600);
                        } else {
                            Toast.makeText(MainActivity.this, "Only adjacent grid. Try another", Toast.LENGTH_SHORT).show();
                            setSingleEvent();
                        }
                    }
                }
            });
        }
    }







    private void move(int fClick, int sClick) {
        /*if ((array[fClick] == -1) || (array[sClick] == -1)){
            Toast.makeText(MainActivity.this, "Momentarily invalid movement" , Toast.LENGTH_LONG).show();
            return;
        }*/
        int aux = array[fClick];
        array[fClick] = array[sClick];
        array[sClick] = aux;
        for (int i = 0; i < 64; i++) {
            if (fClick == i) {
                positionChange(fClick, i, gridLayout);
            } else if (sClick == i) {
                positionChange(sClick, i, gridLayout);
            }
        }
    }







    private void positionChange(int click, int index, GridLayout gL) {
        gL.removeViewAt(index);
        Resources resources = getResources();
        ImageView image = new ImageView(this);
        image.setImageDrawable(resources.getDrawable(ArrayImage[array[click]]));
        gL.addView(image, index);
    }






    private List<Integer> horizontalTest(int cord1) {
        if (cord1 != -1) {
            List<Integer> listPos = new ArrayList();
            int firstClick = array[cord1];
            listPos.add(cord1);
            // if isnt a right limit moves right
            if (!inLimitRight(cord1)) {
                int right = cord1 + 1;
                int broR = array[right];
                while (firstClick == broR) {
                    listPos.add(right);
                    //if "right" is a right limit break
                    if (inLimitRight(right)) break;
                    right += 1;
                    broR = array[right];
                }
            }
            //If isnt a left limit moves left
            if (!inLimitLeft(cord1)) {
                int left = cord1 - 1;
                int broL = array[left];
                while (firstClick == broL) {
                    listPos.add(left);
                    //if "left" is a left limit, then break
                    if (inLimitLeft(left)) break;
                    left -= 1;
                    broL = array[left];
                }
            }
            // I save the size of the list to test in toast // delete later
            int size = listPos.size();
            if (size > 2) {
                return listPos;
                //Toast.makeText(MainActivity.this, "Cant elements: " + size , Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "Delete three o more elements. Between: " + listPos.get(size-1) + "to: " + listPos.get(0) , Toast.LENGTH_SHORT).show();
            } else {
                listPos.clear();
                return listPos;
                //Toast.makeText(MainActivity.this, "Not delete elements. Size: " + size, Toast.LENGTH_SHORT).show();
                //Toast.makeText(MainActivity.this, "Now size is: " + listPos.size(), Toast.LENGTH_SHORT).show();
            }
        } else {
            return new ArrayList();
        }


    }







    private List<Integer> verticalTest(int cord1) {
        List<Integer> listPos = new ArrayList<Integer>();
        int firstClick = array[cord1];

        listPos.add(cord1);
        //If it isnt an upper limit it moves up
        if (cord1 >= 8) {
            int up = cord1 - 8;
            int broU = array[up];
            while (firstClick == broU) {
                listPos.add(up);
                //If "up" is an upper limit, then break
                up -= 8;
                if (up < 0) break;
                broU = array[up];
            }
        }
        //if it isnt a lower limit then it moves down
        if (cord1 <= 55) {
            int down = cord1 + 8;
            int broD = array[down];
            while (firstClick == broD) {
                listPos.add(down);
                down += 8;
                //If the left is a limit to the left then break
                if (down > 63) break;
                broD = array[down];
            }
        }
        //Save the size of the list to try on toast // delete later
        int size = listPos.size();
        if (size > 2) {
            return listPos;
            //Toast.makeText(MainActivity.this, "Cant elements: " + size , Toast.LENGTH_SHORT).show();
            //Toast.makeText(MainActivity.this, "Delete three o more elements. Between: " + listPos.get(size-1) + "to: " + listPos.get(0) , Toast.LENGTH_SHORT).show();
        } else {
            listPos.clear();
            return listPos;
            //Toast.makeText(MainActivity.this, "Not delete elements. Size: " + size, Toast.LENGTH_SHORT).show();
            //Toast.makeText(MainActivity.this, "Now size is: " + listPos.size(), Toast.LENGTH_SHORT).show();
        }
    }







    private void removeGrid(List l1, GridLayout gridL) {
        //Recorro toda la lista 1
        //repeatDelete(l1);

        for (int x = 0; x < l1.size(); x++) {
            int pos = (int) l1.get(x);
            if (array[pos] != -1) {
                int save = array[pos];
                //indiceRun= i;
                //handler.postDelayed(runnableName, 500);
                gridL.removeViewAt(pos);
                Resources resources = getResources();
                ImageView image = new ImageView(this);
                image.setImageDrawable(resources.getDrawable(ArrayImageDark[save]));
                //image.setBackgroundColor(Color.parseColor("#ffffff"));
                gridL.addView(image, pos);
                // I keep the number that refers to the image I saved.
                array[pos] = -1;
            }
        }
    }








    //See if the movement is valid (up, down, left or right)
    private boolean validateMovement(final int first, final int second/*int btnA, int btnB*/) {
        int up = first - 8;
        int down = first + 8;
        int left = first - 1;
        int right = first + 1;
                                                                                                    /* INSPECT RETURN
                                                                                                    if (up == second) Toast.makeText(MainActivity.this, "Valid movement. Up", Toast.LENGTH_SHORT).show();
                                                                                                    else if (down == second) Toast.makeText(MainActivity.this, "Valid movement. Down", Toast.LENGTH_SHORT).show();
                                                                                                    else if (right == second) Toast.makeText(MainActivity.this, "Valid movement. Right", Toast.LENGTH_SHORT).show();
                                                                                                    else if (left == second) Toast.makeText(MainActivity.this, "Valid movement. Left", Toast.LENGTH_SHORT).show();
                                                                                                    else {
                                                                                                        Toast.makeText(MainActivity.this, "Invalid movement.", Toast.LENGTH_SHORT).show();
                                                                                                    }*/
        return ((up == second) || (down == second) || (right == second) || (left == second));
        //return true;
        //else return false;
    }






    //generate a random grid
    private void randomGrid(GridLayout gridLayout, int i) {
        int numberRandom = (int) (Math.random() * 6);
        Resources resources = getResources();
        ImageView image = new ImageView(this);
        image.setImageDrawable(resources.getDrawable(ArrayImage[numberRandom]));
        gridLayout.addView(image, i);
        // I keep the number that refers to the image I saved.
        array[i] = numberRandom;
    }






    private void testLayout(GridLayout gridLayout){
        int aux=0;
        for(int i=0;i<64;i++) {
            ImageView image = new ImageView(this);
            image.setImageResource(ArrayImage[aux]);
            gridLayout.addView(image, i);
            array[i] = aux;
            if (aux == 5)
                aux = 0;
            else
                aux++;
            // PARA PROBAR LA CONDICION DEL DE QUE SI NO HAY COMBINACIONES POSIBLES SE ACTIVA EL BOTON REFRESH
        }
    }







    private void fillGrid() {
        score = 0;
        for (int i = 0; i < 64; i++) {
            if (array[i] == -1) {
                score++;
                gridLayout.removeViewAt(i);
                randomGrid(gridLayout, i);
            }
        }
        int scoreActual = Integer.parseInt((String) txtScore.getText());
        String txt = String.valueOf((scoreActual + score));
        txtScore.setText(txt);
    }






    private boolean movementTest() {
        for (int k = 0; k < 64; k++) {
            List l1 = horizontalTest(k);
            l1.addAll(verticalTest(k));
            if ((l1.size() > 2)) return true;
        }
        return false;
    }






    private boolean inLimitRight(int value) {
        boolean rtn = false;
        for (int i = 0; i < limitRight.length; i++) {
            if (limitRight[i] == value) rtn = true;
        }
        return rtn;
    }






    private boolean inLimitLeft(int value) {
        boolean rtn = false;
        for (int i = 0; i < limitLeft.length; i++) {
            if (limitLeft[i] == value) {
                rtn = true;
            }
        }
        return rtn;
    }






    public void waterfall() {
        for (int x = 0; x < 8; x++) {                                                          //recorro limites inferior horizontal
            int pointerDown = limitDown[x];                                                 //valor de columna abajo
            while (pointerDown > x) {
                while ((array[pointerDown] != -1) && (pointerDown > x))                   //mientras no sea vacia y este dentro de la matriz
                    pointerDown -= 8;
                if (array[pointerDown] == -1) {                                            //mientras sea vacia y este dentro de matriz
                    int auxPointer = pointerDown;
                    while ((array[pointerDown] == -1) && (pointerDown > x))             //mientras el de arriba sea vacio y el indice de arriba sea menos que el indice de abajo
                        pointerDown -= 8;
                    int aux = array[auxPointer];
                    array[auxPointer] = array[pointerDown];
                    array[pointerDown] = aux;
                    pointerDown = auxPointer - 8;
                }
            }
        }

    }




///////////////////////////////////////////BOTONES////////////////////////////////////////////////////////////////////////







    private void recharge() {
        for (int i = 0; i < 64; i++) {
            gridLayout.removeViewAt(i);
            Resources resources = getResources();
            ImageView image = new ImageView(this);
            if (array[i] == -1) {
                image.setImageDrawable(resources.getDrawable(ArrayImage[6]));
            } else {
                image.setImageDrawable(resources.getDrawable(ArrayImage[array[i]]));
            }
            gridLayout.addView(image, i);
        }
    }






    public void onClikTerminar(View view) {
        /*
            Iniciar la activity ranking que muestre el contenido de un JSONarray o de la implementacion hecha
           ,si es que el puntaje entra dentro del ranking
         */

        DialogFragment dialogFragment = new Dialogo();
        dialogFragment.show(getSupportFragmentManager(),"un dialogo");

    }

    @Override
    public void onPositiveButtonClicked() {
        this.finish();
    }

    @Override
    public void onNegativeButtonClicked() {

    }





    public void onClickRefesh(View view) {
        if (masMovimiento()){
            AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
            a_builder.setMessage("Este boton se habilitara solo cuando no tengas mas movimientos para hacer. Reacomodando las gemas para poder continuar jugando.")
                    .setCancelable(false)
                    .setNegativeButton("Volver", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Boton de reacomodo de gemas");
            alert.show();

        } else {
            for (int i = 0; i < array.length; i++) {
                int randomPos = (int) (Math.random() * 63);
                int imgAux = array[randomPos];
                int imgAct = array[i];
                array[i] = imgAux;
                array[randomPos] = imgAct;
            }
            recharge();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (siguiente){
                        //hago la cascada
                        waterfall();
                        //hago que caigan
                        recharge();
                        //relleno
                        fillGrid();
                        siguiente= false;
                        handler.postDelayed(this,1000);
                    }
                    else {
                        if (movementTest()) {
                            for (int j = 0; j < 64; j++) {
                                deleteIfTrue(j);
                            }
                            siguiente= true;
                            handler.postDelayed(this, 1000);

                        }
                        else{
                            setSingleEvent();
                        }



                    }

                }
            }, 1000);
        }
    }






    private boolean masMovimiento(){
        for (int i = 0; i < 64; i++) {
            int arriba = i - 8;
            int abajo = i + 8;
            int izquierda = i - 1;
            int derecha = i + 1;
            if (arriba >= 0) {
                move(i, arriba);
                if (movementTest()) {
                    move(i, arriba);
                    return true;
                }
                move(i, arriba);
            }
            if (abajo <= 63) {
                move(i, abajo);
                if (movementTest()) {
                    move(i, abajo);
                    return true;
                }
                move(i, abajo);
            }
            if (!inLimitRight(izquierda) && ((arriba >= 0))) {
                move(i, izquierda);
                if (movementTest()) {
                    move(i, izquierda);
                    return true;
                }
                else
                    move(i, izquierda);
            }
            if (!inLimitLeft(derecha) && ((abajo < 64))) {
                move(i, derecha);
                if (movementTest()) {
                    move(i, derecha);
                    return true;
                }
                move(i, derecha);
            }
        }
        return false;
    }








    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actionbar, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        this.shareActionProvider = shareActionProvider;
        return true;
    }*/


    public void onClickVerificarPuntaje(View view) {
        AlertDialog.Builder a_builder = new AlertDialog.Builder(MainActivity.this);
        a_builder.setMessage("Si presionas que 'si', se verificara que tu puntuaje entre en el top 10 para guardarlo.\n" +
                "En caso de no entrar al top 10 se te mostrara el Ranking de los 10 mejores puntajes.")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPositiveButton();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = a_builder.create();
        alert.setTitle("¿Usted quiere terminar el juego?");
        alert.show();
    }


    public void onPositiveButton(){
        //Cantidad de puntos
        int scoreActual = Integer.parseInt((String) txtScore.getText());

        //Creo la base de datos y la abro para escritura/lectura
        SQLite admin = new SQLite(this, "jugadores", null, 1);
        SQLiteDatabase db =  admin.getWritableDatabase();

        //PUNTAJE A STRING
        String puntajeStr = String.valueOf(scoreActual);

        //CONSULTA A BASE DE DATOS
        Cursor registros = db.rawQuery("SELECT * FROM jugadores ORDER BY puntaje DESC", null);

        //CUENTO CANTIDAD DE REGISTROS
        int numRows = registros.getCount();

        //Cieerro database
        db.close();

        //SI ES MENOR LO AGREGA DIRECTAMENTE A BASE DE DATOS
        if(numRows < 10){
            //agregar en orden
            Intent i= new Intent (this,IngresaNombre.class);
            i.putExtra("score", puntajeStr);
            i.putExtra("razon", 1);
            startActivityForResult(i, 2);

        }else {  //SINO HAY QUE VER SI EL PUNTAJE ES VALIDO PARA EL RANKING

            //me posiciono en la primer columna
            registros.moveToFirst();
            //control de 10 registros
            int cantidad = 0;
            //si borro
            boolean borrado = false;
            do {
                String cod = registros.getString(0);
                int puntajeInt = Integer.parseInt(registros.getString(2));
                if ((puntajeInt < scoreActual) && (cantidad < 10)){
                    borrado= true;
                    //this.borrarElemento(codigo);
                    Intent i= new Intent (this,IngresaNombre.class);
                    i.putExtra("score", puntajeStr);
                    i.putExtra("razon", "2");
                    i.putExtra("codigo", cod);
                    //i.putExtra("codigo", codigo);
                    startActivityForResult(i,5);
                    break;
                }
                cantidad++;
            } while (registros.moveToNext());
            // si no borre es porque el puntaje es muy bajp entonces muestro el puntaje
            if(!borrado){
                Intent i= new Intent (this,Ranking.class);
                startActivityForResult(i, 3);
            }
        }
        //Cieerro database
        db.close();
    }






    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        this.finish();
    }
    /*
        GUARDAR PUNTAJES EN LA BASE DE DATOS


        ContentValues registro = new ContentValues();
        registro.put("puntaje", puntaje);
        db.insert("jugadores", null, registro);

    */


    /*
      DESPLAZARTE EN UNA TABLA


      registros.moveToFirst();
      Toast.makeText(MainActivity.this, registros.getString(1), Toast.LENGTH_SHORT).show();
      registros.moveToNext();
      Toast.makeText(MainActivity.this, registros.getString(1), Toast.LENGTH_LONG).show();
      db.close();

     */





}











/*





         //ONCLICK BUTTON RESTART LIFE                   // Take out later
         public void restartLife (View view){
             // Restart "life points"
             pressB= 0;
             txtbtn2.setText(String.valueOf(pressB));
             pressA= 0;
             txtbtn1.setText(String.valueOf(pressA));
         }



    --------------------------------------------------------



         final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 1000);





    */





   /*  Runnable runnableRemove = new Runnable() {
        @Override
        public void run() {
            for (int x = 0; x < lis1.size(); x++) {
                int pos = (int) lis1.get(x);
                if (array[pos] != -1){
                    int save = array[pos];
                    //indiceRun= i;
                    //handler.postDelayed(runnableName, 500);
                    gridLayout.removeViewAt(pos);
                    Resources resources = getResources();
                    ImageView image = new ImageView(MainActivity.this);
                    image.setImageDrawable(resources.getDrawable(ArrayImageDark[save]));
                    gridLayout.addView(image, pos);
                    // I keep the number that refers to the image I saved.
                    array[pos] = -1;
                }
            }
        }
    };







   private void removeGrid1(List l1) {
       lis1= l1;
       handler.postDelayed(runnableRemove, 100);
    }

    */







   /*
    private void repeatDelete(List l1){
        for (int i = 0; i < l1.size(); i++){
            int cant = 0;
            int aux =(int)l1.get(i);
            for (int x = 0; x < l1.size(); x++){
                if ((int)l1.get(x) == aux){
                    cant = cant +1;
                }
            }
            if (cant >= 2){
                l1.remove(i);
            }

        }
    }*/








     /* private boolean deleteIfTrue1(int index){
        lis1 = horizontalTest(index);
        lis1.addAll(verticalTest(index));
        if ((lis1.size() > 2)) {
            removeGrid1(lis1);
            //Toast.makeText(MainActivity.this, "Elementos borrados:" + cantList, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }*/

