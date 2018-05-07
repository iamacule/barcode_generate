package vn.mran.barcodegenerate.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import vn.mran.barcodegenerate.R;

/**
 * Created by HP on 03-Jan-17.
 */

public class DialogProgress {
    public static class Build {
        private final String TAG = getClass().getSimpleName();
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private TextView txtMessage;

        public Build(Activity activity) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_progress, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            txtMessage = (TextView) view.findViewById(R.id.txtMessage);
        }

        public Build setMessage(String message) {
            txtMessage.setText(message);
            return this;
        }

        public void show() {
            Log.d(TAG, "show: ");
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        public void dismiss() {
            Log.d(TAG, "dismiss: ");
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
