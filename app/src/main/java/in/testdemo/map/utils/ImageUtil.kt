package `in`.testdemo.map.utils

import `in`.testdemo.map.R
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap

class ImageUtil {

    fun getResizedBitmap(image: Bitmap, bitmapWidth: Int, bitmapHeight: Int): Bitmap {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true)
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }

        return Bitmap.createScaledBitmap(image, width, height, true)
    }


    fun getThumbImage(context : Context): Bitmap? {
        val bitmap = AppCompatResources.getDrawable(context, R.drawable.ic_image_temp)?.toBitmap()
        return bitmap?.let { Bitmap.createScaledBitmap(it, 400, 400, true) }
    }
}