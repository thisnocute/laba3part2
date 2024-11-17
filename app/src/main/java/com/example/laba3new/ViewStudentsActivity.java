package com.example.laba3new;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ViewStudentsActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        TextView textView = findViewById(R.id.textview_students);
        if (textView == null) {
            throw new NullPointerException("TextView с id 'textview_students' не найден в макете!");
        }

        dbHelper = new DatabaseHelper(this);
        displayStudents();

        EdgeToEdge.enable(this);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.close();
    }

    private void displayStudents() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME, null);

        StringBuilder stringBuilder = new StringBuilder();
        while (cursor.moveToNext()) {
            int firstnameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME);
            int lastnameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME);
            int middlenameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_MIDDLE_NAME);
            int dateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE);

            // Проверяем, что все необходимые столбцы существуют
            if (firstnameIndex != -1 && lastnameIndex != -1 && middlenameIndex != -1 && dateIndex != -1) {
                String first = cursor.getString(firstnameIndex);
                String last = cursor.getString(lastnameIndex);
                String middle = cursor.getString(middlenameIndex);
                String date = cursor.getString(dateIndex);

                // Формируем строку для каждого студента с переносом строки
                stringBuilder.append(first).append(" ")
                        .append(last).append(" ")
                        .append(middle).append(" - ")
                        .append(date).append("\n");  // \n — это символ новой строки
            }
        }

        cursor.close();
        db.close();

        // Получаем ссылку на TextView и устанавливаем текст
        TextView textView = findViewById(R.id.textview_students);
        if (textView != null) {
            textView.setText(stringBuilder.toString());
        } else {
            // В случае ошибки выводим исключение
            throw new NullPointerException("TextView с id 'textview_students' не найден после вызова findViewById.");
        }
    }
}
