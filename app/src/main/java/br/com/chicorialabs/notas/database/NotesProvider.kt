package br.com.chicorialabs.notas.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.provider.BaseColumns._ID
import br.com.chicorialabs.notas.database.NotasDatabaseHelper.Companion.TABLE_NOTAS

class NotesProvider : ContentProvider() {

    private lateinit var mUriMatcher: UriMatcher
    private lateinit var dbHelper: NotasDatabaseHelper

    override fun onCreate(): Boolean {

        mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        mUriMatcher.addURI(AUTHORITY, "notas", NOTAS)
        mUriMatcher.addURI(AUTHORITY, "notas/#", NOTAS_BY_ID)
        if (context != null) {
            dbHelper = NotasDatabaseHelper(context as Context)
        }
        return true

    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        if (mUriMatcher.match(uri) == NOTAS_BY_ID) {
            val db = dbHelper.writableDatabase
            val linhasAfetadas = db.delete(
                    TABLE_NOTAS,
                    "$_ID =?",
                    arrayOf(uri.lastPathSegment)
            )
            db.close()
            context?.contentResolver?.notifyChange(uri, null)
            return linhasAfetadas
        } else {
            throw UnsupportedSchemeException("Operação não suportada nessa Uri")
        }
    }

    override fun getType(uri: Uri): String? =
            throw UnsupportedSchemeException("Operação não implementada")


    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if (mUriMatcher.match(uri) == NOTAS) {
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val id: Long = db.insert(TABLE_NOTAS, null, values)
            val insertUri = Uri.withAppendedPath(BASE_URI, id.toString())
            db.close()
            context?.contentResolver?.notifyChange(uri, null)
            return insertUri
        } else {
            throw UnsupportedSchemeException("Operação não suportada nessa Uri")
        }
    }


    override fun query(
            uri: Uri, projection: Array<String>?, selection: String?,
            selectionArgs: Array<String>?, sortOrder: String?,
    ): Cursor? {
        return when {
            mUriMatcher.match(uri) == NOTAS -> {
                val db: SQLiteDatabase = dbHelper.writableDatabase
                val cursor: Cursor = db.query(TABLE_NOTAS,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder)
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            mUriMatcher.match(uri) == NOTAS_BY_ID -> {
                val db: SQLiteDatabase = dbHelper.writableDatabase
                val cursor: Cursor = db.query(TABLE_NOTAS,
                        projection,
                        "$_ID = ?",
                        arrayOf(uri.lastPathSegment),
                        null,
                        null,
                        sortOrder)
                cursor.setNotificationUri(context?.contentResolver, uri)
                cursor
            }
            else -> {
                throw UnsupportedSchemeException("Operação não suportada nessa Uri")
            }


        }
    }

    override fun update(
            uri: Uri, values: ContentValues?, selection: String?,
            selectionArgs: Array<String>?,
    ): Int {
        if (mUriMatcher.match(uri) == NOTAS_BY_ID) {
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val linhasAfetadas = db.update(TABLE_NOTAS,
                    values,
                    "$_ID = ?",
                    arrayOf(uri.lastPathSegment))
            db.close()
            context?.contentResolver?.notifyChange(uri, null)
            return linhasAfetadas
        } else {
            throw UnsupportedSchemeException("Operação não suportada nessa Uri")
        }
    }

    companion object {
        const val AUTHORITY: String = "br.com.chicorialabs.applicationcontentprovider.provider"
        val BASE_URI: Uri = Uri.parse("content://$AUTHORITY")
        val URI_NOTAS: Uri = Uri.withAppendedPath(BASE_URI, "notas")

        //content://br.com.chicorialabs.applicationcontentprovider.provider/notas

        const val NOTAS = 1
        const val NOTAS_BY_ID = 2
    }
}