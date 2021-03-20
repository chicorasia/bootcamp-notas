package br.com.chicorialabs.notas.adapter

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.chicorialabs.notas.R
import br.com.chicorialabs.notas.database.NotasDatabaseHelper.Companion.DESCRIPTION_NOTAS
import br.com.chicorialabs.notas.database.NotasDatabaseHelper.Companion.TITLE_NOTAS

class NotasAdapter(val listener: NotaClickedListener): RecyclerView.Adapter<NotasViewHolder>() {

    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotasViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.notas_item, parent, false)
        return NotasViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotasViewHolder, position: Int) {
        mCursor?.moveToPosition(position)

        holder.tituloTxt.text = mCursor?.getString(mCursor?.getColumnIndex(TITLE_NOTAS) as Int)
        holder.notaTxt.text = mCursor?.getString(mCursor?.getColumnIndex(DESCRIPTION_NOTAS) as Int)
        holder.excluirBtn.setOnClickListener {
            mCursor?.moveToPosition(position)
            listener.notaRemoveItem(mCursor as Cursor)
            notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener {
            listener.notaClickeItem(mCursor as Cursor)
        }
    }

    override fun getItemCount(): Int = if(mCursor != null) mCursor?.count as Int else 0


    fun setCursor(novoCursor: Cursor){
        mCursor = novoCursor
        notifyDataSetChanged()
    }


}

class NotasViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    val tituloTxt = itemView.findViewById<TextView>(R.id.card_title_txt)
    val notaTxt = itemView.findViewById<TextView>(R.id.card_descricao_txt)
    val excluirBtn = itemView.findViewById<Button>(R.id.card_excluir_btn)

    fun bindDados(){

    }

}
