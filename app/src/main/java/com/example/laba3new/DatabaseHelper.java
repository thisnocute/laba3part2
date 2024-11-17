package com.example.laba3new;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Имя базы данных и её версия
    private static final String DATABASE_NAME = "students.db";
    private static final int DATABASE_VERSION = 2; // Увеличьте версию базы данных

    // Столбцы таблицы
    public static final String TABLE_NAME = "Одногруппники";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_LAST_NAME = "Фамилия";
    public static final String COLUMN_FIRST_NAME = "Имя";
    public static final String COLUMN_MIDDLE_NAME = "Отчество";
    public static final String COLUMN_DATE = "ВремяДобавления";

    // SQL-запрос для создания таблицы
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_LAST_NAME + " TEXT, " +
            COLUMN_FIRST_NAME + " TEXT, " +
            COLUMN_MIDDLE_NAME + " TEXT, " +
            COLUMN_DATE + " TEXT)";

    // Запрос для удаления таблицы (при обновлении версии базы данных)
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаём таблицу, если она не существует
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Если версия базы данных изменилась, удаляем старую таблицу и создаём новую
        if (oldVersion < newVersion) {
            db.execSQL(SQL_DROP_TABLE);
            onCreate(db);
        }
    }

    // Метод для очистки таблицы и вставки исходных данных
    public void clearAndInsertInitialData() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // Удаляем все данные из таблицы перед вставкой новых
            db.execSQL("DELETE FROM " + TABLE_NAME);

            // Массив с исходными данными
            String[][] names = {
                    {"Иванов", "Иван", "Иванович", "2024-11-17"},
                    {"Петров", "Петр", "Петрович", "2024-11-17"},
                    {"Сидоров", "Сидор", "Сидорович", "2024-11-17"},
                    {"Кузнецов", "Кузьма", "Кузьмич", "2024-11-17"},
                    {"Семенов", "Семен", "Семенович", "2024-11-17"}
            };

            // Вставляем данные в таблицу
            for (String[] name : names) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_LAST_NAME, name[0]);
                values.put(COLUMN_FIRST_NAME, name[1]);
                values.put(COLUMN_MIDDLE_NAME, name[2]);
                values.put(COLUMN_DATE, name[3]);
                db.insert(TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Закрываем соединение с базой данных только после завершения всех операций
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}
