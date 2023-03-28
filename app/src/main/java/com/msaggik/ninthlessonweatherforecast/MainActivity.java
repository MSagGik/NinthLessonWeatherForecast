package com.msaggik.ninthlessonweatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    // создание полей
    private ImageButton imageButton;
    private SharedPreferences settings; // поле настроек приложения
    private Intent intent; // поле намерения переключения активностей
    private final String APP_WEATHER = "Weather"; // константа названия настроек
    private final String CITY = "City"; // константа названия переменной города

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // присваивание id полям
        imageButton = findViewById(R.id.button);

        // создание объекта работы с настройками приложения
        settings = getSharedPreferences(APP_WEATHER, MODE_PRIVATE);

        // обработка нажатия кнопки
        imageButton.setOnClickListener(listener);
    }

    // создание слушателя
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // считывание настроек выбранного города, данной переменной назначается NoCity если данной настройки нет
            String choiceCity = settings.getString(CITY, "NoCity");

            // если данной настройки не нашлось, то переключаемся на активность настроек города
            if (choiceCity.equals("NoCity")) {
                // переключение на активность настроек
                intent = new Intent(getApplicationContext(), SettingActivity.class);
            } else { // иначе переключаемся на активность показа погоды
                // переключение на активность просмотра погоды
                intent = new Intent(getApplicationContext(), SecondActivity.class);
            }
            startActivity(intent);
        }
    };
}