package com.dimadyuk.smackchat.controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
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
import com.dimadyuk.smackchat.services.AuthService
import com.dimadyuk.smackchat.services.UserDataService
import com.dimadyuk.smackchat.utilities.Constants.BROADCAST_USER_DATA_CHANGE
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            with(binding.navDrawerHeaderInclude) {
                if (AuthService.isLoggedIn) {
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
                } else {
                    loginButtonNavHeader.text = "Login"
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                userDataChangeReceiver,
                IntentFilter(BROADCAST_USER_DATA_CHANGE)
            )
        with(binding.navDrawerHeaderInclude) {
            loginButtonNavHeader.setOnClickListener {
                if (AuthService.isLoggedIn) {
                    UserDataService.logout()
                    loginButtonNavHeader.text = "Login"
                    userNameNavHeader.text = "Login"
                    userEmailNavHeader.text = ""
                    userImageNavHeader.setImageResource(R.drawable.profiledefault)
                    userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
                } else {
                    navController.navigate(R.id.nav_login)
                }
                drawerLayout.close()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
