package uz.pdp.adiblarapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.pdp.adiblarapp.R
import uz.pdp.adiblarapp.adapters.ViewAdapter
import uz.pdp.adiblarapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var viewAdapter: ViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewAdapter = ViewAdapter(requireActivity())

        binding.apply {

            viewpager.adapter = viewAdapter

            viewpager.isUserInputEnabled = false

            smoothBottomBar.setOnItemSelectedListener {
                viewpager.currentItem = it
            }

            viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    smoothBottomBar.itemActiveIndex = position
                }

            })

        }
    }
}