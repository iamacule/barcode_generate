package vn.mran.barcodegenerate.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.BarcodeFormat;

import vn.mran.barcodegenerate.R;
import vn.mran.barcodegenerate.base.BaseFragment;
import vn.mran.barcodegenerate.dialog.DialogConfirm;
import vn.mran.barcodegenerate.dialog.DialogProgress;
import vn.mran.barcodegenerate.mvp.presenter.LogoPresenter;
import vn.mran.barcodegenerate.pref.Constant;
import vn.mran.barcodegenerate.utils.TouchEffect;

/**
 * Created by Mr An on 20/09/2017.
 */

public class LogoFragment extends BaseFragment implements View.OnClickListener, LogoPresenter.LogoView {
    private String TAG = getClass().getSimpleName();

    private Button btnChoose;
    private ImageView imgLogo;
    private LinearLayout btnExport;
    private CheckBox cbFG;
    private EditText edtFBColor;

    private LogoPresenter presenter;
    private DialogProgress.Build dialogProgress;

    private int from = -1;
    private int to = -1;
    private Bitmap bpLogo;

    @Override
    public void initView() {
        btnChoose = (Button) view.findViewById(R.id.btnChoose);
        imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
        btnExport = (LinearLayout) view.findViewById(R.id.btnExport);
        cbFG = (CheckBox) view.findViewById(R.id.cbFG);
        edtFBColor = (EditText) view.findViewById(R.id.edtFBColor);

        TouchEffect.addAlpha(btnExport);
    }

    @Override
    public void initValue() {
        presenter = new LogoPresenter(getActivity(), this);
        dialogProgress = new DialogProgress.Build(getActivity());
    }

    @Override
    public void initAction() {
        btnChoose.setOnClickListener(this);
        btnExport.setOnClickListener(this);
        cbFG.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtFBColor.setEnabled(true);
                } else {
                    edtFBColor.setEnabled(false);
                }
            }
        });
    }

    @Override
    public int setFragmentView() {
        return R.layout.fragment_logo;
    }

    /**
     * Set data
     *
     * @param from
     * @param to
     */
    public void setData(int from, int to) {
        this.from = from;
        this.to = to;

        imgLogo.setBackgroundColor(Color.WHITE);
        imgLogo.setImageBitmap(presenter.exportItem(bpLogo, presenter.encodeAsBitmap(String.format("%07d", from), BarcodeFormat.CODE_128, Constant.PRINT_WIDTH, Constant.BARCODE_HEIGHT), from));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnChoose:
                presenter.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Constant.ID_CALLBACK_WRITE_EXTERNAL_STORAGE_PERMISSION,
                        Constant.ACTION_CHOOSE_PICTURE);
                break;

            case R.id.btnExport:
                if (bpLogo == null) {
                    new DialogConfirm.Build(getActivity()).setTitle(context.getString(R.string.warning))
                            .setContent(context.getString(R.string.no_logo))
                            .setListener(new DialogConfirm.Build.OnDialogConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    Log.d(TAG, "onConfirm: ");
                                    Log.d(TAG, "onClick: export");
                                    presenter.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Constant.ID_CALLBACK_WRITE_EXTERNAL_STORAGE_PERMISSION,
                                            Constant.ACTION_EXPORT);
                                }

                                @Override
                                public void onCancel() {
                                    Log.d(TAG, "onCancel: ");
                                }
                            }).show();
                } else {
                    presenter.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Constant.ID_CALLBACK_WRITE_EXTERNAL_STORAGE_PERMISSION,
                            Constant.ACTION_EXPORT);
                }
                break;
        }
    }

    public void startPickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Constant.SELECT_LOGO_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.getLogoFromPickImage(requestCode, resultCode, data);
    }

    @Override
    public void onPermissionGranted() {
        switch (presenter.getAction()) {
            case Constant.ACTION_CHOOSE_PICTURE:
                startPickImage();
                break;

            case Constant.ACTION_EXPORT:
                if (cbFG.isEnabled()) {
                    try {
                        presenter.export(bpLogo, from, to, Color.parseColor("#"+edtFBColor.getText().toString().trim()));
                    } catch (Exception e) {
                        presenter.export(bpLogo, from, to, -1);
                    }
                } else {
                    presenter.export(bpLogo, from, to, -1);
                }
                break;
        }
    }

    @Override
    public void onGetLogoSuccess(Bitmap bp) {
        this.bpLogo = bp;
        dialogProgress.dismiss();
        imgLogo.setImageBitmap(presenter.exportItem(bpLogo, presenter.encodeAsBitmap(String.format("%07d", from), BarcodeFormat.CODE_128, Constant.PRINT_WIDTH, Constant.BARCODE_HEIGHT), from));
        cbFG.setEnabled(true);
    }

    @Override
    public void onStartingExport() {
        dialogProgress.setMessage(presenter.convertProgress(context.getString(R.string.exporting), 1, to)).show();
    }

    @Override
    public void onExportProgressUpdate(String message) {
        dialogProgress.setMessage(message);
    }

    @Override
    public void onExportSuccess() {
        dialogProgress.dismiss();
        showDialogInfo(context.getString(R.string.success), context.getString(R.string.export_success) + Constant.BARCODE_EXPORT_FOLDER_NAME);
    }

    @Override
    public void onExportFail() {
        dialogProgress.dismiss();
        showDialogInfo(context.getString(R.string.dialog_error_title), context.getString(R.string.export_fail));
    }
}
