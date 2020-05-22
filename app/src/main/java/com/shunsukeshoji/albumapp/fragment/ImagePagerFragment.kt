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

class ImagePagerFragment : Fragment() {
    private lateinit var viewPager: ViewPager

    companion object {
        private const val KEY_IMAGE_URL_LIST = "key_image_url_list"
        fun newInstance(urlList: List<String>): ImagePagerFragment {
            val fragment = ImagePagerFragment()
            val argument = Bundle().apply {
                putStringArray(KEY_IMAGE_URL_LIST, urlList.toTypedArray())
            }
            fragment.arguments = argument
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val imageUrlList =
            arguments?.getStringArray(KEY_IMAGE_URL_LIST)
                ?: throw KotlinNullPointerException("could not get argument")

        viewPager = inflater.inflate(R.layout.fragment_pager_image, container, false) as ViewPager
        viewPager.adapter = ImagePagerAdapter(this, imageUrlList.toList())

        viewPager.currentItem = MainActivity.currentPosition
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
                    sharedElements?.put(
                        names?.get(0) ?: throw IndexOutOfBoundsException(),
                        view.image
                    )
                }
            }
        })
    }

}