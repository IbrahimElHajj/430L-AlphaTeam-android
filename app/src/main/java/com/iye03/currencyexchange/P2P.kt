package com.iye03.currencyexchange

import Authentication
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputLayout
import com.iye03.currencyexchange.api.ExchangeService
import com.iye03.currencyexchange.api.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class P2P : AppCompatActivity() {
    private var fab: FloatingActionButton? = null
    private var transactionDialog: View? = null
    private var menu: Menu? = null
    private var tabLayout: TabLayout? = null
    private var tabsViewPager: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)//recycling is good
        setTitle(R.string.p2p)
        tabLayout = findViewById(R.id.tabLayout)
        tabsViewPager = findViewById(R.id.tabsViewPager)
        tabLayout?.tabMode = TabLayout.MODE_FIXED
        tabLayout?.isInlineLabel = true
        // Enable Swipe
        tabsViewPager?.isUserInputEnabled = false
        // Set the ViewPager Adapter
        refreshTabs()

        fab = findViewById(R.id.fab)
        fab?.setOnClickListener { view ->
            showDialog()
        }
        if(Authentication.getToken().isNullOrBlank()){
            fab?.hide()
        }
    }

    fun refreshTabs(){
        val adapter = TabsPagerAdapterP2P(supportFragmentManager, lifecycle)
        tabsViewPager?.adapter = adapter

        TabLayoutMediator(tabLayout!!, tabsViewPager!!) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "All"
                }
                1 -> {
                    tab.text = "Mine"
                }
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        setMenu()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 16908332) {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.login) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.register) {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.logout) {
            Authentication.clearToken()
            fab?.hide()
            setMenu()
            refreshTabs()
        }
        return true
    }

    private fun setMenu() {
        menu?.clear()
        menuInflater.inflate(if(Authentication.getToken() == null)
            R.menu.menu_logged_out else R.menu.menu_logged_in, menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24);
    }

    private fun showDialog() {
        transactionDialog = LayoutInflater.from(this)
            .inflate(R.layout.dialog_transaction_with_phone, null, false)
        MaterialAlertDialogBuilder(this,R.style.AlertDialogTheme)
            .setView(transactionDialog)
            .setTitle("Add Transaction")
            .setMessage("Enter transaction details")
            .setPositiveButton("Add")
            { dialog, _ ->

                val phoneNumber = transactionDialog?.findViewById<TextInputLayout>(R.id.txtInptPhoneNumber)?.
                editText?.text.toString()
                val usdAmount = transactionDialog?.findViewById<TextInputLayout>(R.id.txtInptUsdAmount)?.
                editText?.text.toString()
                val lbpAmount = transactionDialog?.findViewById<TextInputLayout>(R.id.txtInptLbpAmount)?.
                editText?.text.toString()
                val usdToLbp = transactionDialog?.findViewById<RadioGroup>(R.id.rdGrpTransactionType)?.
                checkedRadioButtonId == R.id.rdBtnSellUsd

                if(!usdAmount.isEmpty() && !lbpAmount.isEmpty()) {
                    val trans = Transaction()
                    trans.seller_phone_number = phoneNumber.toInt()
                    trans.usdAmount = usdAmount.toFloat()
                    trans.lbpAmount = lbpAmount.toFloat()
                    trans.usdToLbp = usdToLbp

                    addTransactionP2P(trans)
                }
                else{
                    Snackbar.make(fab as View, "Missing fields!",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun addTransactionP2P(transaction: Transaction) {

        ExchangeService.exchangeApi(this.application).addTransactionP2P(transaction,
            if (Authentication.getToken() != null)
                "Bearer ${Authentication.getToken()}"
            else
                null)
            .enqueue(object :
                Callback<Any> {
                override fun onResponse(call: Call<Any>, response:
                Response<Any>
                ) {
                    if(response.isSuccessful())
                        Snackbar.make(fab as View, "Transaction added!",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Snackbar.make(fab as View, "Could not add transaction.",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
            })
    }
}