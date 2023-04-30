package com.iye03.currencyexchange

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.timepicker.TimeFormat
import com.iye03.currencyexchange.api.ExchangeService
import com.iye03.currencyexchange.api.model.ExchangeRates
import com.iye03.currencyexchange.api.model.TraceList
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter
import lecho.lib.hellocharts.gesture.ContainerScrollType
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList


class PriceTraceFragment : Fragment() ,SwipeRefreshLayout.OnRefreshListener {
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    val inputDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.getDefault())

    private var scaleSpinner: Spinner? = null
    private var priceChart: LineChartView? = null
    private var buySellSwitch: SwitchMaterial? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: fetch the price list
    }

    override fun onResume() {
        super.onResume()
        //TODO: fetch the price list
    }

    fun setUpScaleSpinner(){
        val scales = resources.getStringArray(R.array.scales)
        val adapter = object : ArrayAdapter<CharSequence>(this.requireContext(), R.layout.spinner_item, scales) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val label = view.findViewById<TextView>(R.id.item_text)

                // Set the label text using the "label" attribute from the data file
                val labelText = getItem(position)?.toString()?.substringAfter(":")?.trim() ?: ""
                label.text = labelText

                return view
            }
        }.also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            scaleSpinner?.adapter = adapter
        }


        class SpinnerActivity : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                processChart()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // do nothing
            }
        }

        scaleSpinner?.onItemSelectedListener = SpinnerActivity()
    }

    fun fillAndDrawData(timeFormat: String,isSell: Boolean,xLabels: MutableList<AxisValue>){
        val arrayToFill :MutableList<PointValue> = ArrayList<PointValue>()
        val traceData: TraceList = TraceList()
        traceData.timeFormat = timeFormat
        ExchangeService.exchangeApi(this.requireActivity().application).getTraceData(traceData).enqueue(object :
            Callback<TraceList> {

            override fun onResponse(call: Call<TraceList>, response:
            Response<TraceList>
            ) {
                if (!response.isSuccessful()){
                    return
                }
                val responseBody: TraceList? = response.body();
                var dates :List<String> = ArrayList<String>()
                responseBody?.dates?.let {
                    dates = it
                }
                var values : List<Float> = ArrayList<Float>()
                if(isSell){
                    responseBody?.averagesUsdToLbp?.let {
                        values=it
                    }
                }
                else {
                    responseBody?.averagesLbpToUsd?.let {
                        values=it
                    }
                }
                var i = 0;
                while(i<dates.size){
                    if(abs(values[i]+1.0f) > 0.1){
                        arrayToFill.add(PointValue((inputDateFormat.parse(dates[i]).toInstant().epochSecond-1577840461).toFloat(), values[i]/1000))
                    }
                    i+=1
                }
                displayChart(arrayToFill,xLabels)
            }

            override fun onFailure(call: Call<TraceList>, t: Throwable) {
                return;
            }

        })
    }

    fun processChart(){
        val cal = Calendar.getInstance()
        val xValues: MutableList<AxisValue> = ArrayList()

        val spinnerText = scaleSpinner?.selectedItem
        var isSell = false
        buySellSwitch?.let {
            isSell = it.isChecked
        }

        if(spinnerText == "1 Day"){
            val outputDateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            cal.time = Date.from(Instant.now())
            for(i in 0 until 7){
                xValues.add(AxisValue(cal.time.toInstant().epochSecond.toFloat()-1577840461).setLabel(outputDateFormat.format(cal.time)))
                cal.add(Calendar.HOUR, -4)
            }
            fillAndDrawData("Hourly",isSell,xValues)
        }
        else if(spinnerText == "1 Week"){
            val outputDateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
            cal.time = Date.from(Instant.now())
            for(i in 0 until 7){
                xValues.add(AxisValue(cal.time.toInstant().epochSecond.toFloat()-1577840461).setLabel(outputDateFormat.format(cal.time)))
                cal.add(Calendar.DAY_OF_MONTH, -1)
            }
            fillAndDrawData("Daily",isSell,xValues)
        }
        else if(spinnerText == "1 Month"){
            val outputDateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
            cal.time = Date.from(Instant.now())
            for(i in 0 until 3){
                xValues.add(AxisValue(cal.time.toInstant().epochSecond.toFloat()-1577840461).setLabel(outputDateFormat.format(cal.time)))
                cal.add(Calendar.DAY_OF_MONTH, -7)
            }
            fillAndDrawData("Weekly",isSell,xValues)
        }
    }
    fun displayChart(values: MutableList<PointValue>, axisLabels: MutableList<AxisValue>) {
        val line: Line = Line(values).setColor(Color.parseColor("#68B0AB")).setCubic(false)
        val lines: MutableList<Line> = ArrayList<Line>()
        lines.add(line)

        val data = LineChartData()
        data.lines = lines
        
        val axisX = Axis(axisLabels)
        axisX.setName("time")
        axisX.textColor = Color.parseColor("#BC323232")
        data.axisXBottom = axisX

        val axisY = Axis()
        axisY.formatter = SimpleAxisValueFormatter(0)
        axisY.setName("Price of 1 USD  in Thousand LBP")
        axisY.textColor = Color.parseColor("#BC323232")
        data.axisYLeft = axisY

        priceChart?.setLineChartData(data)
    }

    fun setUpSwitch(){
        buySellSwitch?.setOnCheckedChangeListener { buttonView, isChecked ->
            buySellSwitch?.text = if(isChecked){"Sell"} else {"Buy"}
            processChart()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_price_trace, container, false)

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshPriceTrace);
        swipeRefreshLayout?.setOnRefreshListener(this);

        scaleSpinner = view.findViewById(R.id.scale_spinner)
        priceChart = view.findViewById(R.id.price_chart);
        buySellSwitch = view.findViewById(R.id.buySell)
        priceChart?.setInteractive(true)
        priceChart?.setZoomEnabled(true)
        priceChart?.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL)

        setUpSwitch()

        setUpScaleSpinner()

        return view
    }

    override fun onRefresh() {
        processChart()
        swipeRefreshLayout?.setRefreshing(false)
    }
}