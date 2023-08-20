package uz.lazydevv.unicaltask.ui.main.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.lazydevv.unicaltask.ui.apps.AppsFragment

class VpAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return AppsFragment.newInstance(position)
    }
}