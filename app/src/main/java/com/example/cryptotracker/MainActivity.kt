package com.example.cryptotracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    // Declaramos variables
    // Usaremos la API de Coindesk para que nos devuelva el precio del Bitcoin usando la librería OkHttp
    val URL = "https://api.coindesk.com/v1/bpi/currentprice.json"
    var apiCall: OkHttpClient = OkHttpClient()
    private lateinit var barraProgreso: ProgressBar
    private lateinit var valorCrypto: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "Inicio de sesión exitoso. Disfruta de la APP.", Toast.LENGTH_SHORT).show()
    }

    // Menu botón para cargar o actualizar el precio
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.cargar -> {
                cargarPrecio()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun cargarPrecio() {
        // Veremos una animación cuando pulsemos el botón para cargar el precio de la criptomoneda
        barraProgreso = findViewById(R.id.barraProgreso)
        barraProgreso.visibility = View.VISIBLE

        val request: Request = Request.Builder().url(URL).build()
        apiCall.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) { }

            override fun onResponse(call: Call?, response: Response?) {
                // Llamaremos a la API para que nos devuelva el precio del Bitcoin
                val json = response?.body()?.string()
                // Le pediremos que nos de el precio en USD y en EUR y sin decimales
                val usdPrecio = (JSONObject(json).getJSONObject("bpi").getJSONObject("USD")["rate"] as String).split(".")[0]
                val eurPrecio = (JSONObject(json).getJSONObject("bpi").getJSONObject("EUR")["rate"] as String).split(".")[0]

                runOnUiThread {
                    barraProgreso.visibility = View.GONE
                    valorCrypto = findViewById(R.id.valorCrypto)
                    valorCrypto.text = "$usdPrecio$\n\n$eurPrecio€"
                }
            }
        })
    }

}