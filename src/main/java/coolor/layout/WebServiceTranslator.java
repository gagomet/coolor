package coolor.layout;

import coolor.translate.UserProxy;


import java.awt.Desktop;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by KAKolesnikov on 2015-08-12.
 */
public class WebServiceTranslator {

    private Proxy proxy;
    private UserProxy userProxy;

    public WebServiceTranslator() {
    }

    public void translate(MediaCanvas canvas, boolean isRaw){
        StringBuilder requestToApi = new StringBuilder();
        requestToApi.append("bins=0:100:");
    }

    public void translateToPreview() {
        HttpURLConnection conn = prepareConnection("http://www.packit4me.com/api/call/preview");
        try {
            assert conn != null;
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            String params = "bins=0:50:160x1000&items=1:0:1:110x100,2:0:1:70x220,3:0:1:70x220&binId=0";

            osw.write(params);
            osw.flush();
            osw.close();

            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
//            Path directory = Files.createTempDirectory("javatmp");
//            File file = Files.createTempFile(directory, "temp", ".html").toFile();
            File file1 = new File(System.getProperty("user.dir")+"\\temp.html");
            FileWriter writer = new FileWriter(file1);
            BufferedWriter out = new BufferedWriter(writer);
            System.out.println(file1.getAbsolutePath());
            String line;
            while ((line = rd.readLine()) != null) {
                if (line.contains("<body>")) {
                    out.write("<body>");
                    out.write("<h3>Scroll the mouse please</h3>");
                    out.write(
                            "<h3>Use mouse scroll to zoom in/out. Hold RMB and drag mouse to navigate on length of canvas</h3>");
                }
                if (line.contains("three.min.js")) {
                    out.write(getScriptText("/js/three.min.js"));
                } else if (line.contains("TrackballControls.js")) {
                    out.write(getScriptText("/js/TrackballControls.js"));
                } else if (line.contains("Detector.js")) {
                    out.write(getScriptText("/js/Detector.js"));
                } else if (line.contains("stats.min.js")) {
                    out.write(getScriptText("/js/stats.min.js"));
                } else if (line.contains("button")) {
                    System.out.println("button dawn");
                } else {
                    out.write(line);
                    out.write("\n");
                }
            }
            rd.close();
            out.close();
            writer.close();
            Desktop.getDesktop().browse(file1.toURI());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String translateRawJson(){
        HttpURLConnection conn = prepareConnection("http://www.packit4me.com/api/call/raw");
        StringBuilder builder = new StringBuilder();
        try {
            assert conn != null;
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

            String params = "bins=0:50:160x1000&items=1:0:1:110x100,2:0:1:70x220,3:0:1:70x220&binId=0";

            osw.write(params);
            osw.flush();
            osw.close();

            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                    builder.append(line);
                    builder.append("\n");
                }

            rd.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    private String getScriptText(String scriptUrl) {
        StringBuilder sb = new StringBuilder();
        try {
            Path resourcePath = Paths.get(this.getClass().getResource(scriptUrl).toURI());
            sb.append("<script type=\"text/javascript\">")
              .append(new String(Files.readAllBytes(resourcePath)))
              .append("</script>");

            sb.append("\n");
        } catch(URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private HttpURLConnection prepareConnection(String urlToApi){
        HttpURLConnection conn = null;
        if (userProxy != null) {
            try {
                proxy = new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(userProxy.getProxyHost(), userProxy.getProxyPort()));
                URL addr = new URL(urlToApi);
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
                URL addr = new URL(urlToApi);
                conn = (HttpURLConnection) addr.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setReadTimeout(30000);
                conn.connect();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }
}
