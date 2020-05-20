package com.shunsukeshoji.albumapp.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionSet
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.shunsukeshoji.albumapp.ImageData
import com.shunsukeshoji.albumapp.MainActivity
import com.shunsukeshoji.albumapp.R
import com.shunsukeshoji.albumapp.fragment.ImagePagerFragment
import kotlinx.android.synthetic.main.item_image.view.*
import java.util.concurrent.atomic.AtomicBoolean

class GridAdapter(fragment: Fragment) : RecyclerView.Adapter<GridAdapter.ImageViewHolder>() {
    private val requestManager: RequestManager = Glide.with(fragment)
    private val viewHolderListener: ViewHolderListener = ViewHolderListenerImpl(fragment)

    override fun getItemCount(): Int = ImageData.IMAGE_DRAWABLES.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view, requestManager, viewHolderListener)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.onBind()
    }


    interface ViewHolderListener {
        fun onLoadCompleted(view: ImageView?, adapterPosition: Int)
        fun onItemClicked(view: View?, adapterPosition: Int)
    }

    class ViewHolderListenerImpl(private val fragment: Fragment) : ViewHolderListener {
        private val enterTransitionStarted = AtomicBoolean()

        override fun onItemClicked(view: View?, adapterPosition: Int) {
            MainActivity.currentPosition = adapterPosition
            val transitionSet = fragment.exitTransition as TransitionSet
            transitionSet.excludeTarget(view ?: return, true)

            fragment.requireFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .addSharedElement(view.image_view, view.image_view.transitionName)
                .replace(
                    R.id.fragment_container,
                    ImagePagerFragment(),
                    ImagePagerFragment::class.simpleName
                )
                .addToBackStack(null)
                .commit()
        }

        override fun onLoadCompleted(view: ImageView?, adapterPosition: Int) {
            if (MainActivity.currentPosition != adapterPosition) {
                return
            }
            if (enterTransitionStarted.getAndSet(true)) {
                return
            }
            fragment.startPostponedEnterTransition()
        }
    }


    class ImageViewHolder(
        private val view: View,
        private val requestManager: RequestManager,
        private val viewHolderListener: ViewHolderListener
    ) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        fun onBind() {
            setImage(adapterPosition)
            view.image_view.transitionName = ImageData.IMAGE_DRAWABLES[adapterPosition].toString()
        }

        private fun setImage(adapterPosition: Int) {
            requestManager.load(ImageData.IMAGE_DRAWABLES[adapterPosition])
                .listener(object : RequestListener<Drawable> {
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(view.image_view, adapterPosition)
                        return false
                    }

                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        viewHolderListener.onLoadCompleted(view.image_view, adapterPosition)
                        return false
                    }
                })
                .into(view.image_view)
        }

        override fun onClick(v: View?) {
            viewHolderListener.onItemClicked(view, adapterPosition)
        }
    }
}