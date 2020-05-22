package com.shunsukeshoji.albumapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.shunsukeshoji.albumapp.fragment.PreviewFragment

class ImagePagerAdapter(fragment: Fragment, private val urlList: List<String>) :
    FragmentStatePagerAdapter(
        fragment.childFragmentManager,
        BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
    ) {

    override fun getCount(): Int = urlList.size

    override fun getItem(position: Int): Fragment =
        PreviewFragment.newInstance(imageUrl = urlList[position])
}