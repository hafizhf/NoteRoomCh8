package andlima.hafizhfy.noteroomch8.view.adapter

import andlima.hafizhfy.noteroomch8.R
import andlima.hafizhfy.noteroomch8.local.room.notetable.Note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*

class AdapterNote(
    private val listNote: List<Note>,
    private var onClick: (Note) -> Unit
) : RecyclerView.Adapter<AdapterNote.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            tv_item_title.text = listNote[position].title
            tv_item_desc.text = listNote[position].description

            item.setOnClickListener {
                onClick(listNote[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return listNote.size
    }
}