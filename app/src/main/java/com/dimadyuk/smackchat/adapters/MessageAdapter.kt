package com.dimadyuk.smackchat.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dimadyuk.smackchat.R
import com.dimadyuk.smackchat.model.Message
import com.dimadyuk.smackchat.services.UserDataService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MessageAdapter(val context: Context, val messages: ArrayList<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater
            .from(context)
            .inflate(R.layout.message_list_view, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bindMessage(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage = itemView.findViewById<ImageView>(R.id.messageUserImage)
        val timeStamp = itemView.findViewById<TextView>(R.id.timeText)
        val userName = itemView.findViewById<TextView>(R.id.messageUserName)
        val messageBody = itemView.findViewById<TextView>(R.id.messageText)

        fun bindMessage(message: Message) {
            val resourcesId = context.resources.getIdentifier(
                message.userAvatar, "drawable", context.packageName
            )
            userImage.setImageResource(resourcesId)
            userImage.setBackgroundColor(UserDataService.returnAvatarColor(message.userAvatarColor))
            timeStamp.text = returnDateString(message.timeStamp)
            userName.text = message.userName
            messageBody.text = message.message
        }

        fun returnDateString(isoString: String): String {
            val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormatter.timeZone = TimeZone.getTimeZone("UTC")
            var convertedDate = Date()
            try {
                convertedDate = isoFormatter.parse(isoString)
            } catch (e: ParseException) {
                Log.d("PARSE", "Cannot parse date")
            }
            val outDateString = SimpleDateFormat("E, h:mm a", Locale.getDefault())
            return outDateString.format(convertedDate)
        }
    }
}
