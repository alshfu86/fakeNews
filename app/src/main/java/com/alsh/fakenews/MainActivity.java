package com.alsh.fakenews;

import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView myWebView = (WebView) findViewById(R.id.webview);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Document doc;

        try {
            doc = Jsoup.connect("http://www.gp.se")
                    .userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Mobile Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();


            Elements link = doc.head().getElementsByTag("link");
            Elements images = doc.getElementsByTag("img");
            Elements logo = doc.getElementsByClass("icon icon--brand");
           // Elements script = doc.head().getElementsByTag("script");
            doc.head().getElementsByTag("link").remove();
            //doc.head().remove();
            doc.getElementsByTag("noscript").remove();
            doc.getElementById("stampenCookieInformationContainer").remove();
            doc.head().getElementsByTag("script").remove();
            //System.out.println(doc);

//            doc.select("div.container-max clearfix").remove();
//            doc.select("navbar-toggleable").remove();
//            doc.select("icon icon--brand").remove();

            doc.getElementsByClass("container-max clearfix").remove();
            doc.getElementsByClass("scaler burt-unit").remove();
            doc.getElementsByClass("row adform element-ad ad panorama").remove();

            doc.getElementsByTag("body").attr("style","pointer-events: none;\n" +
                    "   cursor: default;");

            for (Element image : images) {
                if(!image.attr("src").contains("http:")){
                    String relHref = image.attr("src");
                    relHref="http://gp.se"+relHref;
                    String newRelHref=relHref.replace("amp;", "");
                   // doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", relHref);
                    image.removeAttr("src");
                    image.removeAttr("srcset");
                    image.attr("src", newRelHref);

                    //System.out.println("IMAGE=> "+ image);

                }
               //System.out.println(image);
            }

            for (Element element : link) {
                if(element.attr("type").equals("text/css")){
                    String relHref = element.attr("href");
                    relHref="http://gp.se"+relHref;
                    doc.head().appendElement("link").attr("rel", "stylesheet").attr("type", "text/css").attr("href", relHref);
                   // Log.d("HTML", relHref);

                }
            }


            // String htmldata = doc.outerHtml();
            //Element link = doc.select("a").first();
            //doc.select("#mp-itn b a").first();
            System.out.println(doc.html());
            myWebView.loadDataWithBaseURL(null,doc.html(),"text/html","UTF-8",null);

        } catch (IOException e) {
            e.printStackTrace();
        }

        // disable scroll on touch
        myWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });


//        WebSettings settings = myWebView.getSettings();
//        settings.setDefaultTextEncodingName("utf-8");
//        settings.setLoadWithOverviewMode(true);
//        settings.setBuiltInZoomControls(true);
//        myWebView.setFocusableInTouchMode(false);
//        myWebView.setFocusable(false);
//        myWebView.setClickable(false);
//        myWebView.loadUrl("http://www.gp.se");
    }
}
