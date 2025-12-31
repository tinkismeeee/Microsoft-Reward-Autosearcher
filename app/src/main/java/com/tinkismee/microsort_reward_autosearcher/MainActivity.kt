package com.tinkismee.microsort_reward_autosearcher

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class MainActivity : AppCompatActivity() {
    private lateinit var searchInput : TextInputEditText
    private lateinit var webView: WebView
    private lateinit var delayInput : TextInputEditText
    private lateinit var loginBtn : TextView
    private lateinit var logoutBtn : TextView
    private lateinit var startBtn : Button
    private lateinit var keepScreenBtn : CheckBox
    private lateinit var redditSourceBtn : CheckBox
    private lateinit var googleTrendSourceBtn : CheckBox
    private lateinit var wikipediaSourceBtn : CheckBox
    private lateinit var newspaperSourceBtn : CheckBox
    private lateinit var queries : List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        webView = findViewById(R.id.webViewBing)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }
        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://www.bing.com")

        initVars()
        listeners()


        fetchLocalQueries()
    }

    private fun initVars() {
        queries = listOf()
        searchInput = findViewById(R.id.searchInput)
        delayInput = findViewById(R.id.delayInput)
        loginBtn = findViewById(R.id.loginBtn)
        logoutBtn = findViewById(R.id.logoutBtn)
        startBtn = findViewById(R.id.startBtn)
        keepScreenBtn = findViewById(R.id.keepScreenBtn)
        redditSourceBtn = findViewById(R.id.redditSourceBtn)
        googleTrendSourceBtn = findViewById(R.id.googleTrendSourceBtn)
        wikipediaSourceBtn = findViewById(R.id.wikipediaSourceBtn)
        newspaperSourceBtn = findViewById(R.id.newspaperSourceBtn)
    }

    private fun listeners() {
        loginBtn.setOnClickListener {
            webView.loadUrl("https://www.bing.com/rewards/dashboard")
        }
        logoutBtn.setOnClickListener {
            webView.loadUrl("https://rewards.bing.com/Signout")
        }

    }

    private fun fetchReddit() {

    }

    private fun fetchLocalQueries() {
        val allQueries = readLocalQueries()
        if (allQueries.isEmpty()) {
            Log.i("DEBUG", "No local queries found")
            Toast.makeText(this, "No local queries found or can't read them", Toast.LENGTH_SHORT).show()
            return
        }
        queries = allQueries
    }

    fun readLocalQueries(): List<String> {
        var allQueries : List<String> = listOf()
        try {
            val inputStream = assets.open("queries.json")
            val categories: List<localQueryDataClass> = Json { ignoreUnknownKeys = true; explicitNulls = false }
                .decodeFromStream<List<localQueryDataClass>>(inputStream)
            allQueries = categories.flatMap { it.queries }
        }
        catch (e: Exception) {
                Log.e("DEBUG", "Failed to read local queries", e)
        }
        return allQueries
    }
}