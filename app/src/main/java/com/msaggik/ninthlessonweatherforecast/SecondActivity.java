package com.msaggik.ninthlessonweatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

public class SecondActivity extends AppCompatActivity implements Runnable{

    // создание полей
    private TextView infoCity; // поле информации о населённом пункте
    private TextView infoTemperature; // поле информации о температуре
    private Button buttonSetting; // поле кнопки перехода к настройкам
    private SharedPreferences settings; // поле настроек приложения
    private final String APP_WEATHER = "Weather"; // константа названия настроек
    private final String CITY = "City"; // константа названия переменной города
    // дополнительные поля интернет соединения
    private final String URL_SERVER = "https://api.openweathermap.org/data/2.5/weather?q="; // url сервера
    private final String KEY = "&appid=noKey"; // ключ доступа к сервисам сервера (получается при регистрации на https://openweathermap.org)
    private final String EXTRA_OPTIONS = "&units=metric&lang=ru"; // настройки поиска на русском языке
    private String choiceCity; // поле названия населённого пункта
    private String request; // url для запросов на сервер
    private String response; // ответ с сервера в виде JSON
    private HttpsURLConnection connection; // поле интернет соединения
    private Handler handler; // создание обработчика событий
    private JSONObject jsonObject; // создание поля JSON объекта
    private Intent intent; // поле намерения переключения активностей

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        // присваивание id полям
        infoCity = findViewById(R.id.infoCity);
        infoTemperature = findViewById(R.id.infoTemperature);
        buttonSetting = findViewById(R.id.buttonSetting);

        // создание объекта работы с настройками приложения
        settings = getSharedPreferences(APP_WEATHER, MODE_PRIVATE);
        // считывание настроек выбранного города, данной переменной назначается NoCity если данной настройки нет
        choiceCity = settings.getString(CITY, "NoCity");

        // вывод на экран информации о городе
        infoCity.setText("В населённом пункте " + choiceCity);

        infoTemperature.setText("Данные обновляются ..."); // вывод данных до получения данных с сервера

        handler = new Handler(); // создание объекта обработчика сообщений
        new Thread(this).start(); // запуск фонового потока

        // обработка нажатия кнопоки
        buttonSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // переключение на активность просмотра погоды
                intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    // метод дополнительного потока для интернет соединения и определения погоды в заданном населённом пункте
    @Override
    public void run() {
        // определение url для запросов на сервер
        request = URL_SERVER + choiceCity + KEY + EXTRA_OPTIONS;
        // запрос на сервер
        try {
            URL url = new URL(request); // создание url ссылки для запроса на сервер
            connection = (HttpsURLConnection) url.openConnection(); // открытие соединения с сервером
            connection.connect(); // соединение с сервером

            InputStream stream = connection.getInputStream(); // Считывание данных из потока ответа сервера
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream)); // Запись данных и выделение памяти

            StringBuffer buffer = new StringBuffer(); // Запись данных и выделение памяти
            String line = ""; // По умолчанию пустая строка

            while ((line = reader.readLine()) != null) // Постройное считывание текста
                buffer.append(line).append("\n");
            response = buffer.toString(); // текстовый ответ с сервера

            jsonObject = new JSONObject(response); // создание JSON объекта по ответу с сервера

            // задание на обработчик сообщений обновление TextView с температурой
            handler.post(new Runnable() {
                @Override
                public void run() {
                    // вывод данных с JSON файла
                    try {
                        infoTemperature.setText(jsonObject.getJSONObject("main").getDouble("temp") + " градусов");
                    } catch (JSONException e) { // исключение отсутствия JSON объекта
                        e.printStackTrace();
                    }
                }
            });

        } catch (MalformedURLException e) { // исключение на случай отсутствия ссылки request
            e.printStackTrace();
        } catch (IOException e) { // исключение на случай отсутствия соединения
            e.printStackTrace();
        } catch (JSONException e) { // исключение отсутствия JSON объекта
            e.printStackTrace();
        }
    }
}