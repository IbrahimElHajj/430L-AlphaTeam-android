package com.iye03.currencyexchange

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.view.LineChartView


class PriceTraceFragment : Fragment() {
    private var scaleSpinner: Spinner? = null
    private var priceChart: LineChartView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: fetch the price list
    }

    override fun onResume() {
        super.onResume()
        //TODO: fetch the price list
    }

    fun setUpScaleSpinner(){
        val adapter = object : ArrayAdapter<CharSequence>(this.requireContext(), R.layout.spinner_item, resources.getStringArray(R.array.scales)) {
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
                //change scale in chart
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // do nothing
            }
        }

        scaleSpinner?.onItemSelectedListener = SpinnerActivity()
    }

    fun displayChart(values: MutableList<PointValue>) {
        val line: Line = Line(values).setColor(Color.parseColor("#68B0AB")).setCubic(true)

        val lines: MutableList<Line> = ArrayList<Line>()
        lines.add(line)

        val data = LineChartData()
        data.lines = lines

        val axisX = Axis()
        axisX.setName("time")
        axisX.textColor = Color.parseColor("#BC323232")
        data.axisXBottom = axisX

        val axisY = Axis()
        axisY.setName("Price of 1 USD")
        axisY.textColor = Color.parseColor("#BC323232")
        data.axisYLeft = axisY

        priceChart?.setLineChartData(data)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_price_trace, container, false)
        scaleSpinner = view.findViewById(R.id.scale_spinner)
        priceChart = view.findViewById(R.id.price_chart);

        val values: MutableList<PointValue> = ArrayList()
        values.add(PointValue(0f, 2f))
        values.add(PointValue(1f, 4f))
        values.add(PointValue(2f, 3f))
        values.add(PointValue(3f, 6f))
        values.add(PointValue(4f, 4f))

        displayChart(values)

        setUpScaleSpinner()

        return view
    }
}