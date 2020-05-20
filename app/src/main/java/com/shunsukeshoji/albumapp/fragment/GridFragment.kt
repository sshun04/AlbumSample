package com.shunsukeshoji.albumapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.shunsukeshoji.albumapp.MainActivity
import com.shunsukeshoji.albumapp.R
import com.shunsukeshoji.albumapp.adapter.GridAdapter
import kotlinx.android.synthetic.main.item_image.view.*

/* Created by shojishunsuke on 2020/05/21
 * Copyright © 2020 Shunsuke Shoji. All rights reserved.
 * RecyclerViewを保持するためだけのFragment
 */

class GridFragment : Fragment() {

    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        recyclerView = inflater.inflate(R.layout.fragment_grid, container, false) as RecyclerView
        recyclerView.adapter = GridAdapter(this)

        prepareTransitions()
        postponeEnterTransition()

        return recyclerView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scrollToPosition()
    }

    //    遷移先で表示されていた画像の位置までスクロールする処理
    private fun scrollToPosition() {
        recyclerView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                v: View?,
                left: Int,
                top: Int,
                right: Int,
                bottom: Int,
                oldLeft: Int,
                oldTop: Int,
                oldRight: Int,
                oldBottom: Int
            ) {
                recyclerView.removeOnLayoutChangeListener(this)
                val layoutManager = recyclerView.layoutManager
                val viewAtPosition =
                    layoutManager?.findViewByPosition(MainActivity.currentPosition) ?: return
                if (layoutManager
                        .isViewPartiallyVisible(viewAtPosition, false, true)
                ) {
                    recyclerView.post { layoutManager.scrollToPosition(MainActivity.currentPosition) }
                }

            }
        })
    }


    private fun prepareTransitions() {
        exitTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.grid_exit_transition)

        setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(
                names: MutableList<String>?,
                sharedElements: MutableMap<String, View>?
            ) {
                val selectedViewHolder =
                    recyclerView.findViewHolderForAdapterPosition(MainActivity.currentPosition)
                        ?: return
                selectedViewHolder.let {
                    sharedElements?.put(names?.get(0) ?: return, it.itemView.card_image)
                }
            }
        })

    }

}