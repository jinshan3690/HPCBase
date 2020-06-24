package com.hpcang.base.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

class SimplePagerAdapter(
    fm: FragmentManager, var frags: List<Fragment>, var tabTitle: Array<String>
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return frags[position]
    }

    override fun getCount(): Int {
        return frags.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitle[position]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    companion object {

        @JvmStatic
        fun getTag(viewId: Int, position: Long): String {
            return "android:switcher:$viewId:$position"
        }
    }

}