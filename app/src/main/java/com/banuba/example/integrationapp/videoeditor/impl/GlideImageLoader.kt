package com.banuba.example.integrationapp.videoeditor.impl

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.banuba.example.integrationapp.R
import com.banuba.sdk.core.domain.ImageLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.signature.ObjectKey

class GlideImageLoader(context: Context? = null, view: View? = null, fragment: Fragment? = null) :
    ImageLoader {

    companion object {
        private const val THUMBNAIL_SIZE_MULTIPLIER = 0.01f
    }

    private val glideRequestManager = when {
        fragment != null -> Glide.with(fragment)
        view != null -> Glide.with(view)
        else -> when (context) {
            is FragmentActivity -> Glide.with(context)
            is Activity -> Glide.with(context)
            else -> Glide.with(context!!)
        }
    }

    override fun loadThumbnail(
        view: ImageView,
        uri: Uri,
        placeholderRes: Int?,
        errorPlaceholderRes: Int?,
        isCircle: Boolean
    ) {
        val requestOptions = RequestOptions().apply {
            if (isCircle) transform(CircleCrop()) else transform(CenterCrop())
            errorPlaceholderRes?.let { error(errorPlaceholderRes) }
            placeholderRes?.let { placeholder(it) }
        }
        glideRequestManager.load(uri).apply(requestOptions).thumbnail(THUMBNAIL_SIZE_MULTIPLIER)
            .into(view)
    }

    override fun loadImage(
        view: ImageView,
        uri: Uri,
        placeholderRes: Int?,
        errorPlaceholderRes: Int?,
        isCircle: Boolean,
        cornerRadiusPx: Int
    ) {
        val requestOptions = RequestOptions().apply {
            when {
                isCircle -> {
                    transform(CenterCrop(), CircleCrop())
                }
                cornerRadiusPx > 0 -> {
                    transform(CenterCrop(), RoundedCorners(cornerRadiusPx))
                }
                else -> {
                    transform(CenterCrop())
                }
            }
            errorPlaceholderRes?.let { error(errorPlaceholderRes) }
            placeholderRes?.let { placeholder(it) }
        }
        glideRequestManager.load(uri).apply(requestOptions).into(view)
    }

    override fun loadSticker(view: ImageView, uri: Uri, isHiRes: Boolean, width: Int, height: Int) {
        glideRequestManager.load(uri)
            .apply(RequestOptions().signature(ObjectKey(isHiRes)).override(width, height))
            .into(view)
    }

    override fun loadGif(view: ImageView, uri: Uri, onResourceReady: (Drawable) -> Unit) {
        val glideRequestOptions = RequestOptions().placeholder(R.drawable.ic_gif_placeholder)

        val glideTransitionOptions = DrawableTransitionOptions.withCrossFade(
            DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        )

        glideRequestManager.load(uri).apply(glideRequestOptions).transition(glideTransitionOptions)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    if (resource is GifDrawable) {
                        onResourceReady.invoke(resource)
                    }
                    return false
                }
            }).into(view)
    }
}