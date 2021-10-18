package com.nikola.covernews.data.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.nikola.covernews.ui.main.fragments.*

const val CATEGORY = "CATEGORY_POSITION"

class PagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(
    fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                CoverFragment()
            }
            else -> {
                val bundle = Bundle()
                bundle.putInt(CATEGORY, position)
                BusinessFragment.newInstance(bundle)
            }
        }
    }

    override fun getCount(): Int {
        return 8
    }

    override fun getPageTitle(position: Int): CharSequence {
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