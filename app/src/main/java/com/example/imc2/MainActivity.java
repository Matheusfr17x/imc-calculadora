package com.example.imc2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView textView_resultado;
    private EditText editText_peso;
    private EditText editText_altura;

    private Button buttonCalcular;
    private Button buttonCompartilhar;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView_resultado = findViewById(R.id.textView_resultado);
        buttonCalcular = findViewById(R.id.buttonCalcular);
        buttonCompartilhar = findViewById(R.id.btnCompartilhar);
        editText_peso = findViewById(R.id.editPeso);
        editText_altura = findViewById(R.id.editAltura);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        buttonCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText_peso.getText().toString().isEmpty() && !editText_altura.getText().toString().isEmpty()) {
                    double peso = Double.parseDouble(editText_peso.getText().toString());
                    double altura = Double.parseDouble(editText_altura.getText().toString());
                    double IMC = peso / (altura * altura);

                    DecimalFormat decimalFormat = new DecimalFormat("#.##");
                    String resultadoFormatado = decimalFormat.format(IMC);

                    String resultado;

                    if (IMC < 18.5) {
                        resultado = resultadoFormatado + "\n magreza";
                    } else if (IMC > 18.5 && IMC <= 24.9) {
                        resultado = resultadoFormatado + "\n normal";
                    } else if (IMC > 25 && IMC <= 29.9) {
                        resultado = resultadoFormatado + "\n Sobrepeso";
                    } else if (IMC > 30 && IMC <= 34.9) {
                        resultado = resultadoFormatado + "\n Obesidade";
                    } else if (IMC > 35 && IMC <= 39.9) {
                        resultado = resultadoFormatado + "\n Obesidade grau II";
                    } else {
                        resultado = resultadoFormatado + "\n Obesidade grau III";
                    }

                    textView_resultado.setText(resultado);
                    saveResultado(resultado);
                }
            }
        });

        buttonCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String resultado = getResultado();
                if (resultado != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, resultado);
                    startActivity(Intent.createChooser(intent, "Compartilhar resultado"));
                }
            }
        });

        String resultado = getResultado();
        if (resultado != null) {
            textView_resultado.setText(resultado);
        }
    }

    private void saveResultado(String resultado) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("resultado", resultado);
        editor.apply();
    }

    private String getResultado() {
        return sharedPreferences.getString("resultado", null);
    }
}