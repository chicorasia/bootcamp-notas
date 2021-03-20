package br.com.chicorialabs.notas.adapter

import android.database.Cursor

interface NotaClickedListener {

    fun notaClickeItem(cursor: Cursor)

    fun notaRemoveItem(cursor: Cursor?)

}