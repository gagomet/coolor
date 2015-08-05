package coolor.translate;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by KAKolesnikov on 2015-08-05.
 */
public class CurrencyTranslator {

    private UserProxy userProxy;

    private ResourceBundle bundle = ResourceBundle.getBundle("application");
    private static final Logger log = Logger.getLogger(CurrencyTranslator.class);
    private Float euroCourse;
    private Float usdCourse;


    public CurrencyTranslator() {
        translateCourses();
    }

    public CurrencyTranslator(UserProxy userProxy) {
        this.userProxy = userProxy;
        translateCourses();
    }

    public Float getEuroCourse() {
        return euroCourse;
    }

    public Float getUsdCourse() {
        return usdCourse;
    }

    public void translateCourses() {
        Document companyPageDocument;
        List<String> coursesList = new ArrayList<>();
        try {
            if (userProxy == null) {
                companyPageDocument = Jsoup.connect(bundle.getString("site")).get();
            } else {
                String tmp = getHtmlInString(bundle.getString("site"), userProxy);
                companyPageDocument = Jsoup.parse(tmp);
            }
            Elements courses = companyPageDocument.getElementsByClass(bundle.getString("class.on.site"));
            for (Element course : courses) {
                for (Element child : course.children()) {
                    if (child.tagName().equals("p")) {
                        coursesList.add(child.ownText());
                    }
                }
            }
            parseTextFromSite(coursesList);
        } catch(IOException e) {
            e.printStackTrace();
            //TODO catch proxy exception java.net.ConnectException
        }
    }

    protected String getHtmlInString(String url, UserProxy userProxy) {
        StringBuffer tmp = new StringBuffer();
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress(userProxy.getProxyHost(), userProxy.getProxyPort()));
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection(proxy);
            connection.connect();
            String line = null;
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = in.readLine()) != null) {
                tmp.append(line);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return String.valueOf(tmp);
    }

    private void parseTextFromSite(List<String> strings){
        List<String> courses = new ArrayList<>();
        for (String string : strings) {
            String[] rest = string.split("=");
            String[] value = rest[1].split(" ");
            courses.add(value[1]);
        }
        this.euroCourse = Float.parseFloat(courses.get(0));
        this.usdCourse = Float.parseFloat(courses.get(1));
    }
}
