package com.shunsukeshoji.albumapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.shunsukeshoji.albumapp.ImageData
import com.shunsukeshoji.albumapp.fragment.PreviewFragment

class ImagePagerAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int = ImageData.IMAGE_DRAWABLES.size

    override fun getItem(position: Int): Fragment =
        PreviewFragment.newInstance(ImageData.IMAGE_DRAWABLES[position])
}