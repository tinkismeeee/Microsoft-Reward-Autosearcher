# REWARD AUTOSEARCHER

Reward Autosearcher là một ứng dụng Android được phát triển bằng Kotlin, giúp người dùng tự động hóa quá trình tìm kiếm trên Bing để tích lũy điểm Microsoft Rewards một cách hiệu quả và tiết kiệm thời gian.

## ✨ Tính năng nổi bật

- Tự động tìm kiếm (Auto Search): Thực hiện hàng loạt các truy vấn tìm kiếm tự động mà không cần thao tác thủ công.

**Đa dạng nguồn từ khóa (Query Sources):**

- Reddit: Tự động lấy tiêu đề bài viết mới nhất từ các subreddit (News, Technology, v.v.).

- Local Data: Sử dụng danh sách từ khóa tích hợp sẵn (queries.json) với nhiều chủ đề đa dạng.

- Google Trends / Wikipedia: (Tùy chọn) Lấy từ khóa đang thịnh hành.

## Giả lập User-Agent thông minh:

- Tự động gọi API để lấy phiên bản Chrome mới nhất.

- Giả lập trình duyệt PC (Windows 10) để tối ưu hóa điểm thưởng tìm kiếm trên máy tính.

## Tùy chỉnh linh hoạt:

- Thiết lập số lượng tìm kiếm mong muốn.

- Cài đặt độ trễ (Delay) giữa các lần tìm kiếm để tránh bị phát hiện spam.

- Quản lý tài khoản: Tích hợp WebView để đăng nhập và kiểm tra trạng thái tài khoản Microsoft Rewards trực tiếp.

- Tiện ích: Chế độ giữ màn hình luôn sáng (Keep Screen On) khi đang chạy tác vụ.

## 🛠️ Công nghệ sử dụng

- Dự án được xây dựng dựa trên các công nghệ Android hiện đại:

|Công nghệ|Mô tả|
|---|---|
|Ngôn ngữ|Kotlin|
|Networking|Retrofit & OkHttp|
|JSON Parsing|Kotlinx Serialization & Gson|
|UI Components|Android Views (XML), Material Design|
|Build System|Gradle (Kotlin DSL)|

## 📂 Cấu trúc dự án

**Dưới đây là các tệp tin quan trọng trong mã nguồn:**

- MainActivity.kt: Chứa logic chính, xử lý UI và luồng chạy tự động.

- RetrofitClient_getChromeVersion.kt: Cấu hình API client để lấy dữ liệu version trình duyệt.

- localQueryDataClass.kt & chromeVersionResponse.kt: Các Data Class mô hình hóa dữ liệu JSON.

- assets/queries.json: Kho dữ liệu từ khóa tìm kiếm ngoại tuyến.

- res/layout/activity_main.xml: Giao diện người dùng chính.

## 🚀 Cài đặt và Chạy ứng dụng

**Để chạy dự án này trên máy cục bộ, hãy làm theo các bước sau:**

### Yêu cầu:

- Android Studio (phiên bản mới nhất).

- JDK 11 trở lên.

## Các bước thực hiện:

1. Mở Android Studio và chọn Open.

2. Điều hướng đến thư mục chứa dự án android.

3. Đợi Gradle đồng bộ hóa (Sync) các thư viện cần thiết.

4. Kết nối thiết bị Android (hoặc bật Emulator).

5. Nhấn nút Run (▶️) để cài đặt ứng dụng.

# ⚠️ Lưu ý

Ứng dụng này được phát triển cho mục đích học tập và nghiên cứu về lập trình Android, xử lý mạng (Networking) và tự động hóa tác vụ. Việc sử dụng các công cụ tự động có thể vi phạm điều khoản dịch vụ của một số nền tảng.

# Developed by [[thaikhang113](https://github.com/thaikhang113)/[Tinkismee](https://github.com/tinkismeeee)]
