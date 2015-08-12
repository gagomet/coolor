package coolor.layout;

import coolor.translate.UserProxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;

/**
 * Created by KAKolesnikov on 2015-08-12.
 */
public class WebServiceTranslator {

    private Proxy proxy;

    public void translate(UserProxy userProxy) {
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
                URL addr = new URL("http://www.packit4me.com/api/call/raw");
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

            String params = "bins=0:50:5x5&items=0:0:15:1x1&binId=0";

            osw.write(params);
            osw.flush();
            osw.close();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {

                sb.append(line);
            }

            rd.close();
            String result = sb.toString();
            System.out.println(result);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
