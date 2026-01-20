# REWARD AUTOSEARCHER

(Vietnamese | [English](README_en.md))

Reward Autosearcher là một ứng dụng Android được phát triển bằng ngôn ngữ lập trình Kotlin, giúp người dùng tự động hóa quá trình tìm kiếm trên Bing để tích lũy điểm Microsoft Rewards một cách hiệu quả và tiết kiệm thời gian.

## ✨ Tính năng nổi bật

* Tự động tìm kiếm (Auto Search): Thực hiện hàng loạt các truy vấn tìm kiếm tự động mà không cần thao tác thủ công.

**Đa dạng nguồn từ khóa (Query Sources):**

* Reddit: Tự động lấy tiêu đề bài viết mới nhất từ các subreddit (News, Technology, v.v.).
* Local Data: Sử dụng danh sách từ khóa tích hợp sẵn (queries.json) với nhiều chủ đề đa dạng.
* Google Trends / Wikipedia: Lấy từ khóa đang thịnh hành trên thế giới (random theo từng quốc gia).

## Giả lập User-Agent thông minh:

* Tự động gọi API để lấy phiên bản Chrome mới nhất.
* Giả lập User-Agent để tránh bị phát hiện.

## Tùy chỉnh linh hoạt:

* Thiết lập số lượng tìm kiếm mong muốn.
* Cài đặt độ trễ (Delay) giữa các lần tìm kiếm để tránh bị phát hiện spam, mô phỏng quá triình tìm kiếm như người thật.
* Quá trình lấy nguồn tìm kiếm sử dụng random User-Agent tránh bị phát hiện.
* Quản lý tài khoản: Tích hợp WebView để đăng nhập và kiểm tra trạng thái tài khoản Microsoft Rewards trực tiếp.
* Tiện ích: Chế độ giữ màn hình luôn sáng (Keep Screen On) khi đang chạy tác vụ, có thể chạy ngầm (Background).
* Có thể cuộn với độ dài và số lần random giống như người thật.

## 🛠️ Công nghệ sử dụng

|Công nghệ|Mô tả|
|-|-|
|Ngôn ngữ|Kotlin|
|Networking|Retrofit \& OkHttp|
|JSON Parsing|Kotlinx Serialization \& Gson|
|UI Components|Android Views (XML), Material Design|
|Build System|Gradle (Kotlin DSL)|

## 📂 Cấu trúc dự án

**Dưới đây là các tệp tin quan trọng trong mã nguồn:**

* MainActivity.kt: Chứa logic chính, xử lý UI và luồng chạy tự động.
* RetrofitClient\_getChromeVersion.kt: Cấu hình API client để lấy dữ liệu version trình duyệt.
* localQueryDataClass.kt \& chromeVersionResponse.kt: Các Data Class mô hình hóa dữ liệu JSON.
* assets/queries.json: Kho dữ liệu từ khóa tìm kiếm ngoại tuyến.
* res/layout/activity\_main.xml: Giao diện người dùng chính.

## 🚀 Cài đặt và Chạy ứng dụng

**Để chạy ứng dụng này, hãy làm theo các bước sau:**

### Yêu cầu:

* Điện thoại sử dụng hệ điều hành Android (Android 9.0 hoặc cao hơn).

## Các bước thực hiện:

1. Mở ứng dụng **Reward Autosearcher** trên điện thoại.
2. Bấm vào **Login** để đăng nhập (nếu chưa).
3. Nhập số lượng tìm kiếm và độ trễ mong muốn.
4. Chọn source cần sử dụng (Reddit, Google Trends, Wikipedia, Newspaper), có thể để trống tất cả.
5. Nhấn nút **Start** để bắt đầu.

# ⚠️ Lưu ý

Ứng dụng này được phát triển cho mục đích học tập và nghiên cứu về lập trình Android, xử lý mạng (Networking) và tự động hóa tác vụ. Việc sử dụng công cụ tự động có thể vi phạm các điều khoản của [**Microsoft Rewards**](https://www.microsoft.com/vi-vn/servicesagreement?utm_source=copilot.com#13l_MicrosoftRewards). Nếu xảy ra việc tài khoản bị cấm (suspended) hoặc hạn chế tạm thời (restricted), chúng tôi sẽ không chịu trách nhiệm cho các vấn đề xảy ra với tài khoản của bạn.

Để lấy source từ các bài báo (Newspaper) cần có API key. Hiện tại, API key được sử dụng từ nguồn của lập trình viên. Trong các phiên bản sắp tới, người dùng có thể sử dụng API key của chính mình. Số lượng querry trên mỗi API key là 100/ngày cho bản miễn phí. Nguồn: [**News API**](https://newsapi.org/)

# Developed by \[[thaikhang113](https://github.com/thaikhang113)/[Tinkismee](https://github.com/tinkismeeee)]

