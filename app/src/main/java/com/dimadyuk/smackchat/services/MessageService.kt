package com.dimadyuk.smackchat.services

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.dimadyuk.smackchat.controller.App
import com.dimadyuk.smackchat.model.Channel
import com.dimadyuk.smackchat.model.Message
import com.dimadyuk.smackchat.utilities.Constants.URL_GET_CHANNELS
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()
    val messages = ArrayList<Message>()
    var selectedChannel: Channel? = null
        set(value) {
            field = value
            selectedChannelLiveData.postValue(value)
        }
    val selectedChannelLiveData = MutableLiveData<Channel?>()

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelRequest = object : JsonArrayRequest(
            Method.GET,
            URL_GET_CHANNELS,
            null,
            Response.Listener { response ->
                try {
                    channels.clear()
                    for (x in 0 until response.length()) {
                        val channel = response.getJSONObject(x)
                        val name = channel.getString("name")
                        val description = channel.getString("description")
                        val id = channel.getString("_id")

                        val newChannel = Channel(name, description, id)
                        this.channels.add(newChannel)
                    }
                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC: ${e.localizedMessage}")
                    complete(false)
                }
            },
            Response.ErrorListener { error ->
                Log.d("ERROR", "Could not retrieve channels: $error")
                complete(false)
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer ${App.prefs.authToken}"
                return headers
            }
        }
        App.prefs.requestQueue.add(channelRequest)
    }
}
