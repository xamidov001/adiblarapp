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
import uz.pdp.adiblarapp.room.entity.Writers
import uz.pdp.adiblarapp.databinding.ItemBinding
import java.io.Writer

//RecyclerView.Adapter<RecAdapter.VH>()
class RecAdapter(var context: Context, var list: List<Writers>, var str: List<Writers>, var listener: OnMyClickedListener): ListAdapter<Writers, RecAdapter.VH>(MyDiffUtil()) {

    interface OnMyClickedListener{

        fun onItemClicked(uid: Writers)

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
            val item = getItem(position)
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
                listener.onItemClicked(item)
            }

        }
    }


//    override fun getItemCount(): Int = list.size

}