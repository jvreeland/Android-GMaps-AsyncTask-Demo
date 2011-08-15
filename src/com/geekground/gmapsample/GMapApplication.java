package com.geekground.gmapsample;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.app.Application;
import android.content.Context;

public class GMapApplication extends Application {
private Context mContext;

    private HttpClient httpclient;
    
    private ClientConnectionManager mClientConnectionManager;
	
    private HttpClient createHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 5000));
        schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        mClientConnectionManager = new ThreadSafeClientConnManager(params, schReg);

        return new DefaultHttpClient(mClientConnectionManager, params);
    }
    
    public HttpClient getHttpClient() {
        if (httpclient == null) {
            httpclient = createHttpClient();
        }
        return httpclient;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        httpclient = createHttpClient();
        mContext = getApplicationContext();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        shutdownHttpClient();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        shutdownHttpClient();
    }
    private void shutdownHttpClient() {
        if (httpclient != null && mClientConnectionManager != null) {
            mClientConnectionManager.shutdown();
            httpclient = null;
        }
    }
    
    public String getHostAddress() {
        // Set server address
        return this.mContext.getString(R.string.uri_scheme) + "://" + this.mContext.getString(R.string.host);
    }

}
