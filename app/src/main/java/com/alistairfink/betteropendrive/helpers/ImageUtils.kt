package com.alistairfink.betteropendrive.helpers

import android.graphics.*
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight


class ImageUtils
{
    companion object {
        fun getCircularBitmap(bitmap: Bitmap): Bitmap {
            val output: Bitmap = if (bitmap.width > bitmap.height) {
                Bitmap.createBitmap(bitmap.height, bitmap.height, Bitmap.Config.ARGB_8888)
            } else {
                Bitmap.createBitmap(bitmap.width, bitmap.width, Bitmap.Config.ARGB_8888)
            }

            val canvas = Canvas(output)

            val color = -0xbdbdbe
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width, bitmap.height)

            var r: Float = if (bitmap.width > bitmap.height) {
                (bitmap.height / 2).toFloat()
            } else {
                (bitmap.width / 2).toFloat()
            }

            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = color
            canvas.drawCircle(r, r, r, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(bitmap, rect, rect, paint)
            return output
        }
    }
}