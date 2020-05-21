package com.shunsukeshoji.albumapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import androidx.viewpager.widget.ViewPager
import com.shunsukeshoji.albumapp.MainActivity
import com.shunsukeshoji.albumapp.R
import com.shunsukeshoji.albumapp.adapter.ImagePagerAdapter
import kotlinx.android.synthetic.main.fragment_preview.view.*
import java.lang.Exception
import java.lang.IndexOutOfBoundsException

class ImagePagerFragment : Fragment() {
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewPager = inflater.inflate(R.layout.fragment_pager_image, container, false) as ViewPager
        viewPager.adapter = ImagePagerAdapter(this)

        viewPager.currentItem = MainActivity.currentPosition ?: 0
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                MainActivity.currentPosition = position
            }
        })

        prepareSharedElementTransition()

        if (savedInstanceState == null) {
            postponeEnterTransition()
        }

        return viewPager
    }

    private fun prepareSharedElementTransition() {
        val transition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.image_shared_element_transition)
        sharedElementEnterTransition = transition

        setEnterSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val currentFragment =
                    viewPager.adapter?.instantiateItem(
                        viewPager,
                        MainActivity.currentPosition
                    ) as? Fragment? ?: throw Exception()

                currentFragment.view?.let { view ->
                    sharedElements?.put(names?.get(0) ?: throw IndexOutOfBoundsException(), view.image)
                }
            }
        })
    }

}