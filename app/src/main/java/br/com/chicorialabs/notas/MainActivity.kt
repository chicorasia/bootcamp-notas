package br.com.chicorialabs.notas

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.BaseColumns._ID
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.chicorialabs.notas.adapter.NotaClickedListener
import br.com.chicorialabs.notas.adapter.NotasAdapter
import br.com.chicorialabs.notas.database.NotasDatabaseHelper.Companion.TITLE_NOTAS
import br.com.chicorialabs.notas.database.NotesProvider.Companion.URI_NOTAS
import br.com.chicorialabs.notas.dialog.NotaDetailFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var recyclerView: RecyclerView
    lateinit var adiciona_fab: FloatingActionButton
    lateinit var adapter: NotasAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = NotasAdapter(object : NotaClickedListener {

            override fun notaClickeItem(cursor: Cursor) {
                val id = cursor.getLong(cursor.getColumnIndex(_ID))
                val fragment = NotaDetailFragment.newInstance(id)
                fragment.show(supportFragmentManager, "dialog")
            }

            override fun notaRemoveItem(cursor: Cursor?) {
                val id = cursor?.getLong(cursor.getColumnIndex(_ID))
                contentResolver.delete(Uri.withAppendedPath(URI_NOTAS, id.toString()),
                null, null)
            }

        })
        adapter.setHasStableIds(true)

        recyclerView = findViewById(R.id.main_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter


        adiciona_fab = findViewById(R.id.main_adiciona_fab)
        adiciona_fab.setOnClickListener { NotaDetailFragment()
                .show(supportFragmentManager, "dialog")
        }

        LoaderManager.getInstance(this).initLoader(0, null, this )

    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>  =
        CursorLoader(this, URI_NOTAS, null, null,
                null, TITLE_NOTAS)


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null) { adapter.setCursor(data) }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter.setCursor(null)
    }
}