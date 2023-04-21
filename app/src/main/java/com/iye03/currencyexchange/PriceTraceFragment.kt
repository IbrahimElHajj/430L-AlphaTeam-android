package com.iye03.currencyexchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment

class PriceTraceFragment : Fragment() {
    private var scaleSpinner: Spinner? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: fetch the price list
    }

    override fun onResume() {
        super.onResume()
        //TODO: fetch the price list
    }

    fun setUpScaleSpinner(){
        ArrayAdapter.createFromResource(
            this.requireContext(),
            R.array.scales,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_price_trace, container, false)
        scaleSpinner = view.findViewById(R.id.scale_spinner)

        setUpScaleSpinner()

        return view
    }
}