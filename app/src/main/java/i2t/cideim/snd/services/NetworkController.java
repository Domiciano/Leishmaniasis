package i2t.cideim.snd.services;


import i2t.cideim.model.DataXml;
import i2t.cideim.xml.IOXmlFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.SerializableEntity;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.util.Log;

/**
 * Created by Juan.
 */
public class NetworkController {

    private NetworkUtilities networkUtilities;
    private List<String> contentStrings;
    private String authContentString;
    private List<String> logsResponse;
    private Calendar calendar;
    private SimpleDateFormat sdf;

    /**
     * Constructor del controlador de sincronización
     */
    public NetworkController() {
        networkUtilities = new NetworkUtilities();
        contentStrings = new ArrayList<String>();
        logsResponse = new ArrayList<String>();
        authContentString = "";
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("HH:mm:ss");
    }

    /**
     * Envía cada XML uno a uno a la URL especificada.
     *
     * @throws ClientProtocolException
     * @throws IOException
     * @throws AuthenticationException
     * @throws SynchronizationException
     */
    public void syncContent() throws ClientProtocolException, IOException, AuthenticationException, SynchronizationException {
        HttpResponse responseHTTP = networkUtilities.HttpPost(authContentString);
        int code = responseHTTP.getStatusLine().getStatusCode();
        String respuesta = EntityUtils.toString(responseHTTP.getEntity());
        if (code == 200) {
            logsResponse.add(sdf.format(calendar.getTime()) + " [" + respuesta + "]");
            if (respuesta.contains("&lt;url&gt;") == true && respuesta.contains("&lt;/url&gt;") == true) {

                String url = respuesta.substring(respuesta.indexOf("&lt;url&gt;"), respuesta.indexOf("&lt;/url&gt;")).replace("&lt;url&gt;", "");
                networkUtilities.setStringURL(url);

                for (String content : contentStrings) {

                    responseHTTP = networkUtilities.HttpPost(content);
                    code = responseHTTP.getStatusLine().getStatusCode();
                    respuesta = networkUtilities.toStringResponse(responseHTTP);
                    logsResponse.add(sdf.format(calendar.getTime()) + " [" + respuesta + "]");
                    Log.d("SND response", respuesta);
                    if (code != 200)
                        throw new SynchronizationException("Ha ocurrido un error en la sincronización");
                }
            } else {
                throw new AuthenticationException("El usuario no ha superado el proceso de autenticación:\n\n" + respuesta);
            }
        } else {
            throw new SynchronizationException("Ha ocurrido un error en la sincronización:\n\n" + respuesta);
        }
    }

    public String SummarySyncResponses() {
        int ok = 0, bad = 0;
        int senden = contentStrings.size() + 1;
        String total = "";
        for (String respuestas : getLogsResponse()) {
            if (respuestas.contains("True") || respuestas.contains("true") || respuestas.contains("url"))
                ok++;
            else
                bad++;
        }

        String message = ok == senden ? ok + " de " + senden + " enviado(s)" : "No se enviaron " + bad + " registros";

        total = "La sincronización ha finalizado:\n " + message;

        return total;
    }

    /**
     * Sincronización de objetos apartir de serialización.
     *
     * @param data objeto DataXml serializado a envíar.
     * @throws IOException
     */
    public void synContent(DataXml data) throws IOException {
        HttpEntity entity = new SerializableEntity(data, true);
        networkUtilities.HttpPost(entity);
    }

    /**
     * Toma un objeto DataXml y lo añade a la lista interna de XML a enviar cuando se realice la sincronización.
     *
     * @param data    Objeto DataXml a enviar.
     * @param context Clase que representa el contexto de la aplicación
     * @throws Exception
     */
    public void parseToXml(DataXml data, Context context) throws Exception {

        String filename = UUID.randomUUID().toString();
        String content = "TEMP";

        FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        outputStream.write(content.getBytes());
        outputStream.close();

        File file = new File(context.getFilesDir().getAbsolutePath(), filename);

        if (file.exists()) {
            try {
                IOXmlFile io = new IOXmlFile();
                io.WriteFileXml(data, file);
                content = getFileContents(file);
                if (content.contains("LiderComunitarioXml"))
                    authContentString = content;
                else
                    contentStrings.add(content);
            } finally {
                file.delete();
            }
        }
    }

    /**
     * Lee el archivo recibido por parametros, contruye una cadena y elimina ese archivo.
     *
     * @param file <span/> archivo a leer y eliminar.
     * @return cadena que representa el contenido del archivo.
     * @throws IOException
     */
    private String getFileContents(final File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                inputStream));
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

    public List<String> getLogsResponse() {
        return logsResponse;
    }
}
