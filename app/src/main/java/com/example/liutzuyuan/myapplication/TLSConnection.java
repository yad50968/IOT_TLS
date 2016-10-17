package com.example.liutzuyuan.myapplication;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.Extension;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import static java.security.AccessController.getContext;

/**
 * Created by liutzuyuan on 2016/10/17.
 */

public class TLSConnection extends ContextWrapper implements  Runnable{


    public TLSConnection(Context base) {
        super(base);
    }

    private Certificate certificateFactory(){

        InputStream caInput = null;
        Certificate ca = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // From https://www.washington.edu/itconnect/security/ca/load-der.crt

            caInput = getAssets().open("server.crt");

            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

        } catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                caInput.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return ca;
    }

    private KeyStore keyStore(Certificate ca) {
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return keyStore;
    }

    private SSLContext sslcontextOntrustManager(KeyStore keyStore){

        SSLContext context = null;
        try{
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            context = SSLContext.getInstance("TLSv1.2");
            context.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
        }catch (Exception e){
            e.printStackTrace();
        }

        return context;
    }

    private HttpsURLConnection constructHttpsConnection(SSLContext sslContext, HostnameVerifier hostnameVerifier){

        HttpsURLConnection urlConnection = null;
        try {
            URL url = new URL("https://140.119.164.35");
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setHostnameVerifier(hostnameVerifier);
            urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

        } catch (Exception e){
            e.printStackTrace();
        }
        return  urlConnection;
    }


    private int getResponseCode(HttpsURLConnection urlConnection) {

        int code = 0;
        try {
            code = urlConnection.getResponseCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  code;
    }

    private void printHeader(HttpsURLConnection urlConnection){
        Map<String, List<String>> map = urlConnection.getHeaderFields();
        System.out.println("Header : ");
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey() + " ,Value : " + entry.getValue());
        }
    }

    private String getDataFromServer(HttpsURLConnection urlConnection){
        String myString = null;
        try {
            InputStream in = urlConnection.getInputStream();
            myString = IOUtils.toString(in, "UTF-8");
            System.out.println(in);
        }catch (Exception e){
            e.printStackTrace();
        }
        return myString.toString();
    }

    @Override
    public void run() {

        Certificate ca = certificateFactory();

        // Create a KeyStore containing our trusted CAs
        KeyStore ks = keyStore(ca);


        // Create a TrustManager that trusts the CAs in our KeyStor
        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = sslcontextOntrustManager(ks);


        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                HostnameVerifier hv =
                        HttpsURLConnection.getDefaultHostnameVerifier();
                //return hv.verify("IP/Domain Name", session);
                return true;
            }
        };

        HttpsURLConnection urlConnection = constructHttpsConnection(sslContext,hostnameVerifier);
        System.out.print("fff" + getDataFromServer(urlConnection));










    }
}
