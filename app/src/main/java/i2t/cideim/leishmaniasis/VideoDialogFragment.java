package i2t.cideim.leishmaniasis;

import android.app.Dialog;
import android.app.DialogFragment;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import i2t.cideim.R;

/**
 * Created by Leonardo and Camilo.
 * Dialog to show videos from resources.
 */

public class VideoDialogFragment extends DialogFragment {

    private VideoView videoView;
    private int videoResId;

    public void setVideoResId(int videoResId) {
        this.videoResId = videoResId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View rootView = inflater.inflate(R.layout.dialog_video, container, false);
        videoView = (VideoView) rootView.findViewById(R.id.videoView);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                VideoDialogFragment.this.dismiss();
            }
        });
        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // do nothing
                return true;
            }
        });
        videoView.setMediaController(new MediaController(getActivity()));
        Uri video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + videoResId);
        videoView.setVideoURI(video);
        videoView.start();
        return rootView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}
