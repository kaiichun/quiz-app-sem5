package com.alvin.quiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alvin.quiz.core.di.utils.UserRole
import com.alvin.quiz.core.service.AuthService
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authService: AuthService
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
        setupToolbar()
        setupDestinationVisibility()
        setupLogout()
        lifecycleScope.launch {
            showLoading()
            navigateBasedOnUserRole()
            hideLoading()
        }
    }
    fun checkRoleToGetNavView(role: UserRole) {
        val navView = findViewById<NavigationView>(R.id.navigationView)
        val menu = navView.menu

        when (role) {
            UserRole.STUDENT -> {
                menu.findItem(R.id.teacherHomeFragment).isVisible = false
                menu.findItem(R.id.studentHomeFragment).isVisible = true
                menu.findItem(R.id.teacherDashboardFragment).isVisible = false
                navController.navigate(
                    R.id.studentHomeFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, true)
                        .build()
                )
            }
            UserRole.TEACHER -> {
                menu.findItem(R.id.studentHomeFragment).isVisible = false
                menu.findItem(R.id.teacherHomeFragment).isVisible = true
                menu.findItem(R.id.teacherDashboardFragment).isVisible = true
                navController.navigate(
                    R.id.teacherHomeFragment,
                    null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, true)
                        .build()
                )
            }
        }
    }

    private fun showLoading() {
        findViewById<View>(R.id.loading_overlay_login)?.isVisible = true
        findViewById<TextView>(R.id.tvLoginLoadingText)?.text = getString(R.string.authentication)
    }

    private fun hideLoading() {
        findViewById<View>(R.id.loading_overlay_login)?.isVisible = false
    }

    private fun setupNavigation() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        navController = findNavController(R.id.navHostFragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.studentHomeFragment, R.id.teacherHomeFragment, R.id.teacherDashboardFragment, R.id.profileFragment), drawerLayout
        )

        val navView = findViewById<NavigationView>(R.id.navigationView)
        navView.setupWithNavController(navController)
        navView.menu.clear()
        navView.inflateMenu(R.menu.drawer_menu)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }


    private fun setupDestinationVisibility() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            toolbar.isVisible = destination.id != R.id.loginFragment && destination.id != R.id.registerFragment
        }
    }

    private fun setupLogout() {
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navView = findViewById<NavigationView>(R.id.navigationView)
        navView.apply {
            menu.findItem(R.id.logout).setOnMenuItemClickListener {
            showLogoutConfirmationDialog()
            drawerLayout.close()
            true
        }
        }
    }

    private fun showLogoutConfirmationDialog() {
        val alertView = layoutInflater.inflate(R.layout.layout_alert_logout, null)
        val alertDialog = AlertDialog.Builder(this).setView(alertView).create()
        val btnCancel = alertView.findViewById<Button>(R.id.btnCancel)
        val btnLogout = alertView.findViewById<Button>(R.id.btnLogout)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnLogout.setOnClickListener {
            val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
            val uid = authService.getUid()
            if (uid != null) {
                authService.logout()
                drawerLayout.close()
            }
            navController.navigate(
                R.id.loginFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.loginFragment, true)
                    .build()
            )
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
    private fun navigateBasedOnUserRole() {
        val uid = authService.getUid()

        if (uid != null) {
            lifecycleScope.launch {
                showLoading()
                val userRole = authService.getUserRole(uid)
                val navView = findViewById<NavigationView>(R.id.navigationView)
                val menu = navView.menu

                when (userRole) {
                    UserRole.STUDENT -> {
                        menu.findItem(R.id.teacherHomeFragment).isVisible = false
                        menu.findItem(R.id.studentHomeFragment).isVisible = true
                        menu.findItem(R.id.teacherDashboardFragment).isVisible = false
                        navController.navigate(
                            R.id.studentHomeFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.loginFragment, true)
                                .build()
                        )
                    }

                    UserRole.TEACHER -> {
                        menu.findItem(R.id.studentHomeFragment).isVisible = false
                        menu.findItem(R.id.teacherHomeFragment).isVisible = true
                        navController.navigate(
                            R.id.teacherHomeFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.loginFragment, true)
                                .build()
                        )
                    }

                    else -> {
                        navController.navigate(
                            R.id.loginFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.loginFragment, true)
                                .build()
                        )
                    }
                }
                hideLoading()
            }
        } else {
            hideLoading()
            navController.navigate(R.id.loginFragment)
        }
    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onNavigateUp()
    }
}