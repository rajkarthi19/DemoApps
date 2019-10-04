package `in`.testdemo.map.utils

import `in`.testdemo.map.R
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat


/**
 * Created by karthi-2322 on 03,December,2018
 */
class DisplayUtils {

    fun showToast(context: Context, message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        val view = toast.view
        view.background.setColorFilter(
            ContextCompat.getColor(context, R.color.colorAccent),
            PorterDuff.Mode.SRC_IN
        )
        val text = view.findViewById<TextView>(android.R.id.message)
        text.setTextColor(Color.WHITE)
        toast.show()
    }


}