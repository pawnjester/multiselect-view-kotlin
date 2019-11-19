package com.andela.mulitselect

import android.content.Context
import java.util.*
import android.view.WindowManager
import android.app.Activity




class Util {

    companion object {
        fun getInboxData(ctx: Context): List<Inbox> {
            val items = mutableListOf<Inbox>()
            val drw_arr = ctx.resources.getStringArray(R.array.people_images)
            val name_arr = ctx.getResources().getStringArray(R.array.people_names)
            val date_arr = ctx.getResources().getStringArray(R.array.general_date)

            for (i in 0 until drw_arr.size) {
                val obj = Inbox(from = name_arr[i],
                    email = "abc@gmail.com",
                    message = ctx.resources.getString(R.string.lorem_ipsum),
                    date = date_arr[randInt(date_arr.size - 1)] )
                items.add(obj)
            }
            Collections.shuffle(items)
            return items
        }

        fun toggleStatusBarColor(activity: Activity, color: Int) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = activity.resources.getColor(R.color.colorPrimary)
        }

    }

}

fun randInt(max: Int): Int {
    val r = Random()
    val min = 0
    return r.nextInt(max - min + 1) + min
}