package uz.pdp.adiblarapp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.pdp.adiblarapp.R
import uz.pdp.adiblarapp.adapters.RecAdapter
import uz.pdp.adiblarapp.adapters.RecAdapterSaved
import uz.pdp.adiblarapp.room.entity.Writers
import uz.pdp.adiblarapp.databinding.FragmentSavedBinding
import uz.pdp.adiblarapp.room.database.DbHelper
import uz.pdp.telegramm.preference.MyShared

class SavedFragment : Fragment(R.layout.fragment_saved) {

    private val binding by viewBinding(FragmentSavedBinding::bind)
    private lateinit var list: ArrayList<Writers>
    private var myShared = MyShared
    private lateinit var strList: ArrayList<Writers>
    private lateinit var recAdapter: RecAdapterSaved
    private lateinit var dbHelper: DbHelper
    private val gson = Gson()

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myShared = MyShared.getInstance(requireContext())
        dbHelper = DbHelper.getInstance(requireContext())
        loadPreferences()

        list = ArrayList()

        binding.apply {

            recAdapter = RecAdapterSaved(requireContext(), object : RecAdapterSaved.OnMyClickedListener{
                override fun onItemClicked(uid: Writers) {
                    val bundle = Bundle()
                    bundle.putSerializable("writer", uid)
                    findNavController().navigate(R.id.aboutFragment, bundle)
                }

                override fun onSavedClicked(item: Writers, cardView: CardView, position: Int) {
                    var boolean = false
                    for (s in strList) {
                        if (s.uid == item.uid) {
                            boolean = true
                            item.id = s.id
                            break
                        }

                    }
                    setColor(boolean, item, cardView)
//                recAdapter.notifyItemChanged(position)
                }

            })

            recycle.adapter = recAdapter

        }

        binding.searchCard.setOnClickListener {
            findNavController().navigate(R.id.searchFragment)
        }

    }

    @SuppressLint("CheckResult")
    override fun onResume() {
        super.onResume()

        dbHelper.funDb().getAllWriters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                recAdapter.submitList(it)
            }) {
            }
    }

    private fun setColor(boolean: Boolean, item: Writers, cardView: CardView) {
        if (boolean) {
            cardView.setCardBackgroundColor(Color.parseColor("#E5E5E5"))
            strList.remove(item)
            dbHelper.funDb().deleteWriter(item)
        } else {
            cardView.setCardBackgroundColor(Color.parseColor("#00B238"))
            var c = false
            for (s in strList) {
                if (s.uid == item.uid!!) {
                    c = true
                    break
                }
            }
            if (!c) {
                strList.add(item)
            }
            dbHelper.funDb().addWriters(item)
        }
        myShared.setList(gson.toJson(strList), "saved")
    }

    private fun loadPreferences() {
        strList = ArrayList()
        dbHelper.funDb().getAllWriters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                strList.clear()
                strList.addAll(it)
            }
    }
}