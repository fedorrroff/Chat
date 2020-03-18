package com.example.myapplication.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import androidx.core.content.res.ResourcesCompat
import com.example.myapplication.R
import kotlin.math.abs

object AvatarCreator {

    fun generateCircleBitmap(
        context: Context,
        diameterDP: Float,
        text: String?
    ): Bitmap {
        val textColor = -0x1

        val metrics = Resources.getSystem().displayMetrics
        val diameterPixels = diameterDP * (metrics.densityDpi / 160f)
        val radiusPixels = diameterPixels / 2
        val circleColor = getMaterialColor(text)

        // Create the bitmap
        val output = Bitmap.createBitmap(
            diameterPixels.toInt(), diameterPixels.toInt(),
            Bitmap.Config.ARGB_8888
        )

        val textSizeMultiplier = if(text?.length != 2) {
            1.5f
        } else {
            1f
        }

        // Create the canvas to draw on
        val canvas = Canvas(output)
        canvas.drawARGB(0, 0, 0, 0)

        // Draw the circle
        val paintC = Paint()
        paintC.isAntiAlias = true
        paintC.color = circleColor
        canvas.drawCircle(radiusPixels, radiusPixels, radiusPixels, paintC)

        // Draw the text
        if (text != null && text.isNotEmpty()) {
            val paintT = Paint()
            paintT.color = textColor
            paintT.isAntiAlias = true
            paintT.textSize = radiusPixels * textSizeMultiplier
            val typeFace = ResourcesCompat.getFont(context, R.font.roboto_bold)
            paintT.typeface = typeFace
            val textBounds = Rect()
            paintT.getTextBounds(text, 0, text.length, textBounds)
            canvas.drawText(
                text,
                radiusPixels - textBounds.exactCenterX(),
                radiusPixels - textBounds.exactCenterY(),
                paintT
            )
        }

        return output
    }

    private val materialColors = listOf(
        0xffe57373.toInt(),
        0xfff06292.toInt(),
        0xffba68c8.toInt(),
        0xff9575cd.toInt(),
        0xff7986cb.toInt(),
        0xff64b5f6.toInt(),
        0xff4fc3f7.toInt(),
        0xff4dd0e1.toInt(),
        0xff4db6ac.toInt(),
        0xff81c784.toInt(),
        0xffaed581.toInt(),
        0xffff8a65.toInt(),
        0xffd4e157.toInt(),
        0xffffd54f.toInt(),
        0xffffb74d.toInt(),
        0xffa1887f.toInt(),
        0xff90a4ae.toInt()
    )

    //Так сложно для того чтобы при каждой новой генерации аватара мы получали один и тот же цвет
    private fun getMaterialColor(key: String?): Int = materialColors[abs(key.hashCode()) % materialColors.size]
}