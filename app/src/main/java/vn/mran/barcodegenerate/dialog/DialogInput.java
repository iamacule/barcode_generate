package vn.mran.barcodegenerate.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import vn.mran.barcodegenerate.R;

/**
 * Created by HP on 03-Jan-17.
 */

public class DialogInput {

    public interface OnDialogInputListener {
        void onNumberReturn(int number);
    }

    public static class Build {
        private final String TAG = getClass().getSimpleName();
        private AlertDialog.Builder builder;
        private AlertDialog dialog;
        private Button btnAdd;
        private Button btnCancel;
        private EditText edtAdd;
        private TextView txtTitle;
        private OnDialogInputListener onDialogInputListener;

        public Build(final Activity activity) {
            builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_input, null);
            builder.setView(view);
            dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            btnAdd = (Button) view.findViewById(R.id.btnAdd);
            btnCancel = (Button) view.findViewById(R.id.btnCancel);
            edtAdd = (EditText) view.findViewById(R.id.edtAdd);
            txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Toast.makeText(activity,activity.getString(R.string.add_success),Toast.LENGTH_SHORT).show();
                        onDialogInputListener.onNumberReturn(Integer.parseInt(edtAdd.getText().toString()));
                        dismiss();
                    } catch (Exception e) {
                        Toast.makeText(activity,activity.getString(R.string.input_empty),Toast.LENGTH_SHORT).show();
                    }
                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        public Build setOnDialogInputListener(OnDialogInputListener onDialogInputListener) {
            this.onDialogInputListener = onDialogInputListener;
            return this;
        }

        public Build setTitle(String title) {
            txtTitle.setText(title);
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
