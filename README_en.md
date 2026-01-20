# REWARD AUTOSEARCHER

Reward Autosearcher is an Android application developed in Kotlin, designed to automate Bing searches to efficiently accumulate Microsoft Rewards points and save time.
## ✨ Key Features

- Auto Search: Perform multiple automated search queries without manual interaction.

**Diverse Query Sources:**

- Reddit: Automatically fetch the latest post titles from subreddits (News, Technology, etc.).

- Local Data: Use a built-in keyword list `(queries.json)` with various topics.

- Google Trends / Wikipedia: Retrieve trending keywords worldwide (randomized by country).

## Smart User-Agent Simulation:

- Automatically call API to get the latest Chrome version.

- Simulate User-Agent to avoid detection.

## Flexible Customization:

- Configure the number of searches

- Set delay between searches to mimic human-like behavior and avoid spam detection.

- Randomize User-Agent during query sourcing to reduce detection risk.

- Account Management: Integrated WebView for login and checking Microsoft Rewards account status.

- Utilities: Keep screen on while running tasks, support background execution.
- Human-like scrolling.

## 🛠️ Technologies Used

| Technology    |Description|
|---------------|---|
| Language      |Kotlin|
| Networking    |Retrofit & OkHttp|
| JSON Parsing  |Kotlinx Serialization & Gson|
| UI Components |Android Views (XML), Material Design|
| Build System  |Gradle (Kotlin DSL)|

## 📂 Project Structure

*Important source files include:**

- `MainActivity.kt`: Core logic, UI handling, and automation flow.

- `RetrofitClient_getChromeVersion.kt`: API client configuration to fetch browser version data.

- `localQueryDataClass.kt` & `chromeVersionResponse.kt`: Data classes modeling JSON data.

- `assets/queries.json`: Offline keyword dataset.

- `res/layout/activity_main.xml`: Main user interface layout.

## 🚀 Installation & Usage

### Requirements:

- Android device running Android 9.0 or higher.

## Steps:

1. Open the **Reward Autosearcher** app on your device.

2. Tap **Login** to sign in (if not already).

3. Enter the desired number of searches and delay.

4. Choose query sources (Reddit, Google Trends, Wikipedia, Newspaper) — optional.

5. Press **Start** to begin.

# ⚠️ Disclaimer

This application is developed for educational and research purposes in Android development, networking, and task automation. Using automated tools may violate **[Microsoft Rewards](https://www.microsoft.com/vi-vn/servicesagreement?utm_source=copilot.com#13l_MicrosoftRewards)** terms of service. If your account is suspended or restricted, the developers are not responsible.

To fetch queries from Newspaper sources, an API key is required. Currently, the API key is provided by the developer. In future versions, users may use their own API key. Free API keys allow up to 100 queries/day. Source: **[News API](https://newsapi.org/)**

# Developed by [[thaikhang113](https://github.com/thaikhang113)/[Tinkismee](https://github.com/tinkismeeee)]
