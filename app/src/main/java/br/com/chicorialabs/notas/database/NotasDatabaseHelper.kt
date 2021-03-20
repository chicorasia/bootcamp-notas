package br.com.chicorialabs.notas.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class NotasDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    "databaseNotas",
    null,
    1
) {

    override fun onCreate(db: SQLiteDatabase?) {

        val sql = "CREATE TABLE $TABLE_NOTAS (" +
                "$_ID INTEGER NOT NULL PRIMARY KEY," +
                "$TITLE_NOTAS TEXT NOT NULL," +
                "$DESCRIPTION_NOTAS TEXT NOT NULL)"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }


    companion object {

        const val TABLE_NOTAS = "Notas"
        const val TITLE_NOTAS = "title"
        const val DESCRIPTION_NOTAS = "descricao"
    }
}