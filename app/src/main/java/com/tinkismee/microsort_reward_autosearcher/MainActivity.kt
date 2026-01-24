package com.tinkismee.microsort_reward_autosearcher

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import java.io.IOException
import java.util.Locale


class MainActivity : AppCompatActivity() {
    private lateinit var searchInput: TextInputEditText
    private lateinit var webView: WebView
    private lateinit var delayInput: TextInputEditText
    private lateinit var loginBtn: TextView
    private lateinit var logoutBtn: TextView
    private lateinit var startBtn: Button
    private lateinit var keepScreenBtn: CheckBox
    private lateinit var redditSourceBtn: CheckBox
    private lateinit var googleTrendSourceBtn: CheckBox
    private lateinit var wikipediaSourceBtn: CheckBox
    private lateinit var newspaperSourceBtn: CheckBox
    private lateinit var local_queries: List<String>
    private var searchCount: Int = 0
    private var delayCount: Int = 0
    private lateinit var userAgent: String
    private lateinit var fetched_queries_Reddit: List<String>
    private lateinit var fetched_queries_GoogleTrends: List<String>
    private lateinit var fetched_queries_Wikipedia: List<String>
    private lateinit var fetched_queries_Newspaper: List<String>
    private lateinit var finalQueries: MutableList<String>
    private var currentIndex = 0
    private var isRunning = false
    private var waitingForSearch = false
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { startAutoSearch() }
    private lateinit var backgroundRunBtn: CheckBox
    private lateinit var statusTextView: TextView
    private lateinit var progressBar: android.widget.ProgressBar
    private lateinit var vietnam: ImageView
    private lateinit var unitedstates: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadLocale()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        webView = findViewById(R.id.webViewBing)
        webView.keepScreenOn = false
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }
        webView.loadUrl("https://www.bing.com")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (!isRunning) {
                    return
                }
                when {
                    waitingForSearch && url == "https://www.bing.com/" -> {
                        waitingForSearch = false
                        searchLikeRealUser(finalQueries[currentIndex])
                        statusTextView.text = getString(R.string.progress_text, (currentIndex + 1), searchCount)
                        progressBar.setProgress(currentIndex + 1, true)
                        currentIndex++
                    }
                    url?.contains("bing.com/search") == true -> {
                        ScrollJS()
                        handler.postDelayed({
                            waitingForSearch = true
                            startAutoSearch()
                        }, delayCount * 1000L)
                    }
                }
            }
        }

        initVars()
        listeners()
        fetchReddit()
        fetchGoogleTrend()
        fetchLocalQueries()
        fetchWikipedia()
        fetchNewspaper()
    }

    private fun initVars() {
        userAgent = ""
        finalQueries = mutableListOf()
        fetched_queries_Newspaper = listOf()
        fetched_queries_Wikipedia = listOf()
        fetched_queries_Reddit = listOf()
        fetched_queries_GoogleTrends = listOf()
        local_queries = listOf()
        searchInput = findViewById(R.id.searchInput)
        delayInput = findViewById(R.id.delayInput)
        loginBtn = findViewById(R.id.loginBtn)
        logoutBtn = findViewById(R.id.logoutBtn)
        startBtn = findViewById(R.id.startBtn)
        keepScreenBtn = findViewById(R.id.keepScreenBtn)
        backgroundRunBtn = findViewById(R.id.backgroundRunBtn)
        redditSourceBtn = findViewById(R.id.redditSourceBtn)
        googleTrendSourceBtn = findViewById(R.id.googleTrendSourceBtn)
        wikipediaSourceBtn = findViewById(R.id.wikipediaSourceBtn)
        newspaperSourceBtn = findViewById(R.id.newspaperSourceBtn)
        statusTextView = findViewById(R.id.statusTextView)
        progressBar = findViewById(R.id.progressBar)
        unitedstates = findViewById(R.id.unitedstates)
        vietnam = findViewById(R.id.vietnam)
    }

    private fun listeners() {
        unitedstates.setOnClickListener {
            setLocale("en")
        }

        vietnam.setOnClickListener {
            setLocale("vi")
        }
        loginBtn.setOnClickListener {
            webView.loadUrl("https://www.bing.com/fd/auth/signin?action=interactive&provider=windows_live_id&return_url=https%3A%2F%2Fwww.bing.com%2F")
        }

        logoutBtn.setOnClickListener {
            webView.clearCache(true)
            webView.clearFormData()
            webView.clearHistory()
            webView.clearSslPreferences()
            webView.clearMatches()
            CookieManager.getInstance().removeAllCookies {
                CookieManager.getInstance().flush()
            }
            webView.loadUrl("https://rewards.bing.com/Signout")
        }

        startBtn.setOnClickListener {
            if (isRunning) {
                isRunning = false
                handler.removeCallbacks(searchRunnable)
                startBtn.text = getString(R.string.startBtn_description)
                statusTextView.text = "0/0"
                progressBar.progress = 0
                Toast.makeText(this, getString(R.string.stopBtn_notification), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (searchInput.text.toString().isEmpty() || delayInput.text.toString().isEmpty()) {
                Toast.makeText(this, getString(R.string.invalid_searchcount), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            searchCount = searchInput.text.toString().toInt()
            delayCount = delayInput.text.toString().toInt()
            if (searchCount <= 0 || delayCount <= 0) {
                Toast.makeText(this, getString(R.string.invalid_delay), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            finalQueries = mutableListOf()
            if (redditSourceBtn.isChecked) finalQueries.addAll(fetched_queries_Reddit)
            if (googleTrendSourceBtn.isChecked) finalQueries.addAll(fetched_queries_GoogleTrends)
            if (wikipediaSourceBtn.isChecked) finalQueries.addAll(fetched_queries_Wikipedia)
            if (newspaperSourceBtn.isChecked) finalQueries.addAll(fetched_queries_Newspaper)
            if (finalQueries.isEmpty()) {
                Toast.makeText(this, getString(R.string.noqueries_found), Toast.LENGTH_LONG).show()
                finalQueries.addAll(local_queries)
            }
            finalQueries = finalQueries.map { it.lowercase() }.distinct().shuffled().take(searchCount).toMutableList()
            Log.i("DEBUG FINAL QUERIES", finalQueries.toString())
            for (i in 0 until finalQueries.size) {
                finalQueries[i] = finalQueries[i].replace(Regex("[^A-Za-z0-9 ]"),"")
                var temp = finalQueries[i].split(' ')
                temp = temp.take((3..6).random())
                finalQueries[i] = temp.joinToString(" ")
            }
            Log.i("DEBUG FINAL QUERIES", finalQueries.toString())
            currentIndex = 0
            isRunning = true
            startBtn.text = getString(R.string.stopBtn_description)
            progressBar.max = searchCount
            progressBar.progress = 0
            statusTextView.text = "0 / $searchCount"
            handler.post(searchRunnable)
        }

        keepScreenBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            else window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        backgroundRunBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                    if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                        != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                        androidx.core.app.ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
                    }
                }
                val serviceIntent = android.content.Intent(this, AutoSearchService::class.java)
                serviceIntent.putExtra("inputExtra", "Đang giữ app chạy nền...")
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startForegroundService(serviceIntent)
                } else {
                    startService(serviceIntent)
                }
                Toast.makeText(this, getString(R.string.background_start), Toast.LENGTH_SHORT).show()
            } else {
                val serviceIntent = android.content.Intent(this, AutoSearchService::class.java)
                stopService(serviceIntent)
                Toast.makeText(this, getString(R.string.background_stop), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun searchLikeRealUser(query: String) {
        val safe = query
            .replace("\\", "\\\\")
            .replace("'", "\\'")
        val js = """
        (function () {
            function waitInput(retry) {
                const input = document.querySelector('#sb_form_q');
                if (!input) {
                    if (retry < 30) setTimeout(() => waitInput(retry + 1), 300);
                    return;
                }
                input.focus();
                input.value = '';
                const text = '$safe';
                let i = 0;
                function typeChar() {
                    if (i < text.length) {
                        input.value += text.charAt(i);
                        input.dispatchEvent(new Event('input', { bubbles: true }));
                        i++;
                        setTimeout(typeChar, 20 + Math.random() * 15);
                    } else {
                        input.dispatchEvent(
                            new KeyboardEvent('keydown', {
                                bubbles: true,
                                cancelable: true,
                                key: 'Enter',
                                code: 'Enter',
                                keyCode: 13
                            })
                        );
                    }
                }
                typeChar();
            }
            waitInput(0);
        })();
    """.trimIndent()
        webView.evaluateJavascript(js, null)
    }

    private fun startAutoSearch() {
        if (!isRunning) return
        if (currentIndex >= finalQueries.size) {
            isRunning = false
            currentIndex = 0
            startBtn.text = getString(R.string.startBtn_description)
            statusTextView.text = getString(R.string.complete_description)
            return
        }
        waitingForSearch = true
        webView.loadUrl("https://www.bing.com")
    }
    private fun fetchNewspaper(){
        getRandomUserAgent { user_Agent ->
            Log.d("DEBUG NEWSPAPER", "Proceeding with User-Agent: $user_Agent")
            var urls = listOf(
                "https://newsapi.org/v2/everything?q=tesla&from=2025-12-03&sortBy=publishedAt&apiKey=${BuildConfig.API_KEY}",
                "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=${BuildConfig.API_KEY}",
                "https://newsapi.org/v2/top-headlines?sources=techcrunch&apiKey=${BuildConfig.API_KEY}",
                "https://newsapi.org/v2/everything?q=china&from=2025-12-03&sortBy=publishedAt&apiKey=${BuildConfig.API_KEY}",
                "https://newsapi.org/v2/everything?domains=wsj.com&apiKey=${BuildConfig.API_KEY}",
                "https://newsapi.org/v2/everything?q=apple&from=2025-12-03&sortBy=publishedAt&apiKey=${BuildConfig.API_KEY}",
                "https://newsapi.org/v2/everything?q=vietnam&from=2025-12-03&sortBy=publishedAt&apiKey=${BuildConfig.API_KEY}")
            urls = urls.shuffled()
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(urls[0])
                .header("Accept", "application/json")
                .header("User-Agent", user_Agent)
                .build()
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("DEBUG NEWSPAPER", "Request failed", e)
                }
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.body()?.use { responseBody ->
                        if (response.isSuccessful) {
                            val body = responseBody.string()
                            val json = JSONObject(body)
                            val arraySize = json.getJSONArray("articles").length()
                            if (arraySize > 0) {
                                Log.i("DEBUG NEWSPAPER", "Fetched data from Newspaper successfully!")
                                for (i in 0 until arraySize) {
                                    val title = json.getJSONArray("articles").getJSONObject(i).getString("title")
                                    fetched_queries_Newspaper = fetched_queries_Newspaper + title
                                }
                                // Log.i("DEBUG NEWSPAPER", "${fetched_queries_Newspaper}")
                            }
                        }
                        else {
                            Log.e("DEBUG NEWSPAPER", "HTTP ${response.code()}: ${response.message()}")
                        }
                    }
                }
            })
        }

    }
    private fun fetchWikipedia() {
        getRandomUserAgent { user_Agent ->
            Log.d("DEBUG WIKIPEDIA", "Proceeding with User-Agent: $user_Agent")
            val client = OkHttpClient()
            val request = Request.Builder()
                .header("Accept", "application/json")
                .header("User-Agent", user_Agent)
                .url("https://vi.wikipedia.org/w/api.php?action=query&list=random&rnnamespace=0&rnlimit=15&format=json")
                .build()
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("DEBUG WIKIPEDIA", "Request failed", e)
                }
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.body()?.use { responseBody ->
                        if (response.isSuccessful) {
                            val body = responseBody.string()
                            val json = JSONObject(body)
                            val arraySize = json.getJSONObject("query").getJSONArray("random").length()
                            if (arraySize > 0) {
                                Log.i("DEBUG WIKIPEDIA", "Fetched data from Wikipedia successfully!")
                                for (i in 0 until arraySize) {
                                    val title = json.getJSONObject("query").getJSONArray("random").getJSONObject(i).getString("title")
                                    fetched_queries_Wikipedia = fetched_queries_Wikipedia + title
                                }
                                // Log.i("DEBUG WIKIPEDIA", "${fetched_queries_Wikipedia}")
                            }
                            else {
                                Log.i("DEBUG WIKIPEDIA", "Unknows errors occurred!")
                            }
                        }
                    }
                }
            })
        }
    }
    private fun fetchGoogleTrend() {
        getRandomUserAgent { user_Agent ->
            Log.d("DEBUG GOOGLE TREND", "Proceeding with User-Agent: $user_Agent")
            val geoLocation = listOf("VN","US","GE", "FR", "ES", "IT", "PT", "BR", "RO")
            val randomGeoLocation = geoLocation.shuffled()[0]
            val client = OkHttpClient()
            val formBody = FormBody.Builder()
                .add("f.req", "[[[\"i0OFE\",\"[null,null,\\\"${randomGeoLocation}\\\",0,null,${(20..48).random()}]\"]]]")
                .build()
            val request = Request.Builder()
                .url("https://trends.google.com/_/TrendsUi/data/batchexecute")
                .post(formBody)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                .header("Origin", "https://trends.google.com")
                .header("Referer", "https://trends.google.com/trends/")
                .header("User-Agent", user_Agent)
                .header("Accept", "/")
                .build()
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("DEBUG GOOGLE TREND", "Request failed", e)
                }
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.body()?.use { responseBody ->
                        if (response.isSuccessful) {
                            val body = responseBody.string()
                            val data = body.replace(")]}'", "").trimStart()
                            val rootArray = JSONArray(data)
                            val innerArray = JSONArray(rootArray.getJSONArray(0).getString(2))
                            val suggestions = mutableListOf<String>()
                            val suggestionArray = innerArray.getJSONArray(1)
                            for (i in 0 until suggestionArray.length()) {
                                val item = suggestionArray.getJSONArray(i)
                                val keyword = item.getString(0)
                                suggestions.add(keyword)
                            }
                            if (suggestions.isNotEmpty()) {
                                val temp = suggestions.shuffled().take(15)
                                fetched_queries_GoogleTrends = fetched_queries_GoogleTrends + temp
                                // Log.i("DEBUG GOOGLE TREND", "${fetched_queries_GoogleTrends}")
                                Log.i("DEBUG GOOGLE TREND", "Fetched data from Google Trends successfully!")
                            }
                            else {
                                Log.i("DEBUG GOOGLE TREND", "Unknows errors occurred!")
                            }
                        }
                        else {
                            Log.e("DEBUG GOOGLE TREND", "HTTP ${response.code()}: ${response.message()}")
                        }
                    }
                }
            })
        }
    }
    private fun fetchReddit() {
        getRandomUserAgent { user_Agent ->
            Log.d("DEBUG REDDIT", "Proceeding with User-Agent: $user_Agent")
            val subReddits = listOf("news", "worldnews", "life", "todayilearned", "askreddit", "technology", "medical","food", "government", "education", "history", "culture", "money")
            val randomSubreddit = subReddits.random()
            val request = Request.Builder()
                .url("https://www.reddit.com/r/$randomSubreddit/hot.json?limit=${(10..15).random()}")
                .header("User-Agent", user_Agent)
                .build()
            val client = OkHttpClient()
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("DEBUG REDDIT", "Request failed", e)
                }
                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    response.body()?.use { responseBody ->
                        if (response.isSuccessful) {
                            val body = responseBody.string()
                            // Log.d("DEBUG REDDIT", "Response body: ${body.take(500)}")
                            val json = JSONObject(body)
                            val array_size = json.getJSONObject("data").getJSONArray("children").length()
                            if (array_size > 0) {
                                Log.i("DEBUG REDDIT", "Fetched data from Reddit successfully!")
                                for (i in 0 until array_size) {
                                    val title = json.getJSONObject("data").getJSONArray("children").getJSONObject(i).getJSONObject("data").getString("title")
                                    fetched_queries_Reddit = fetched_queries_Reddit + title
                                }
                                // Log.i("DEBUG REDDIT", "${fetched_queries_Reddit}")
                            }
                            else {
                                Log.i("DEBUG REDDIT", "Unknows errors occurred!")
                            }
                        } else {
                            Log.e("DEBUG REDDIT", "HTTP ${response.code()}: ${response.message()}")
                        }
                    }
                }
            })
        }
    }
    private fun fetchLocalQueries() {
        var allQueries = readLocalQueries()
        if (allQueries.isEmpty()) {
            Log.i("DEBUG", "No local queries found")
            Toast.makeText(this, getString(R.string.noqueries_found), Toast.LENGTH_SHORT).show()
            return
        }
        Log.i("DEBUG LOCAL QUERIES", "Fetched data from local queries successfully!")
        allQueries = allQueries.shuffled()
        local_queries = allQueries
        // Log.i("DEBUG LOCAL QUERIES", "${allQueries}")
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
                            val androidVersion = (10..14).random()
                            val systemComponent = "Linux; Android ${androidVersion}; K"
                            val shuffledVersions = chromeVersions.shuffled()
                            val chromeMajorVersion = shuffledVersions[0].split('.')[0]
                            val chromeReducedVersion = "$chromeMajorVersion.0.0.0"
                            finalUserAgent = "Mozilla/5.0 ($systemComponent) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/$chromeReducedVersion Safari/537.36"
//                            Log.i("DEBUG", "User agent fetched: $finalUserAgent")
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
    private fun setAppLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun ScrollJS() {
        val js = """
            (function () {
                function random(min, max) {
                    return Math.floor(Math.random() * (max - min + 1)) + min;
                }
                let scrollTimes = random(0, 5);
                let count = 0;
                function limitedScroll() {
                    if (count >= scrollTimes) {
                        return; 
                    }
                    window.scrollBy({
                        top: random(150, 600),
                        behavior: 'smooth'
                    });
                    count++;
                    setTimeout(
                        limitedScroll,
                        random(1000, 1500) 
                    );
                }
                setTimeout(limitedScroll, random(1000, 1500));
            })();
            """.trimIndent()
        webView.evaluateJavascript(js, null)
    }

    private fun setLocale(langCode: String) {
        val locale = Locale(langCode)
        Locale.setDefault(locale)
        val resources = resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)
        prefs.edit().putString("My_Lang", langCode).apply()
        recreate()
    }

    private fun loadLocale() {
        val prefs = getSharedPreferences("Settings", MODE_PRIVATE)
        val lang = prefs.getString("My_Lang", "")
        if (!lang.isNullOrEmpty()) {
            val locale = Locale(lang)
            Locale.setDefault(locale)
            val config = resources.configuration
            config.setLocale(locale)
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }
}