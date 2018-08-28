import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.security.interfaces.ECKey;
import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        final String request = "https://hh.ru/vacancy/27297035?query=java%20junior";
        try {
            ArrayList<String> links = new ArrayList<>();
            Document doc = Jsoup.connect(request)
                    .userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36")
                    .referrer("http://www.google.com")
                    .get();
            Elements elements = doc.select("div[class = g-user-content ]");
            elements = elements.select("ul:gt(1)");
            Element element = elements.first();
            elements = element.select("li");
            System.out.println("------------------ELEMENT-------------------------");
            System.out.println(element);
            System.out.println("-------------------ELEMENTS------------------------");
            for (Element el :elements)
                System.out.println(el);

        }catch (Exception e){e.printStackTrace();}
    }

}

