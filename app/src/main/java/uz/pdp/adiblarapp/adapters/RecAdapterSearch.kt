package uz.pdp.adiblarapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import uz.pdp.adiblarapp.adapters.RecAdapterSearch.*
import uz.pdp.adiblarapp.room.entity.Writers
import uz.pdp.adiblarapp.databinding.ItemBinding

//RecyclerView.Adapter<RecAdapterSearch.VH>()
class RecAdapterSearch(var context: Context, var list: ArrayList<Writers>, var str: List<Writers>, var listener: OnMyClickedListener): ListAdapter<Writers, VH>(MyDiffUtil()) {

    var newList = ArrayList(list)

    interface OnMyClickedListener{

        fun onItemClicked(uid: String)

        fun onSavedClicked(writers: Writers, cardView: CardView, position: Int)

    }

    class MyDiffUtil(): DiffUtil.ItemCallback<Writers>() {
        override fun areItemsTheSame(oldItem: Writers, newItem: Writers): Boolean {
            return oldItem.uid == newItem.uid
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Writers, newItem: Writers): Boolean {
            return oldItem == newItem
        }

    }

    inner class VH(var binding: ItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.binding.apply {
            val item = list[position]
            var boolean = false
            for (s in str) {
                if (s.uid == item.uid) {
                    boolean = true
                    break
                }

            }
            if (boolean) {
                savedCard.setCardBackgroundColor(Color.parseColor("#00B238"))
            } else {
                savedCard.setCardBackgroundColor(Color.parseColor("#E5E5E5"))
            }

            Glide.with(context).load(item.image).addListener(object :
                RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    spinKit.visibility = View.GONE
                    nameWriters.text = item.name
                    lifeTxt.text = item.life_sicle
                    return false
                }
            }).into(image)

            savedCard.setOnClickListener {
                listener.onSavedClicked(item, savedCard, position)
            }

            bigCard.setOnClickListener {
                listener.onItemClicked(item.uid!!)
            }

        }
    }


//    override fun getItemCount(): Int = list.size

//    override fun getFilter(): Filter = filter
//
//    private val filter = object : Filter() {
//        override fun performFiltering(constraint: CharSequence?): FilterResults {
//            var filterList = ArrayList<Writers>()
//
//            if (constraint == null || constraint.isEmpty()) {
//                filterList.addAll(newList)
//            } else {
//                var stringPattern = constraint.toString().lowercase().trim()
//
//                for (passport in newList) {
//                    if (passport.name?.lowercase()!!.contains(stringPattern)) {
//                        filterList.add(passport)
//                    }
//                }
//            }
//
//            val filterResults = FilterResults()
//            filterResults.values = filterList
//            filterResults.count = filterList.size
//
//            return filterResults
//        }
//
//        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//            list.clear()
//            list.addAll(results?.values as ArrayList<Writers>)
//            notifyDataSetChanged()
//        }
//
//    }
}