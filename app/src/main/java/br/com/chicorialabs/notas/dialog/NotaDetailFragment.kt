package br.com.chicorialabs.notas.dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import br.com.chicorialabs.notas.R
import br.com.chicorialabs.notas.database.NotasDatabaseHelper.Companion.DESCRIPTION_NOTAS
import br.com.chicorialabs.notas.database.NotasDatabaseHelper.Companion.TITLE_NOTAS
import br.com.chicorialabs.notas.database.NotesProvider.Companion.URI_NOTAS

class NotaDetailFragment : DialogFragment(), DialogInterface.OnClickListener {

    private lateinit var notaEditTitle: EditText
    private lateinit var notaEditDescription: EditText
    private var id: Long = 0

    companion object {

        private const val EXTRA_ID = "id"

        fun newInstance(id: Long): NotaDetailFragment {
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID, id)

            val notaFragment = NotaDetailFragment()
            notaFragment.arguments = bundle
            return notaFragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.nota_detail, null)

        notaEditTitle = view?.findViewById(R.id.nota_edt_title) as EditText
        notaEditDescription = view?.findViewById(R.id.nota_edt_descricao) as EditText

        var newNota = true
        if (arguments != null && arguments?.getLong(EXTRA_ID) != 0L) {
            id = arguments?.getLong(EXTRA_ID) as Long
            val uri = Uri.withAppendedPath(URI_NOTAS, id.toString())
            val cursor = activity?.contentResolver?.query(uri, null, null, null, null)

            if(cursor?.moveToNext() as Boolean) {
                newNota = false
                notaEditTitle.setText(cursor.getString(cursor.getColumnIndex(TITLE_NOTAS)))
                notaEditDescription.setText(cursor.getString(cursor.getColumnIndex(DESCRIPTION_NOTAS)))
            }
            cursor.close()
        }

        return AlertDialog.Builder(activity as Activity)
                .setTitle(if (newNota) "Nova anotação" else "Editar anotação")
                .setView(view)
                .setPositiveButton("Salvar", this)
                .setNegativeButton("Cancelar", this)
                .create()
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val values = ContentValues()
        values.put(TITLE_NOTAS, notaEditTitle.text.toString())
        values.put(DESCRIPTION_NOTAS, notaEditDescription.text.toString())

        if (id != 0L) { //já existe registro com esse id...
            val uri = Uri.withAppendedPath(URI_NOTAS, id.toString())
            context?.contentResolver?.update(uri, values, null, null)
        } else {
            context?.contentResolver?.insert(URI_NOTAS, values)
        }
    }


}