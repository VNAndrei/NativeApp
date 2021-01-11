package ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.shelf

import android.media.ThumbnailUtils
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ro.ubbcluj.scs.andreiverdes.beerkeeper.R
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.Beer
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.data.BeerDetails
import ro.ubbcluj.scs.andreiverdes.beerkeeper.beershelf.taste.TasteFragment
import java.io.File
import java.net.URI


class BeerShelfAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<BeerShelfAdapter.ViewHolder>() {
    var values = emptyList<Beer>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var details = emptyList<BeerDetails>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_beer, parent, false)
        return ViewHolder(view)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemView.tag = item
        holder.type.text = item.type
        holder.abv.text = item.abv.toString() + "%"
        holder.name.text = item.name
        var detail: BeerDetails? = null
        details.forEach { d ->
            if (d._id == item._id)
                detail = d
        }

        if (detail?.imagePath != null) {
            val file = File(detail?.imagePath)
            val size = Size(100,100)
            val thumb = ThumbnailUtils.createImageThumbnail(file, size,null)
            holder.image.setImageBitmap(thumb)
            holder.image.visibility = View.VISIBLE
        }else{
            holder.image.visibility=View.GONE
        }

        holder.itemView.setOnClickListener { view ->
            val item = view.tag as Beer
            fragment.findNavController().navigate(R.id.action_beerShelfFragment_to_tasteFragment, Bundle().apply {
                putString(TasteFragment.ITEM_ID, item._id)
            })
        }
    }
    override fun getItemId(position: Int): Long {
        val product: Beer = values[position]
        return product._id.toLong()
    }
    override fun getItemCount(): Int {
        return values.size
    }
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val type: TextView = view.findViewById(R.id.type)
        val abv: TextView = view.findViewById(R.id.abv)
        val name: TextView = view.findViewById(R.id.name)
        val image: ImageView = view.findViewById(R.id.image)
    }
}