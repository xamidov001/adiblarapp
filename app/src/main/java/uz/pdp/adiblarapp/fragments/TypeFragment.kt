package uz.pdp.adiblarapp.fragments

import android.graphics.Color
import android.os.Bundle
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
import uz.pdp.adiblarapp.room.entity.Writers
import uz.pdp.adiblarapp.databinding.FragmentTypeBinding
import uz.pdp.adiblarapp.room.database.DbHelper
import uz.pdp.telegramm.preference.MyShared


class TypeFragment : Fragment(R.layout.fragment_type) {

    private val binding by viewBinding(FragmentTypeBinding::bind)
    private lateinit var recAdapter: RecAdapter
    private var n = -1
    private var myShared = MyShared
    private val gson = Gson()
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var list: ArrayList<Writers>
    private lateinit var strList: ArrayList<Writers>
    private lateinit var dbHelper: DbHelper
    private var str = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            n = it.getInt("int")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("writers")
        myShared = MyShared.getInstance(requireContext())
        dbHelper = DbHelper.getInstance(requireContext())
        loadType(n)
        binding.apply {

            list = ArrayList()
        }

    }

    override fun onResume() {
        super.onResume()
        strList = ArrayList()
        dbHelper.funDb().getAllWriters()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{
                strList.clear()
                strList.addAll(it)
            }
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(Writers::class.java)
                    if (value?.type == str) {
                        list.add(value)
                    }
                }
                binding.spinKit.visibility = View.GONE
                recAdapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        recAdapter = RecAdapter(requireContext(), list, strList, object : RecAdapter.OnMyClickedListener{
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
//                    recAdapter.notifyItemChanged(position)
            }

        })
        binding.recycle.adapter = recAdapter
    }

    private fun setColor(boolean: Boolean, item: Writers, cardView: CardView) {
        if (boolean) {
            cardView.setCardBackgroundColor(Color.parseColor("#E5E5E5"))
            dbHelper.funDb().deleteWriter(item)
            strList.remove(item)
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
                dbHelper.funDb().addWriters(item)
            }
        }
    }

    private fun loadType(n: Int) {
        if (n == 0) str = "Mumtoz adabiyoti"
        else if (n == 1) str = "O`zbek adabiyoti"
        else str = "Jahon adabiyoti"
    }

    private fun loadPreferences() {
        
    }

    companion object {

        fun newInstance(param1: Int) =
            TypeFragment().apply {
                arguments = Bundle().apply {
                    putInt("int", param1)
                }
            }
    }
}