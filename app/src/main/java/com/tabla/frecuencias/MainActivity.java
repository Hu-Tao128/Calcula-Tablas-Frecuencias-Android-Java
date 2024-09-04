package com.tabla.frecuencias;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private double rango;
    private double amplitud;
    private double Intervalo;
    double Mediana;
    //double paso1;

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText txtnumMayor = findViewById(R.id.txtnumMayor);
        //findViewById busca una vista de igual id en el xml
        EditText txtnumMenor = findViewById(R.id.txtnumMenor);
        EditText txtnumDatos = findViewById(R.id.txtnumDatos);

        Button btncalcular = findViewById(R.id.btncalcular);
        //tblTablaKRA = findViewById(R.id.tblTablaKRA);

        TextView lblIntervaloVal = findViewById(R.id.lblIntervaloVal);
        TextView lblrangoVal = findViewById(R.id.lblrangoVal);
        TextView lblamplitVal = findViewById(R.id.lblamplitVal);


        btncalcular.setOnClickListener(view -> {

            String num1Str = txtnumMayor.getText().toString();
            String num2Str = txtnumMenor.getText().toString();
            String num3Str = txtnumDatos.getText().toString();

            // Verificar que ambos campos no estén vacíos
            if (!num1Str.isEmpty() && !num2Str.isEmpty() && !num3Str.isEmpty()) {
                try {
                    // Convertir las cadenas a enteros
                    int Num1 = Integer.parseInt(num1Str);
                    int Num2 = Integer.parseInt(num2Str);

                    TableLayout tblfrecuencias = findViewById(R.id.tblfrecuencias);
                    TableLayout tbltriplem = findViewById(R.id.tbltriplem);

                    // Obtén el número total de filas en el TableLayout
                    int totalFilas = tblfrecuencias.getChildCount();

                    // Itera desde la segunda fila hasta la última
                    for (int Filremove = totalFilas - 1; Filremove > 0; Filremove--) {
                        // Obtiene una referencia a la fila actual
                        TableRow fila = (TableRow) tblfrecuencias.getChildAt(Filremove);
                        TableRow fila1 = (TableRow) tbltriplem.getChildAt(Filremove);

                        // Elimina la fila del TableLayout
                        tblfrecuencias.removeView(fila);
                        tbltriplem.removeView(fila1);
                    }


                    rango = (Num1 - Num2);

                    //Toast.makeText(MainActivity.this, "El Rango es: " + rango, Toast.LENGTH_SHORT).show();
                    //se cambia toastmessage por maketext y el show se separa con un punto .show()


                    // Obtener el texto del EditText y luego convertirlo a double
                    short valorInter = (short) Double.parseDouble(txtnumDatos.getText().toString());

                    Intervalo = 1+(3.3*(Math.log10(valorInter)));

                    //math,round redondea el numero mas cercano el standar que usamos siempre

                    amplitud = (Math.round(rango / Intervalo));

                    // Valores que queremos agregar a la fila (en este ejemplo, solo números para ilustrar)
                    // format("Rango:\n%s", rango) //darle el valor de string, como vimos en el button string.valueof(int)

                    Intervalo = Math.round(Intervalo);
                    lblIntervaloVal.setText(format("Intervalo:\n%s", Intervalo)); //con el valor setText obtenemos el texto a escribir
                    lblamplitVal.setText(format("Amplitud:\n%s", amplitud)); // \n ees el salto de linea
                    lblrangoVal.setText(format("Rango:\n%s", rango));


                    List<Integer> InicioInterval = new ArrayList<>();
                    List<Integer> FinalInterval = new ArrayList<>();
                    final int[] Inicio = {Num2}; // Llama el valor del número menor para iniciar el conteo

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Ingrese las frecuencias");
                    LinearLayout layout = new LinearLayout(MainActivity.this);
                    layout.setOrientation(LinearLayout.VERTICAL);

                    for (int numeracion = 0; numeracion < Intervalo; numeracion++) {
                        int FinInterval = (int) (Inicio[0] + amplitud);
                        InicioInterval.add(Inicio[0]);  // Agrega el valor actual de Inicio a InicioInterval
                        FinalInterval.add(FinInterval); // Agrega el valor actual de FinInterval a FinalInterval

                        final EditText editText = new EditText(MainActivity.this);
                        editText.setHint("Frecuencia de: " + String.format("%d - %d", Inicio[0], FinInterval));
                        editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);

                        Inicio[0] = (int) (Inicio[0] + amplitud);
                        layout.addView(editText);
                    }


                    builder.setView(layout);

                    builder.setPositiveButton("Calcular", new DialogInterface.OnClickListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Aquí puedes recopilar las frecuencias ingresadas
                            List<Integer> frecuencias = new ArrayList<>();

                            double sigmaFXi = 0.0;
                            int FreeAcm = 0;
                            double FreeRelAcm = 0.0;
                            byte cont = 0;
                            int Micha = Math.round((valorInter/2));
                            // Accede a los valores ingresados en los EditText
                            for (int VAl = 0; VAl < Intervalo; VAl++) {
                                EditText editText = (EditText) layout.getChildAt(VAl);
                                String frecuenciaStr = editText.getText().toString();

                                if (!TextUtils.isEmpty(frecuenciaStr)) {
                                    int frecuencia = Integer.parseInt(frecuenciaStr);
                                    frecuencias.add(frecuencia);


                                    TableLayout tblfrecuencias = findViewById(R.id.tblfrecuencias);

                                    // Crear una nueva fila y agregar celdas con los valores
                                    TableRow tableRow = new TableRow(MainActivity.this);//mainactivity.this declara donde se va a vizualizar // el contexto

                                    TextView textView1 = new TextView(MainActivity.this);
                                    textView1.setText(String.valueOf(VAl + 1));
                                    textView1.setGravity(Gravity.CENTER);
                                    //textView1.set(0, 0, 2, 0);
                                    tableRow.addView(textView1);

                                    TextView textView2 = new TextView(MainActivity.this);
                                    textView2.setText(format("%d - %d", InicioInterval.get(VAl), FinalInterval.get(VAl))); //%d se utiliza en cadenas de formato para insertar valores enteros en la cadena.
                                    textView2.setGravity(Gravity.CENTER);
                                    tableRow.addView(textView2);

                                    TextView textView3 = new TextView(MainActivity.this);
                                    double marcaClasse = (InicioInterval.get(VAl) + FinalInterval.get(VAl)) / 2;
                                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    textView3.setText(String.valueOf(decimalFormat.format(marcaClasse)));
                                    textView3.setGravity(Gravity.CENTER);
                                    //textView3.setPadding(8, 8, 8, 8);
                                    tableRow.addView(textView3);

                                    TextView lblFrecc = new TextView(MainActivity.this);
                                    lblFrecc.setText(String.valueOf(frecuencia));
                                    lblFrecc.setGravity(Gravity.CENTER);
                                    tableRow.addView(lblFrecc);

                                    TextView textView4 = new TextView(MainActivity.this);
                                    FreeAcm = FreeAcm + frecuencia; //Frecuancia acumulada, un acumulador;
                                    textView4.setText(String.valueOf(FreeAcm));
                                    textView4.setGravity(Gravity.CENTER);



                                    //int valor = 0;
                                    if (FreeAcm >= Micha){

                                        if (cont < 1){

                                            cont += 1;
                                            double LimInf;
                                            double ValFrecc;
                                            double ValFreccMinus;

                                            ValFrecc = frecuencias.get(VAl);
                                            LimInf = InicioInterval.get(VAl);
                                            ValFreccMinus = FreeAcm - ValFrecc;

                                            double paso1 = ((Micha-ValFreccMinus)/ValFrecc);
                                            Mediana = (float) (LimInf + (paso1 * amplitud));
                                        }

                                    }



                                    //textView3.setPadding(8, 8, 8, 8);
                                    tableRow.addView(textView4);

                                    TextView textView5 = new TextView(MainActivity.this);
                                    double FreccRelVal = (double) frecuencia / valorInter;
                                    DecimalFormat decimal3cifras = new DecimalFormat("#.###");
                                    textView5.setText(String.valueOf(decimal3cifras.format(FreccRelVal)));
                                    textView5.setGravity(Gravity.CENTER);
                                    //textView3.setPadding(8, 8, 8, 8);
                                    tableRow.addView(textView5);

                                    TextView textView6 = new TextView(MainActivity.this);
                                    FreeRelAcm = FreeRelAcm + FreccRelVal;
                                    //DecimalFormat decimalFormat = new DecimalFormat("#.##");
                                    textView6.setText(String.valueOf(decimalFormat.format(FreeRelAcm)));
                                    textView6.setGravity(Gravity.CENTER);
                                    tableRow.addView(textView6);

                                    TextView textView7 = new TextView(MainActivity.this);
                                    int Grados = (int) Math.round(FreccRelVal * 360); //Frecuancia Rel * 360 grados
                                    textView7.setText(Grados + "°");
                                    textView7.setGravity(Gravity.CENTER);
                                    //textView3.setPadding(8, 8, 8, 8);
                                    tableRow.addView(textView7);

                                    TextView textView8 = new TextView(MainActivity.this);
                                    int FXi = (int) (frecuencia * marcaClasse);
                                    textView8.setText(String.valueOf(FXi));
                                    sigmaFXi = (int) (sigmaFXi + FXi);
                                    //textView3.setPadding(8, 8, 8, 8);
                                    textView8.setGravity(Gravity.CENTER);
                                    tableRow.addView(textView8);


                                    // Agregar la fila a la tabla
                                    tblfrecuencias.addView(tableRow);

                                    if (VAl == Intervalo - 1 ) {
                                        TableRow sumaRow = new TableRow(MainActivity.this);

                                        for (int columna = 0; columna < 9; columna++) {
                                            TextView textView = new TextView(MainActivity.this);

                                            if (columna == 0) { // Mostrar la suma de la fila 4 debajo de la columna correspondiente
                                                textView.setText("Suma Total:");
                                                textView.setGravity(Gravity.CENTER);
                                            } else if (columna == 3) { // Mostrar la suma de la fila 9 debajo de la columna correspondiente
                                                textView.setText(String.valueOf(FreeAcm));
                                                textView.setGravity(Gravity.CENTER);
                                            } else if (columna == 8) { // Mostrar la suma de la fila 9 debajo de la columna correspondiente
                                                textView.setText(String.valueOf(sigmaFXi));
                                                textView.setGravity(Gravity.CENTER);
                                            } else {
                                                textView.setText(""); // Dejar las otras celdas en blanco
                                            }

                                            sumaRow.addView(textView);
                                        }

                                        // Agregar la fila de suma al TableLayout
                                        tblfrecuencias.addView(sumaRow);
                                    }

                                } else {
                                    // Manejo de errores si el usuario no ingresó un valor válido
                                    Toast.makeText(MainActivity.this, "Ingrese un valor válido en Frecuencia " + (VAl + 1), Toast.LENGTH_SHORT).show();
                                    return; // Sale de la función onClick
                                }
                            }

                            TableRow tableRow1 = new TableRow(MainActivity.this);

                            TableLayout tbltriplem = findViewById(R.id.tbltriplem);

                            double media = sigmaFXi/valorInter;

                            DecimalFormat decimal4cifras = new DecimalFormat("#.####");

                            //Media
                            TextView textView9 = new TextView(MainActivity.this);
                            textView9.setText(String.valueOf(decimal4cifras.format(media)));
                            //textView3.setPadding(8, 8, 8, 8);
                            textView9.setGravity(Gravity.CENTER);
                            tableRow1.addView(textView9);


                            TextView textView10 = new TextView(MainActivity.this);
                            textView10.setText(String.valueOf(decimal4cifras.format(Mediana)));
                            //textView3.setPadding(8, 8, 8, 8);
                            textView10.setGravity(Gravity.CENTER);
                            tableRow1.addView(textView10);


                            //Moda o Modas
                            TextView textView11 = new TextView(MainActivity.this);
                            textView11.setText("Hu Tao Main");
                            //textView3.setPadding(8, 8, 8, 8);
                            textView11.setGravity(Gravity.CENTER);
                            tableRow1.addView(textView11);

                            tbltriplem.addView(tableRow1);

                            dialog.dismiss();
                        }
                    });


                    AlertDialog.Builder cancelar = builder.setNegativeButton("Cancelar",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    // return null;
                                }

                            });
                    builder.show();

                } catch (NumberFormatException e) {
                    // Manejar el caso en el que las cadenas no se puedan convertir a números
                    Toast.makeText(this, "Por favor, ingresa números válidos.", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Mostrar un mensaje de error si alguno de los campos está vacío
                Toast.makeText(this, "Por favor, llena ambos campos.", Toast.LENGTH_SHORT).show();
            }


        });
    }
}
