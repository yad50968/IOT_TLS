# IOT_TLS

參考Android Document 實作 server client連線 via TLS1.2</br>
https://developer.android.com/training/articles/security-ssl.html</br>
目前使用 self-signed certificate 認證
最後會使用雙向認證

</br>
</br>
problem1 : 
尚未申請憑證
所以有hostname verified 問題

目前使用
http://stackoverflow.com/questions/31917988/okhttp-javax-net-ssl-sslpeerunverifiedexception-hostname-domain-com-not-verifie
解決



---------------------------------------------------
MAC server 設置 TLS連線  &  self-signed certificate</br>
https://getgrav.org/blog/macos-sierra-apache-ssl


</br>
設置apache 只接受某些tls版本連線</br>
http://tecadmin.net/enable-tls-in-modssl-and-apache/
