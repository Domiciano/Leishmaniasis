package i2t.cideim.snd.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;


/**
 * Created by Juan.
 * Performs the network operations needed to perform the synchronization.
 */
public class NetworkUtilities {

    // False implica la URL de pruebas (URLLocal)
    // True implica la URL de producción (URLRemote)
    private static boolean isRemote = true;

    private URL url;
    private File xmlData;
    private String URLRemote = "http://sndservices.azurewebsites.net/Modules/LeishmaniasisApp.svc/xml/Login";
    private String URLLocal = "http://192.168.160.98:56520/Modules/LeishmaniasisApp.svc/xml/Login";

    /**
     * Constructor
     */
    public NetworkUtilities() {
        try {
            if(isRemote)
                url = new URL(URLRemote);
            else
                url = new URL(URLLocal);
        } catch (MalformedURLException e) {
            Log.d(e.getLocalizedMessage(), e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @return the URL string
     */
    public String getURLString() {
        return this.url.toString();
    }

    /**
     * @param URLString
     *            the stringURL to set
     * @throws MalformedURLException
     */
    public void setStringURL(String URLString) throws MalformedURLException {
        this.url = new URL(URLString);
    }

    /**
     * @return the xmlData
     */
    public File getXmlData() {
        return xmlData;
    }

    /**
     * @param xmlData
     *            the xmlData to set
     */
    public void setXmlData(File xmlData) {
        this.xmlData = xmlData;
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public String getFileContents(final File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(	inputStream));
        final StringBuilder stringBuilder = new StringBuilder();
        boolean done = false;
        while (!done) {
            final String line = reader.readLine();
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }
        reader.close();
        inputStream.close();
        file.delete();
        return stringBuilder.toString();
    }
    /**
     *
     * @return
     * @throws IOException
     */

    public String URLConnection() throws IOException {
        java.net.URLConnection con = url.openConnection();
        con.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        String all = "";
        for (; (line = in.readLine()) != null;) {
            all += line + " ";
        }
        in.close();
        return all;
    }
    /**
     *
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResponse HttpGet() throws ClientProtocolException, IOException{
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(url.toString());
        HttpResponse httpresponse = client.execute(get);
        return httpresponse;
    }
    /**
     *
     * @param content
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResponse HttpPost(String content) throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url.toString());
        StringEntity entity = new StringEntity(content,HTTP.UTF_8);
        entity.setContentType("text/xml");
        post.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
        return response;
    }
    /**
     *
     * @param content
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public HttpResponse HttpPost(HttpEntity content) throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url.toString());
        post.setHeader("Content-Type","application/soap+xml;charset=UTF-8");
        post.setEntity(content);
        HttpResponse httpresponse = client.execute(post);
        return httpresponse;
    }

    public String toStringResponse(HttpResponse httpresponse) throws ParseException, IOException{
        return "*server status: "+httpresponse.getStatusLine()+"\n"+EntityUtils.toString(httpresponse.getEntity());
    }

}