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
import kotlinx.serialization.decodeFromString
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request

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
    private var searchCount : Int = 0
    private lateinit var userAgent : String


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
        fetchReddit()
    }

    private fun initVars() {
        userAgent = ""
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
        startBtn.setOnClickListener {
            if (searchInput.text.toString().isNullOrEmpty()) {
                Toast.makeText(this, "Please enter valid number of searches", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            searchCount = searchInput.text.toString().toInt()
            fetchLocalQueries()
        }
    }

    private fun fetchReddit() {
        
    }
    private fun fetchLocalQueries() {
        var allQueries = readLocalQueries()
        if (allQueries.isEmpty()) {
            Log.i("DEBUG", "No local queries found")
            Toast.makeText(this, "No local queries found or can't read them", Toast.LENGTH_SHORT).show()
            return
        }
        allQueries = allQueries.shuffled()
        Log.i("DEBUG", allQueries.toString())
        queries = allQueries.take(searchCount)
        // Log.i("DEBUG", "number of queries: ${queries.size}")
    }

    private fun readLocalQueries(): List<String> {
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

    private fun getRandomUserAgent(onUserAgentReady: (String) -> Unit){
        try {
            RetrofitClient_getChromeVersion.instance.getChromeVersion("application/json")
                .enqueue(object : retrofit2.Callback<ChromeVersionResponse> {
                    override fun onResponse(call: retrofit2.Call<ChromeVersionResponse>, response: retrofit2.Response<ChromeVersionResponse>) {
                        val finalUserAgent: String
                        if (response.isSuccessful) {
                            val chromeVersionResponse = response.body()
                            val chromeVersions = chromeVersionResponse!!.channels.values.map { it.version }
                            val systemComponent = "Windows NT 10.0; Win64; x64"
                            val shuffledVersions = chromeVersions.shuffled()
                            val chromeMajorVersion = shuffledVersions[0].split('.')[0]
                            val chromeReducedVersion = "$chromeMajorVersion.0.0.0"
                            finalUserAgent = "Mozilla/5.0 ($systemComponent) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/$chromeReducedVersion Safari/537.36"
                            Log.i("DEBUG", "User agent fetched: $finalUserAgent")
                        } else {
                            Log.w("DEBUG", "Failed to get chrome version, using default.")
                            // Default user-agent
                            finalUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
                        }
                        onUserAgentReady(finalUserAgent)
                    }
                    override fun onFailure(call: Call<ChromeVersionResponse>, t: Throwable) {
                        Log.e("DEBUG", "Failed to get chrome version on failure: ${t.message}", t)
                        // Default user-agent
                        val defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
                        onUserAgentReady(defaultUserAgent)
                    }
                })
        } catch (e: Exception) {
            Log.e("DEBUG", "Exception in getRandomUserAgent: ${e.message}", e)
            val defaultUserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"
            onUserAgentReady(defaultUserAgent)
        }
    }

}