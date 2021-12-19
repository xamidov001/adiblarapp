package uz.pdp.adiblarapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.pdp.adiblarapp.R
import uz.pdp.adiblarapp.adapters.RecAdapterSearch
import uz.pdp.adiblarapp.room.entity.Writers
import uz.pdp.adiblarapp.databinding.FragmentSearchBinding
import uz.pdp.adiblarapp.room.database.DbHelper
import uz.pdp.telegramm.preference.MyShared

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var recAdapter: RecAdapterSearch
    private lateinit var list: ArrayList<Writers>
    private lateinit var list1: ArrayList<Writers>
    private var myShared = MyShared
    private lateinit var strList: ArrayList<Writers>
    private lateinit var dbHelper: DbHelper
    private val gson = Gson()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myShared = MyShared.getInstance(requireContext())
        dbHelper = DbHelper.getInstance(requireContext())
        loadPreferences()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference("writers")

        list = ArrayList()
        list1 = ArrayList()
        recAdapter = RecAdapterSearch(requireContext(), list, strList, object : RecAdapterSearch.OnMyClickedListener{
            override fun onItemClicked(uid: String) {

            }

            override fun onSavedClicked(writers: Writers, cardView: CardView, position: Int) {

            }

        })
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                val children = snapshot.children
                for (child in children) {
                    val value = child.getValue(Writers::class.java)
                        if (value != null) {
                            list.add(value)
                        }
                }
                list1.addAll(list)
                recAdapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        binding.apply {

            recycle.adapter = recAdapter

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    list.clear()

                    if (newText!!.isEmpty()) {
                        list.addAll(list1)
                    } else {
                        for (passport in list1) {
                            if (passport.name!!.contains(newText)) {
                                list.add(passport)
                            }
                        }
                    }
                    recAdapter.submitList(list)
                    recAdapter.notifyDataSetChanged()
                    return false
                }

            })

        }
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