# IOT_TLS

參考Android Document 實作 TLS連線
https://developer.android.com/training/articles/security-ssl.html
目前使用 self-signed certificate 認證
最後會使用雙向認證

problem1 : 

尚未申請憑證
所以有hostname verified 問題
目前使用
http://stackoverflow.com/questions/31917988/okhttp-javax-net-ssl-sslpeerunverifiedexception-hostname-domain-com-not-verifie
解決



---------------------------------------------------
MAC server 設置 TLS連線  &  self-signed certificate
https://getgrav.org/blog/macos-sierra-apache-ssl

