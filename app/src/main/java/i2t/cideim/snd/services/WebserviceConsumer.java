package i2t.cideim.snd.services;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import i2t.cideim.dto.Constants;
import i2t.cideim.dto.Document;
import i2t.cideim.dto.Usuario;

public class WebserviceConsumer {
    public static final String GET_USER_BY_CEDULA = "https://i2thub.icesi.edu.co:5443/guaralrpc/user/getbycedula/";
    public static final String POSR_USER = "https://i2thub.icesi.edu.co:5443/guaralrpc/user/";
    public static final String GET_DOCLIST_BY_CEDULA = "https://i2thub.icesi.edu.co:5443/guaralrpc/document/list/usercedula/";
    public static final String POST_DOCUMENT = "https://i2thub.icesi.edu.co:5443/guaralrpc/document/";


    public interface ServerResponseReceiver {
        void onServerResponse(Object json);

    }

    public static class GetUserByCedula extends Thread {
        private String identificacion;
        private Gson gson;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public GetUserByCedula(String identificacion) {
            this.identificacion = identificacion;
            gson = new Gson();
            util = new HTTPSWebUtilDomi();
        }

        @Override
        public void run() {
            try {
                String json = util.GETrequest(GET_USER_BY_CEDULA + identificacion);
                if (json != null) {
                    Usuario usuario = gson.fromJson(json, Usuario.class);
                    observer.onServerResponse(usuario);
                } else {
                    observer.onServerResponse(null);
                }
            } catch (IOException ex) {
                observer.onServerResponse(Constants.IOEX);
            }

        }
    }

    public static class PostUserByCedula extends Thread {
        private String data;
        private Gson gson;
        private String cedula;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public PostUserByCedula(String data, String cedula) {
            this.data = data;
            this.cedula = cedula;
            gson = new Gson();
            util = new HTTPSWebUtilDomi();
        }

        @Override
        public void run() {
            try {
                String json = util.POSTrequest(POSR_USER + cedula, data);
                observer.onServerResponse(Constants.OK);
            } catch (IOException ex) {
                observer.onServerResponse(Constants.IOEX);
            }
        }


    }

    public static class GetDocumentListOfUserByCedula extends Thread {
        private String identificacion;
        private Gson gson;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public GetDocumentListOfUserByCedula(String identificacion) {
            this.identificacion = identificacion;
            gson = new Gson();
            util = new HTTPSWebUtilDomi();
        }

        @Override
        public void run() {
            try {
                String json = util.GETrequest(GET_DOCLIST_BY_CEDULA + identificacion);
                Document[] docs = gson.fromJson(json, Document[].class);
                observer.onServerResponse(docs);
            } catch (IOException ex) {
                observer.onServerResponse(Constants.IOEX);
            }
        }
    }

    public static class PostDocument extends Thread {
        private String data;
        private Gson gson;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public PostDocument(String data) {
            this.data = data;
            gson = new Gson();
            util = new HTTPSWebUtilDomi();
        }

        @Override
        public void run() {
            try {
                String json = util.POSTrequest(POST_DOCUMENT, data);
                Log.e("Debug", "JSON: " + json);
                observer.onServerResponse(json);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public static class PostListDocument extends Thread {
        private ArrayList<Document> documents;
        private Gson gson;
        private HTTPSWebUtilDomi util;

        ServerResponseReceiver observer;

        public void setObserver(ServerResponseReceiver observer) {
            this.observer = observer;
        }

        public PostListDocument(ArrayList<Document> documents) {
            this.documents = documents;
            gson = new Gson();
            util = new HTTPSWebUtilDomi();
        }

        @Override
        public void run() {
            try {
                for(int i=0 ; i<documents.size() ; i++) {
                    String json = util.POSTrequest(POST_DOCUMENT+"a", gson.toJson(documents.get(i)));
                    observer.onServerResponse(documents.get(i));
                }
                observer.onServerResponse(Constants.OK);
            } catch (IOException ex) {
                observer.onServerResponse(Constants.IOEX);
                System.out.println(ex.getMessage());
            }
        }
    }
}
