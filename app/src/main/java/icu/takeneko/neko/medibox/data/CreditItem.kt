package icu.takeneko.neko.medibox.data

import android.content.Context
import com.google.gson.GsonBuilder

open class CreditItem(
    open val name: String,
    open val participatePart: String,
    open val link: String
) {
    companion object {
        fun readCredits(context: Context): List<CreditItem> {
            val jsonContent = context.assets.open("credits.json").readBytes().decodeToString()
            val gson = GsonBuilder().serializeNulls().create()
            val items = gson.fromJson(jsonContent, Array<CreditItem>::class.java)
            return items.toList()
        }
    }
}
