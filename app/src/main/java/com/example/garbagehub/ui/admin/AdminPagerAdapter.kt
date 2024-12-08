package com.example.garbagehub.ui.admin

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.garbagehub.ui.admin.bins.BinManagementFragment
import com.example.garbagehub.ui.admin.maintenance.MaintenanceTasksFragment
import com.example.garbagehub.ui.admin.reports.AdminReportsFragment
import com.example.garbagehub.ui.admin.users.UserManagementFragment

class AdminPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BinManagementFragment()
            1 -> MaintenanceTasksFragment()
            2 -> UserManagementFragment()
            3 -> AdminReportsFragment()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
    }
}
