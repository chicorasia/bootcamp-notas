package br.com.chicorialabs.notas

import android.database.Cursor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.RecyclerView
import br.com.chicorialabs.notas.database.NotasDatabaseHelper.Companion.TITLE_NOTAS
import br.com.chicorialabs.notas.database.NotesProvider.Companion.URI_NOTAS
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var recyclerView: RecyclerView
    lateinit var adiciona_fab: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.main_recyclerview)


        adiciona_fab = findViewById(R.id.main_adiciona_fab)
        adiciona_fab.setOnClickListener {  }


    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>  =
        CursorLoader(this, URI_NOTAS, null, null,
                null, TITLE_NOTAS)


    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null) { }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("Not yet implemented")
    }
}