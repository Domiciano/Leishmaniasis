package i2t.cideim.leishmaniasis;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import i2t.cideim.custom.BroadcastConstants;
import i2t.cideim.model.Evaluation;
import i2t.cideim.model.LiderComunitario;
import i2t.cideim.model.Patient;
import i2t.cideim.snd.services.NetworkController;

import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by Juan.
 */

public class SyncService extends Service {

    public SyncService() {
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        LiderComunitario lider = (LiderComunitario) (intent.getSerializableExtra("user"));
        /* TEST
        LiderComunitario lider = new LiderComunitario("1143924795", "Juan David", "Arango Paredes", 'M');
        for (int i = 0; i < 30; i++) {
            Patient tempPa = new Patient("123456" + i, "Pepito", "Peréz", "CC", 'M', "Calle 1" + i + "C #45-43", "+5724656987", "18/12/19" + i, "Nariño", "Tumaco", "Una", "Sutanita", "Perez", "+572654987", "Calle 3C #54-43", 5);
            tempPa.addEvaluation(new Evaluation(true, true, true, false, false, false, false, false, false, false, true, i + "/12/2014"));
            lider.addPatient(tempPa);
        }*/
        if (lider != null) {
            startSync(lider);
        } else {
            stopService();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startSync(LiderComunitario lider) {
        new SendFileXmlTask().execute(lider);
    }

    public void stopService() {
        this.stopSelf();
    }

    private class SendFileXmlTask extends AsyncTask<LiderComunitario, String, String> {

        @Override
        protected String doInBackground(LiderComunitario... params) {

            NetworkController nController = new NetworkController();
            try {
                //nController.synContent(params[0]);
                for (LiderComunitario liderComunitario : params) {
                    nController.parseToXml(liderComunitario, SyncService.this);
                    for (Patient pacienteTemp : liderComunitario.getPatientList()) {
                        nController.parseToXml(pacienteTemp, SyncService.this);
                        for (Evaluation guaralTemp : pacienteTemp.getEvaluationList()) {
                            nController.parseToXml(guaralTemp, SyncService.this);
                        }
                    }
                }
                nController.syncContent();
                return nController.SummarySyncResponses();
            } catch (Exception e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = new Intent(BroadcastConstants.BROADCAST_ACTION)
                    .putExtra(BroadcastConstants.EXTENDED_DATA_STATUS, result);
            LocalBroadcastManager.getInstance(SyncService.this).sendBroadcast(intent);
            stopService();
        }
    }
}
