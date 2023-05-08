package com.iye03.currencyexchange

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.iye03.currencyexchange.api.ExchangeService
import com.iye03.currencyexchange.api.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MyP2POptions : Fragment() {
    private var listview: ListView? = null
    private var transactions: ArrayList<Transaction>? = ArrayList()
    private var adapter: TransactionAdapter? = null

    private fun fetchTransactions() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi(this.requireActivity().application)
                .getMyTransactionsP2P("Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<List<Transaction>> {
                    override fun onFailure(
                        call: Call<List<Transaction>>,
                        t: Throwable
                    ) {
                        return
                    }

                    override fun onResponse(
                        call: Call<List<Transaction>>,
                        response: Response<List<Transaction>>
                    ) {
                        transactions?.clear()
                        transactions?.addAll(response.body()!!)
                        adapter?.notifyDataSetChanged()
                    }
                })
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchTransactions()
    }

    override fun onResume() {
        super.onResume()
        fetchTransactions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_all_p2_p_options,
            container, false)

        listview = view.findViewById(R.id.listview)
        adapter = TransactionAdapter(layoutInflater, transactions!!)
        listview?.adapter = adapter
        return view
    }

    class TransactionAdapter(
        private val inflater: LayoutInflater,
        private val dataSource: List<Transaction>
    ) : BaseAdapter() {
        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())


        @RequiresApi(Build.VERSION_CODES.O)
        override fun getView(position: Int, convertView: View?, parent:
        ViewGroup?): View {
            val view: View = inflater.inflate(R.layout.item_transactionP2P,
                parent, false)
            view.findViewById<TextView>(R.id.ID).text =
                dataSource[position].id.toString()
            view.findViewById<TextView>(R.id.LBP).text =
                dataSource[position].lbpAmount.toString()
            view.findViewById<TextView>(R.id.USD).text =
                dataSource[position].usdAmount.toString()
            view.findViewById<TextView>(R.id.DATE).text =
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


}