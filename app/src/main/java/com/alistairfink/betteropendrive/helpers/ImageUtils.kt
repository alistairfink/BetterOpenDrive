package com.alistairfink.betteropendrive.helpers

import android.graphics.*
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight


class ImageUtils
{
    companion object {
        fun getCircularBitmap(bitmap: Bitmap): Bitmap {
            val output: Bitmap

            if (bitmap.width > bitmap.height) {
                output = Bitmap.createBitmap(bitmap.height, bitmap.height, Bitmap.Config.ARGB_8888)
            } else {
                output = Bitmap.createBitmap(bitmap.width, bitmap.width, Bitmap.Config.ARGB_8888)
            }

            val canvas = Canvas(output)

            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)

            var r = 0f

            if (bitmap.width > bitmap.height) {
                r = (bitmap.height / 2).toFloat()
            } else {
                r = (bitmap.width / 2).toFloat()
            }

            paint.setAntiAlias(true)
            canvas.drawARGB(0, 0, 0, 0)
            paint.setColor(color)
            canvas.drawCircle(r, r, r, paint)
            paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }
    }
}