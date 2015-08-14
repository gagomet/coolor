package coolor.layout;

import coolor.fx.MainPaneController;
import coolor.translate.UserProxy;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;

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
import java.net.URI;
import java.net.URL;

/**
 * Created by KAKolesnikov on 2015-08-12.
 */
public class WebServiceTranslator {

    private Proxy proxy;
    private UserProxy userProxy;
    private MainPaneController controller;
    private String result;

    public WebServiceTranslator() {
    }

    public WebServiceTranslator(UserProxy userProxy, MainPaneController controller) {
        this.userProxy = userProxy;
        this.controller = controller;
    }

    public void translate() {
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

            String params = "bins=0:50:1600x5000&items=0:0:1:1100x1000,1:0:1:700x2200,2:0:1:700x2200&binId=0";

            osw.write(params);
            osw.flush();
            osw.close();

            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            File file = new File("C:\\sample\\temp.html");
            FileWriter writer = new FileWriter(file);
            BufferedWriter bufWriter = new BufferedWriter(writer);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                writer.write(line);
                writer.write("\n");
                sb.append(line);
                sb.append("\n");
            }
            rd.close();
            writer.close();
            result = sb.toString();
            Desktop.getDesktop().browse(file.toURI());
            System.out.println(result);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
