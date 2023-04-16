package com.iye03.currencyexchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry

class PriceTraceFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: fetch the price list
    }

    override fun onResume() {
        super.onResume()
        //TODO: fetch the price list
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_price_trace, container, false)

        val lineChart: LineChart = view.findViewById(R.id.lineChart)

        val entries: ArrayList<Entry> = ArrayList()
        entries.add(Entry(0.0f, 4.0f))
        entries.add(Entry(1.0f, 2.0f))
        entries.add(Entry(2.0f, 3.0f))
        entries.add(Entry(3.0f, 5.0f))
        entries.add(Entry(4.0f, 1.0f))

        val dataSet = LineDataSet(entries, "Label") // add entries to dataset

        val lineData = LineData(dataSet)
        lineChart.setData(lineData)
        lineChart.invalidate() // refresh chart


        return view
    }
}