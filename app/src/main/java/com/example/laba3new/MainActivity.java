package com.example.laba3new;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    Button viewStudentsButton;
    Button addStudentButton;
    Button deleteAllButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        dbHelper.clearAndInsertInitialData();

        viewStudentsButton = findViewById(R.id.button_view_students);
        viewStudentsButton.setOnClickListener(this::onClick);


        addStudentButton = findViewById(R.id.button_add_student);
        addStudentButton.setOnClickListener(v -> addStudent());

        deleteAllButton = findViewById(R.id.button_delete_all);
        deleteAllButton.setOnClickListener(v -> deleteAllStudents());


        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }


    private void addStudent() {
        addStudentButton.setEnabled(false);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();  // Начинаем транзакцию

        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_FIRST_NAME, "Иванов");
            values.put(DatabaseHelper.COLUMN_LAST_NAME, "Иванушка");
            values.put(DatabaseHelper.COLUMN_MIDDLE_NAME, "Иванович");
            values.put(DatabaseHelper.COLUMN_DATE, "2024-11-18");

            // Вставляем данные
            db.insert(DatabaseHelper.TABLE_NAME, null, values);
            db.setTransactionSuccessful();  // Если вставка прошла успешно, фиксируем транзакцию

            Toast.makeText(this, "Запись добавлена", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();  // Завершаем транзакцию
            db.close();  // Закрываем соединение с базой данных
            addStudentButton.setEnabled(true);
        }
    }

    private void deleteAllStudents() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();  // Начинаем транзакцию

        try {
            db.execSQL("DELETE FROM " + DatabaseHelper.TABLE_NAME);
            db.setTransactionSuccessful();  // Фиксируем транзакцию, если удаление прошло успешно

            Toast.makeText(this, "Все записи удалены", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();  // Завершаем транзакцию
            db.close();  // Закрываем соединение с базой данных
        }
    }

    private void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), ViewStudentsActivity.class);
        startActivity(intent);
    }
}