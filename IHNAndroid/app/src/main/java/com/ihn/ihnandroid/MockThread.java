package com.ihn.ihnandroid;

import android.os.Handler;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by tong on 2015/7/28.
 * mock thread to call REST api to get google search result
 */
public class MockThread extends Thread {

    private Handler uiHandler;

    public MockThread(Handler handler){
        this.uiHandler=handler;
    }

    public void run(){
        String url = "https://ajax.googleapis.com/ajax/services/search/web?v=1.0&q={query}";
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        String result=null;
        try{
            result = restTemplate.getForObject(url, String.class, "citi");
        }catch(Throwable th){
            result=th.getMessage();
            th.printStackTrace();
        }

        uiHandler.obtainMessage(0, result).sendToTarget();
    }

}
