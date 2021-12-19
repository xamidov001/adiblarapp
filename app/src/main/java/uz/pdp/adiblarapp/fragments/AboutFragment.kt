package uz.pdp.adiblarapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.xeoh.android.texthighlighter.TextHighlighter
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import uz.pdp.adiblarapp.R
import uz.pdp.adiblarapp.room.entity.Writers
import uz.pdp.adiblarapp.databinding.FragmentAboutBinding
import uz.pdp.adiblarapp.room.database.DbHelper

class AboutFragment : Fragment(R.layout.fragment_about) {

    private val binding by viewBinding(FragmentAboutBinding::bind)
    private lateinit var writers: Writers
    private lateinit var list: ArrayList<Writers>
    private lateinit var dbHelper: DbHelper
    private var boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            writers = it.getSerializable("writer") as Writers
        }
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DbHelper.getInstance(requireContext())

        binding.apply {

            list = ArrayList()
            dbHelper.funDb().getAllWriters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    list.clear()
                    list.addAll(it)
                }

            for (it in list) {
                if (it.uid == writers.uid) {
                    Toast.makeText(requireContext(), "have", Toast.LENGTH_SHORT).show()
                    binding.savedCr.setCardBackgroundColor(Color.parseColor("#00B238"))
                    boolean = true
                    break
                }
            }

            binding.savedCr.setOnClickListener {
                if (boolean) {
                    binding.savedCr.setCardBackgroundColor(Color.WHITE)
                    boolean = false
                } else {
                    binding.savedCr.setCardBackgroundColor(Color.parseColor("#00B238"))
                    boolean = true
                }
            }

            serch.setOnClickListener {
                serch.visibility = View.GONE
                savedCr.visibility = View.GONE
                arrowBack.visibility = View.GONE
                searchBtn.visibility = View.VISIBLE
            }

            imageBt.setOnClickListener {
                search1.setText("")
                savedCr.visibility = View.VISIBLE
                arrowBack.visibility = View.VISIBLE
                serch.visibility = View.VISIBLE
                searchBtn.visibility = View.GONE
            }

            search1.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    TextHighlighter().setBackgroundColor(Color.parseColor("#FFFF00"))
                        .addTarget(about)
                        .highlight(s.toString(), TextHighlighter.BASE_MATCHER)
                }

                override fun afterTextChanged(s: Editable?) {

                }

            })

            collapsingToolbar.title =  writers.name
            born.text = writers.life_sicle
            Glide.with(requireActivity()).load(writers.image).into(imageIcn)

            about.text = writers.dict

        }
    }

    override fun onResume() {
        super.onResume()



    }

    override fun onDestroy() {
        super.onDestroy()

        if (boolean) {
            var c = false
            for (it in list) {
                if (it.uid == writers.uid) {
                    c = true
                    break
                }
            }
            if (!c) {
                dbHelper.funDb().addWriters(writers)
            }
        } else {
            dbHelper.funDb().deleteWriter(writers)
        }
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//
//        inflater.inflate(R.menu.my_menu_1, menu)
//
//        val searchView = menu.findItem(R.id.search_menu_1).actionView as SearchView
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String?): Boolean {
//                TextHighlighter().setBackgroundColor(Color.parseColor("#FFFF00"))
//                    .addTarget(binding.about)
//                    .highlight(newText.toString(), TextHighlighter.BASE_MATCHER)
//                return false
//            }
//
//        })
//
//        super.onCreateOptionsMenu(menu, inflater)
//    }
}