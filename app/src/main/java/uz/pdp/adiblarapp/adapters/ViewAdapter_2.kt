package uz.pdp.adiblarapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.pdp.adiblarapp.fragments.SavedFragment
import uz.pdp.adiblarapp.fragments.ServiceFragment
import uz.pdp.adiblarapp.fragments.TypeFragment
import uz.pdp.adiblarapp.fragments.WritersFragment

class ViewAdapter_2(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return TypeFragment.newInstance(position)
    }
}