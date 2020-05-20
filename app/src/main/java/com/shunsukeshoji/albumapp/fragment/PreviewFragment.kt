package com.shunsukeshoji.albumapp.fragment

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.shunsukeshoji.albumapp.R
import kotlinx.android.synthetic.main.fragment_preview.view.*

class PreviewFragment : Fragment() {

    companion object {
        private const val KEY_IMAGE_RES = "key_image_res"

        fun newInstance(imageRes: Int): PreviewFragment {
            val fragment = PreviewFragment()
            val argument = Bundle().apply {
                putInt(KEY_IMAGE_RES, imageRes)
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
        val view = inflater.inflate(R.layout.fragment_preview, container, false)

        val imageRes = arguments?.getInt(KEY_IMAGE_RES) ?: 0
        view.image_view.transitionName = imageRes.toString()

        Glide.with(this)
            .load(imageRes)
            .listener(object : RequestListener<Drawable> {
                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    requireParentFragment().startPostponedEnterTransition()
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    requireParentFragment().startPostponedEnterTransition()
                    return false
                }
            })
            .into(view.image_view)
        return view
    }
}