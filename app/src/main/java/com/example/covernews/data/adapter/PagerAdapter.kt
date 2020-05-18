package com.example.covernews.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.covernews.ui.main.fragments.*

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CoverFragment()
            }
            1 -> {
                ScienceFragment()
            }
            2 -> {
                SportsFragment()
            }
            3 -> {
                BusinessFragment()
            }
            4 -> {
                EducationFragment()
            }
            5 -> {
                HealthFragment()
            }
            6 -> {
                TravelFragment()
            }
            else -> {
                return TechnologyFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 8
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> {
                "Cover"
            }
            1 -> {
                "Science"
            }
            2 -> {
                "Sports"
            }
            3 -> {
                "Business"
            }
            4 -> {
                "Education"
            }
            5 -> {
                "Health"
            }
            6 -> {
                "Travel"
            }
            else -> {
                return "Technology"
            }
        }
    }
}