package com.mmarquezroldan.quickunits

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {

    private lateinit var category: Spinner
    private lateinit var mainUnit: Spinner
    private lateinit var intoUnit: Spinner
    private lateinit var toConvert: EditText
    private lateinit var result: TextView
    private lateinit var convert: Button
    private var currentCategory: String = "Mass"
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        val adContainer = findViewById<LinearLayout>(R.id.lyAdsBanner)

        adView = AdView(this).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = "ca-app-pub-3940256099942544/6300978111" // ID de prueba de Google
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        }

        adContainer.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        category = findViewById(R.id.category)
        mainUnit = findViewById(R.id.main_unit)
        intoUnit = findViewById(R.id.into_unit)
        toConvert = findViewById(R.id.to_convert)
        result = findViewById(R.id.result)
        convert = findViewById(R.id.convert)

        val categoryAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.category,
            android.R.layout.simple_spinner_item
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        category.adapter = categoryAdapter

        category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentCategory = parent?.getItemAtPosition(position).toString()

                val unitsArrayId = when (currentCategory) {
                    "Mass" -> R.array.mass_units
                    "Length" -> R.array.length_units
                    "Temperature" -> R.array.temperature_units
                    "Speed" -> R.array.speed_units
                    "Energy" -> R.array.energy_units
                    "Power" -> R.array.power_units
                    "Pressure" -> R.array.pressure_units
                    "Volume" -> R.array.volume_units
                    else -> null
                }

                unitsArrayId?.let {
                    val unitsAdapter = ArrayAdapter.createFromResource(
                        this@MainActivity,
                        it,
                        android.R.layout.simple_spinner_item
                    )
                    unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    mainUnit.adapter = unitsAdapter
                    intoUnit.adapter = unitsAdapter
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Botón de conversión
        convert.setOnClickListener {
            val input = toConvert.text.toString()
            if (input.isBlank()) {
                Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val value = input.toDoubleOrNull()
            if (value == null) {
                Toast.makeText(this, "Invalid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fromUnit = mainUnit.selectedItem.toString()
            val toUnit = intoUnit.selectedItem.toString()

            val fromUnitShort = when (fromUnit) {
                "Kilogram" -> "kg"
                "Gram" -> "g"
                "Pound" -> "lb"
                "Kilometer" -> "km"
                "Meter" -> "m"
                "Centimeter" -> "cm"
                "Millimeter" -> "mm"
                "Inch" -> "in"
                "Celsius" -> "°C"
                "Fahrenheit" -> "°F"
                "Kelvin" -> "K"
                "Kilometer per hour" -> "km/h"
                "Meter per second" -> "m/s"
                "Mile per hour" -> "mph"
                "Joule" -> "J"
                "Kilowatt hour" -> "kWh"
                "Calorie" -> "cal"
                "Kilocalorie" -> "kcal"
                "Watt-hour" -> "Wh"
                "BTU" -> "BTU"
                "Electronvolt" -> "eV"
                "Watt" -> "W"
                "Kilowatt" -> "kW"
                "Horsepower" -> "hp"
                "Megawatt" -> "MW"
                "Gigawatt" -> "GW"
                "Pascal" -> "Pa"
                "Kilopascal" -> "kPa"
                "Bar" -> "bar"
                "Atmosphere" -> "atm"
                "Torr" -> "torr"
                "Liter" -> "L"
                "Milliliter" -> "mL"
                "Gallon" -> "gal"
                "Ounce (fluid)" -> "oz"
                else -> ""
            }

            val toUnitShort = when (toUnit) {
                "Kilogram" -> "kg"
                "Gram" -> "g"
                "Pound" -> "lb"
                "Kilometer" -> "km"
                "Meter" -> "m"
                "Centimeter" -> "cm"
                "Millimeter" -> "mm"
                "Inch" -> "in"
                "Celsius" -> "°C"
                "Fahrenheit" -> "°F"
                "Kelvin" -> "K"
                "Kilometer per hour" -> "km/h"
                "Meter per second" -> "m/s"
                "Mile per hour" -> "mph"
                "Joule" -> "J"
                "Kilowatt hour" -> "kWh"
                "Calorie" -> "cal"
                "Kilocalorie" -> "kcal"
                "Watt-hour" -> "Wh"
                "BTU" -> "BTU"
                "Electronvolt" -> "eV"
                "Watt" -> "W"
                "Kilowatt" -> "kW"
                "Horsepower" -> "hp"
                "Megawatt" -> "MW"
                "Gigawatt" -> "GW"
                "Pascal" -> "Pa"
                "Kilopascal" -> "kPa"
                "Bar" -> "bar"
                "Atmosphere" -> "atm"
                "Torr" -> "torr"
                "Liter" -> "L"
                "Milliliter" -> "mL"
                "Gallon" -> "gal"
                "Ounce (fluid)" -> "oz"
                else -> ""
            }

            val resultValue = when (currentCategory) {
                "Mass" -> calculateMass(value, fromUnit, toUnit)
                "Length" -> calculateLength(value, fromUnit, toUnit)
                "Temperature" -> calculateTemperature(value, fromUnit, toUnit)
                "Speed" -> calculateSpeed(value, fromUnit, toUnit)
                "Energy" -> calculateEnergy(value, fromUnit, toUnit)
                "Power" -> calculatePower(value, fromUnit, toUnit)
                "Pressure" -> calculatePressure(value, fromUnit, toUnit)
                "Volume" -> calculateVolume(value, fromUnit, toUnit)
                else -> 0.0
            }

            result.text = "$input $fromUnitShort is equal to $resultValue $toUnitShort"
        }
    }

    private fun calculateMass(value: Double, from: String, to: String): Double {
        val kg = when (from) {
            "Kilogram" -> value
            "Gram" -> value / 1000
            "Pound" -> value * 0.453592
            else -> 0.0
        }

        return when (to) {
            "Kilogram" -> kg
            "Gram" -> kg * 1000
            "Pound" -> kg * 2.20462
            else -> 0.0
        }
    }

    private fun calculateLength(value: Double, from: String, to: String): Double {
        val meters = when (from) {
            "Kilometer" -> value * 1000
            "Meter" -> value
            "Centimeter" -> value / 100
            "Millimeter" -> value / 1000
            "Inch" -> value * 0.0254
            else -> 0.0
        }

        return when (to) {
            "Kilometer" -> meters / 1000
            "Meter" -> meters
            "Centimeter" -> meters * 100
            "Millimeter" -> meters * 1000
            "Inch" -> meters / 0.0254
            else -> 0.0
        }
    }

    private fun calculateTemperature(value: Double, from: String, to: String): Double {
        val celsius = when (from) {
            "Celsius" -> value
            "Fahrenheit" -> (value - 32) * 5 / 9
            "Kelvin" -> value - 273.15
            else -> 0.0
        }

        return when (to) {
            "Celsius" -> celsius
            "Fahrenheit" -> celsius * 9 / 5 + 32
            "Kelvin" -> celsius + 273.15
            else -> 0.0
        }
    }

    private fun calculateSpeed(value: Double, from: String, to: String): Double {
        val metersPerSecond = when (from) {
            "Kilometer per hour" -> value / 3.6
            "Meter per second" -> value
            "Mile per hour" -> value * 0.44704
            else -> 0.0
        }

        return when (to) {
            "Kilometer per hour" -> metersPerSecond * 3.6
            "Meter per second" -> metersPerSecond
            "Mile per hour" -> metersPerSecond * 2.23694
            else -> 0.0
        }
    }

    private fun calculateEnergy(value: Double, from: String, to: String): Double {
        val joules = when (from) {
            "Joule" -> value
            "Kilowatt hour" -> value * 3_600_000
            "Calorie" -> value * 4.1868
            "Kilocalorie" -> value * 4186.8
            "Watt-hour" -> value * 3600
            "BTU" -> value * 1055.056
            "Electronvolt" -> value * 1.60218e-19
            else -> 0.0
        }

        return when (to) {
            "Joule" -> joules
            "Kilowatt hour" -> joules / 3_600_000
            "Calorie" -> joules / 4.1868
            "Kilocalorie" -> joules / 4186.8
            "Watt-hour" -> joules / 3600
            "BTU" -> joules / 1055.056
            "Electronvolt" -> joules / 1.60218e-19
            else -> 0.0
        }
    }

    private fun calculatePower(value: Double, from: String, to: String): Double {
        val watts = when (from) {
            "Watt" -> value
            "Kilowatt" -> value * 1000
            "Horsepower" -> value * 745.699872
            "Megawatt" -> value * 1000000
            "Gigawatt" -> value * 1000000000
            else -> 0.0
        }

        return when (to) {
            "Watt" -> watts
            "Kilowatt" -> watts / 1000
            "Horsepower" -> watts / 745.699872
            "Megawatt" -> watts / 1000000
            "Gigawatt" -> watts / 1000000000
            else -> 0.0
        }
    }

    private fun calculatePressure(value: Double, from: String, to: String): Double {
        val pascales = when (from) {
            "Pascal" -> value
            "Kilopascal" -> value * 1000
            "Bar" -> value * 100000
            "Atmosphere" -> value * 101325
            "Torr" -> value * 133.322
            else -> 0.0
        }

        return when (to) {
            "Pascal" -> pascales
            "Kilopascal" -> pascales / 1000
            "Bar" -> pascales / 100000
            "Atmosphere" -> pascales / 101325
            "Torr" -> pascales / 133.322
            else -> 0.0
        }
    }

    private fun calculateVolume(value: Double, from: String, to: String): Double {
        val liters = when (from) {
            "Liter" -> value
            "Milliliter" -> value / 1000
            "Gallon" -> value * 3.78541
            "Ounce (fluid)" -> value * 0.0295735
            else -> 0.0
        }

        return when (to) {
            "Liter" -> liters
            "Milliliter" -> liters * 1000
            "Gallon" -> liters / 3.78541
            "Ounce (fluid)" -> liters / 0.0295735
            else -> 0.0
        }
    }
}
