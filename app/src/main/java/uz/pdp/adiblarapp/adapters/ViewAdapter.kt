package uz.pdp.adiblarapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.pdp.adiblarapp.fragments.SavedFragment
import uz.pdp.adiblarapp.fragments.ServiceFragment
import uz.pdp.adiblarapp.fragments.WritersFragment

class ViewAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> WritersFragment()
            1 -> SavedFragment()
            2 -> ServiceFragment()
            else -> WritersFragment()
        }
    }
}