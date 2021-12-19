package uz.pdp.adiblarapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.pdp.adiblarapp.R
import uz.pdp.adiblarapp.adapters.RecAdapter
import uz.pdp.adiblarapp.adapters.ViewAdapter_2
import uz.pdp.adiblarapp.databinding.FragmentWritersBinding

class WritersFragment : Fragment(R.layout.fragment_writers) {

    private val binding by viewBinding(FragmentWritersBinding::bind)
    private lateinit var viewadapter2: ViewAdapter_2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewadapter2 = ViewAdapter_2(requireActivity())

        binding.apply {

            viewpager2.adapter = viewadapter2

            cardSearch.setOnClickListener {
                findNavController().navigate(R.id.searchFragment)
            }

            TabLayoutMediator(tablayout, viewpager2
            ) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Mumtoz adabiyoti"
                    }
                    1 -> {
                        tab.text = "O`zbek adabiyoti"
                    }
                    else -> {
                        tab.text = "Jahon adabiyoti"
                    }
                }
            }.attach()


        }
    }
}