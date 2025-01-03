package com.dimadyuk.smackchat.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dimadyuk.smackchat.R
import com.dimadyuk.smackchat.databinding.ActivityMainBinding
import com.dimadyuk.smackchat.model.Channel
import com.dimadyuk.smackchat.model.Message
import com.dimadyuk.smackchat.services.AuthService
import com.dimadyuk.smackchat.services.MessageService
import com.dimadyuk.smackchat.services.UserDataService
import com.dimadyuk.smackchat.utilities.Constants.BROADCAST_USER_DATA_CHANGE
import com.dimadyuk.smackchat.utilities.hideKeyboard
import com.google.android.material.navigation.NavigationView
import io.socket.emitter.Emitter

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var channelAdapter: ArrayAdapter<Channel>

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            with(binding.navDrawerHeaderInclude) {
                if (App.prefs.isLoggedIn) {
                    loginButtonNavHeader.text = "Logout"
                    userNameNavHeader.text = UserDataService.name
                    userEmailNavHeader.text = UserDataService.email
                    userImageNavHeader
                        .setImageResource(
                            resources.getIdentifier(
                                UserDataService.avatarName,
                                "drawable", packageName
                            )
                        )
                    userImageNavHeader.setBackgroundColor(
                        UserDataService.returnAvatarColor(
                            UserDataService.avatarColor
                        )
                    )
                    MessageService.getChannels { complete ->
                        if (complete) {
                            if (MessageService.channels.isNotEmpty()) {
                                MessageService.selectedChannel = MessageService.channels[0]
                                channelAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                } else {
                    loginButtonNavHeader.text = "Login"
                }
            }
        }
    }

    private fun setupAdapters() {
        channelAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            MessageService.channels
        )
        binding.channelList.adapter = channelAdapter
        binding.channelList.setOnItemClickListener { parent, view, position, id ->
            MessageService.selectedChannel = MessageService.channels[position]
            binding.drawerLayout.close()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.socket.connect()
        App.socket.on("channelCreated", onNewChannel)
        App.socket.on("messageCreated", onNewMessage)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_login, R.id.nav_create_user
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        setupAdapters()
        if (App.prefs.isLoggedIn) {
            AuthService.findUserByEmail(this) {}
        }
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                userDataChangeReceiver,
                IntentFilter(BROADCAST_USER_DATA_CHANGE)
            )
        with(binding.navDrawerHeaderInclude) {
            loginButtonNavHeader.setOnClickListener {
                if (App.prefs.isLoggedIn) {
                    UserDataService.logout()
                    loginButtonNavHeader.text = "Login"
                    userNameNavHeader.text = ""
                    userEmailNavHeader.text = ""
                    userImageNavHeader.setImageResource(R.drawable.profiledefault)
                    userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    navController.navigate(R.id.nav_login)
                }
                drawerLayout.close()
            }
            addChanelButton.setOnClickListener {
                if (App.prefs.isLoggedIn) {
                    val builder = AlertDialog.Builder(this@MainActivity)
                    val dialogView = layoutInflater.inflate(R.layout.add_channel_dialog, null)

                    builder.setView(dialogView)
                        .setPositiveButton("Add") { dialog, which ->
                            val nameTextField =
                                dialogView.findViewById<EditText>(R.id.addChannelNameText)
                            val descTextField =
                                dialogView.findViewById<EditText>(R.id.addChannelDescriptionText)
                            val channelName = nameTextField.text.toString()
                            val channelDesc = descTextField.text.toString()
                            if (channelName.isNotEmpty() && channelDesc.isNotEmpty()) {
                                App.socket.emit("newChannel", channelName, channelDesc)
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Please fill in both fields",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                        }
                        .setNegativeButton("Cancel") { dialog, which -> hideKeyboard() }
                        .show()
                }
                drawerLayout.close()
            }
        }
    }

    private val onNewChannel = Emitter.Listener { args ->
        if (App.prefs.isLoggedIn) {
            runOnUiThread {
                val name = args[0] as String
                val desc = args[1] as String
                val id = args[2] as String
                MessageService.channels.add(Channel(name, desc, id))
                channelAdapter.notifyDataSetChanged()
            }
        }
    }
    private val onNewMessage = Emitter.Listener { args ->
        if (App.prefs.isLoggedIn) {
            runOnUiThread {
                val msgBody = args[0] as String
                val channelId = args[2] as String
                if (channelId == MessageService.selectedChannel?.id) {
                    val userName = args[3] as String
                    val userAvatar = args[4] as String
                    val userAvatarColor = args[5] as String
                    val id = args[6] as String
                    val timeStamp = args[7] as String
                    val newMessage = Message(
                        msgBody,
                        userName,
                        channelId,
                        userAvatar,
                        userAvatarColor,
                        id,
                        timeStamp
                    )
                    MessageService.messages.add(newMessage)
                }
            }
        }
    }

    override fun onDestroy() {
        App.socket.disconnect()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(userDataChangeReceiver)
        super.onDestroy()
    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
