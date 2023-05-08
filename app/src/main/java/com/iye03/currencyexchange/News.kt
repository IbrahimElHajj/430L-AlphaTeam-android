package com.iye03.currencyexchange

import Authentication
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.iye03.currencyexchange.api.ExchangeService
import com.iye03.currencyexchange.api.model.News
import com.iye03.currencyexchange.api.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class News : AppCompatActivity() {
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var listview: ListView? = null
    private var news: ArrayList<News>? = ArrayList()
    private var adapter: TransactionAdapter? = null
    private var menu: Menu? = null
    private var addNewsDialog: View? = null

    private fun fetchNews() {
        ExchangeService.exchangeApi(this.application)
            .getNews()
            .enqueue(object : Callback<List<News>> {
                override fun onFailure(call: Call<List<News>>,
                                       t: Throwable) {
                    return
                }
                override fun onResponse(
                    call: Call<List<News>>,
                    response: Response<List<News>>
                ) {
                    news?.clear()
                    news?.addAll(response.body()!!)
                    adapter?.notifyDataSetChanged()
                }
            })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)


        listview = findViewById(R.id.newsListview)
        adapter = TransactionAdapter(layoutInflater, news!!)
        listview?.adapter = adapter

        fetchNews()
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
            setMenu()
        } else if (item.itemId == R.id.addNews) {
            showDialog()
        }
        return true
    }


    private fun setMenu() {
        menu?.clear()
        menuInflater.inflate(if(Authentication.getToken() == null)
            R.menu.menu_logged_out else if(Authentication.canSendNews()) R.menu.menu_news else R.menu.menu_logged_in, menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_menu_24);
    }

    override fun onResume() {
        super.onResume()
        fetchNews()
    }

    class TransactionAdapter(
        private val inflater: LayoutInflater,
        private val dataSource: List<News>,
    ) : BaseAdapter() {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())


        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(position: Int, convertView: View?, parent:
        ViewGroup?): View {
            val view: View = inflater.inflate(R.layout.item_news,
                parent, false)
            view.findViewById<TextView>(R.id.PosterUsername).text =
                dataSource[position].newscaster_username.toString()
            view.findViewById<TextView>(R.id.NewsContent).text =
                dataSource[position].news.toString()
            view.findViewById<TextView>(R.id.date).text =
                outputDateFormat.format(inputDateFormat.parse(dataSource[position].addedDate)).toString()
            return view
        }
        override fun getItem(position: Int): Any {
            return dataSource[position]
        }
        override fun getItemId(position: Int): Long {
            return dataSource[position].id?.toLong() ?: 0
        }
        override fun getCount(): Int {
            return dataSource.size
        }
    }

    private fun showDialog() {
        addNewsDialog = LayoutInflater.from(this)
            .inflate(R.layout.dialog_news, null, false)
        MaterialAlertDialogBuilder(this,R.style.AlertDialogTheme)
            .setView(addNewsDialog)
            .setTitle("Add News Entry")
            .setPositiveButton("Add")
            { dialog, _ ->

                val text = addNewsDialog?.findViewById<TextInputLayout>(R.id.txtInptNews)?.
                editText?.text.toString()

                if(!text.isEmpty()) {
                    val news = News()
                    news.news = text

                    pushNews(news)
                }
                else{
                    Snackbar.make(listview as View, "Text missing from field!",
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

    private fun pushNews(news: News) {

        ExchangeService.exchangeApi(this.application).postNews(news,
            if (Authentication.getToken() != null)
                "Bearer ${Authentication.getToken()}"
            else
                null)
            .enqueue(object :
                Callback<News> {
                override fun onResponse(call: Call<News>, response:
                Response<News>) {
                    if(response.isSuccessful())
                    Snackbar.make(listview as View, "Transaction added!",
                        Snackbar.LENGTH_LONG)
                        .show()
                    else{
                        Snackbar.make(listview as View, "Could not add transaction.",
                            Snackbar.LENGTH_LONG)
                            .show()
                    }
                    fetchNews()

                }
                override fun onFailure(call: Call<News>, t: Throwable) {
                    Snackbar.make(listview as View, "Could not add transaction.",
                        Snackbar.LENGTH_LONG)
                        .show()
                }
            })
    }


}