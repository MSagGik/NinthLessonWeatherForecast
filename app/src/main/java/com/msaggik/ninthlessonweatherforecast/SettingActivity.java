package com.msaggik.ninthlessonweatherforecast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    // создание полей
    private Button buttonSetting; // поле кнопки обработки данных
    private EditText editCity; // поле выбора города
    private SharedPreferences settings; // поле настроек приложения
    private SharedPreferences.Editor editor; // поле для добавления новых данных в настройки
    private Intent intent; // поле намерения переключения активностей
    private final String APP_WEATHER = "Weather"; // константа названия настроек
    private final String CITY = "City"; // константа названия переменной города

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // присваивание id полям
        buttonSetting = findViewById(R.id.buttonSetting);
        editCity = findViewById(R.id.editCity);

        // создание объекта работы с настройками приложения
        settings = getSharedPreferences(APP_WEATHER, MODE_PRIVATE);

        // обработка нажатия кнопки
        buttonSetting.setOnClickListener(listener);
    }

    // создание слушателя
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            // Считывание вводимого значения города
            String nameCity = editCity.getText().toString();

            // если пользователь ничего не ввёл, то остаёмся в этой же активности
            if (nameCity.equals("")) {
                // сообщение пользователю об отсутствии введённого города
                Toast toast = Toast.makeText(getApplicationContext(), "Вы не ввели название населённого пункта", Toast.LENGTH_SHORT);
                toast.show();
            } else { // иначе сохраняем (пересохраняем) эти данные и переключаемся в активность прогноза погоды
                // запись новой настройки
                editor = settings.edit(); // создание объекта для доступа к изменению настроек
                editor.putString(CITY, nameCity); // запись настроек
                editor.apply(); // сохранение настроек
                // переключение на активность просмотра погоды
                intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }
        }
    };
}