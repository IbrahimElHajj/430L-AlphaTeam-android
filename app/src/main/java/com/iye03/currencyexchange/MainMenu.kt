package com.iye03.currencyexchange

import Authentication
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainMenu : AppCompatActivity() {
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val exchangeScreenButton: Button = findViewById(R.id.exchangeScreenButton)
        exchangeScreenButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            startActivity(intent)
        }
        val P2PScreenButton: Button = findViewById(R.id.P2PScreenButton)
        P2PScreenButton.setOnClickListener {
            val intent = Intent(this, P2P::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            startActivity(intent)
        }
        val NewsScreenButton: Button = findViewById(R.id.NewsScreenButton)
        NewsScreenButton.setOnClickListener {
            val intent = Intent(this, News::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        setMenu()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.login) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.register) {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.logout) {
            Authentication.clearToken()
            setMenu()
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
            startActivity(intent)
        }
        return true
    }

    private fun setMenu() {
        menu?.clear()
        menuInflater.inflate(if(Authentication.getToken() == null)
            R.menu.menu_logged_out else R.menu.menu_logged_in, menu)
    }
}