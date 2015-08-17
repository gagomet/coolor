package coolor.layout;

import coolor.translate.UserProxy;


import java.awt.Desktop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by KAKolesnikov on 2015-08-12.
 */
public class WebServiceTranslator {

    private Proxy proxy;
    private UserProxy userProxy;
    private String result;

    public WebServiceTranslator() {
    }

    public WebServiceTranslator(UserProxy userProxy) {
        this.userProxy = userProxy;
    }

    public String translate() {
        HttpURLConnection conn = null;
        if (userProxy != null) {
            try {
                proxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(userProxy.getProxyHost(), userProxy.getProxyPort()));
                URL addr = new URL("http://www.packit4me.com/api/call/preview");
                conn = (HttpURLConnection) addr.openConnection(proxy);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setReadTimeout(30000);
                conn.connect();
            } catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL addr = new URL("http://www.packit4me.com/api/call/preview");
                conn = (HttpURLConnection) addr.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setReadTimeout(30000);
                conn.connect();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        try {
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            String params = "bins=0:50:1600x5000&items=1:0:1:1100x1000,2:0:1:700x2200,3:0:1:700x2200&binId=0";

            osw.write(params);
            osw.flush();
            osw.close();

            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            File file = new File("C:\\sample\\temp.html");
            FileWriter writer = new FileWriter(file);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.contains("three.min.js")) {
                    System.out.println("prevented");
                    sb.append(getScriptText("/js/three.min.js"));
                    writer.write(getScriptText("/js/three.min.js"));
                } else if (line.contains("TrackballControls.js")) {
                    System.out.println("prevented2");
                    sb.append(getScriptText("/js/TrackballControls.js"));
                    writer.write(getScriptText("/js/TrackballControls.js"));
                } else if (line.contains("Detector.js")) {
                    System.out.println("prevented3");
                    sb.append(getScriptText("/js/Detector.js"));
                    writer.write(getScriptText("/js/Detector.js"));
                } else if (line.contains("stats.min.js")) {
                    System.out.println("prevented4");
                    sb.append(getScriptText("/js/stats.min.js"));
                    writer.write(getScriptText("/js/stats.min.js"));
                } else {
                    writer.write(line);
                    writer.write("\n");
                    sb.append(line);
                    sb.append("\n");
                }
            }
            rd.close();
            writer.close();

            Desktop.getDesktop().browse(file.toURI());
        } catch(IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getScriptText(String scriptUrl) {
        StringBuilder sb = new StringBuilder();
        try {
            Path resourcePath = Paths.get(this.getClass().getResource(scriptUrl).toURI());
            sb.append("<script type=\"text/javascript\" src=\"")
              .append(resourcePath)
              .append("\"></script>");
            sb.append("\n");
        } catch(URISyntaxException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
