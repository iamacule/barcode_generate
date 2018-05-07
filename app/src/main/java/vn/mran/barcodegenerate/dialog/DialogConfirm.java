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
 * Created by Covisoft on 07/01/2016.
 */
public class DialogConfirm {
    public static class Build {
        private final String TAG = getClass().getSimpleName();
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private TextView txtTitle;
        private TextView txtContent;
        private Button btnCancel;
        private Button btnConfirm;
        private OnDialogConfirmListener onDialogConfirmListener;

        public Build(Activity activity) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_confirm, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            txtContent = (TextView) view.findViewById(R.id.txtMessage);
            btnConfirm = (Button) view.findViewById(R.id.btnConfirm);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDialogConfirmListener.onConfirm();
                    dismiss();
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDialogConfirmListener.onCancel();
                    dismiss();
                }
            });
        }

        public Build setTitle(String title) {
            txtTitle.setText(title);
            return this;
        }

        public Build setContent(String content) {
            txtContent.setText(content);
            return this;
        }

        public Build setListener(OnDialogConfirmListener onDialogConfirmListener) {
            this.onDialogConfirmListener = onDialogConfirmListener;
            return this;
        }

        public void show() {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }

        public void dismiss() {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

        public interface OnDialogConfirmListener {
            void onConfirm();

            void onCancel();
        }
    }
}
