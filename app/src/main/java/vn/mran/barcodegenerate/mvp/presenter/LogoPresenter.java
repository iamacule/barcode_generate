package vn.mran.barcodegenerate.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumMap;
import java.util.Map;

import vn.mran.barcodegenerate.R;
import vn.mran.barcodegenerate.helper.FileHelper;
import vn.mran.barcodegenerate.helper.PermissionHelper;
import vn.mran.barcodegenerate.pref.Constant;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

/**
 * Created by Mr An on 20/09/2017.
 */

public class LogoPresenter {
    public interface LogoView {
        void onPermissionGranted();

        void onGetLogoSuccess(Bitmap bp);

        void onStartingExport();

        void onExportProgressUpdate(String message);

        void onExportSuccess();

        void onExportFail();
    }

    private final String TAG = getClass().getSimpleName();
    private final int MAX_ITEM_WIDTH = 40;

    private LogoView view;
    private Activity activity;
    private PermissionHelper permissionHelper;
    private int action;

    public LogoPresenter(Activity activity, LogoView view) {
        this.activity = activity;
        this.view = view;
        permissionHelper = new PermissionHelper(activity);
    }

    public int getAction() {
        return action;
    }

    public void checkPermission(String permission, int idCallBack, int action) {
        Log.d(TAG, "checkPermission: " + permission);
        this.action = action;
        if (permissionHelper.checkPermission(permission)) {
            Log.d(TAG, "checkPermission: onPermissionGranted");
            view.onPermissionGranted();
        } else {
            Log.d(TAG, "checkPermission: requestPermission");
            permissionHelper.requestPermission(permission, idCallBack);
        }
    }

    public void getLogoFromPickImage(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: get image requestCode : " + requestCode);
        Log.d(TAG, "onActivityResult: get image resultCode : " + resultCode);

        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.
        if (requestCode == Constant.SELECT_LOGO_CODE && resultCode == activity.RESULT_OK && data != null) {
            Log.d(TAG, "onActivityResult: get image success");
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = activity.getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
            view.onGetLogoSuccess(bitmap);
            // Do something with the bitmap

            // At the end remember to close the cursor or you will end with the RuntimeException!
            cursor.close();
        }
    }

    public Bitmap configureLogo(Bitmap logo) {
        int exceptionColor;
        if (logo.getPixel(1, 1) == Color.WHITE) {
            Log.d(TAG, "configureLogo: white");
            exceptionColor = Color.WHITE;
        } else {
            exceptionColor = Color.TRANSPARENT;
            Log.d(TAG, "configureLogo: transparent");
        }
        return configureBitmap(logo, -1, Color.BLACK, exceptionColor);
    }

    public Bitmap configureBitmap(final Bitmap oldLogo, final int oldColor, final int newColor, final int exceptionColor) {
        Bitmap newLogo = oldLogo.copy(Bitmap.Config.ARGB_8888, true);
        for (int i = 0; i < oldLogo.getWidth(); i++) {
            for (int j = 0; j < oldLogo.getHeight(); j++) {
                if (oldColor != -1) {

                    if (oldLogo.getPixel(i, j) == oldColor) {
                        newLogo.setPixel(i, j, newColor);
                    }
                }
                if (exceptionColor != -1) {
                    if (oldLogo.getPixel(i, j) != exceptionColor) {
                        newLogo.setPixel(i, j, newColor);
                    }
                }
            }
        }
        return newLogo;
    }

    public Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }
        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
            hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (Exception iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return Bitmap.createBitmap(bitmap, bitmap.getWidth() * 5 / 100, 0, bitmap.getWidth() * 90 / 100, bitmap.getHeight());
    }

    private String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    public String convertProgress(String message, int value, int total) {
        return message.replace("$1", String.valueOf(value)).replace("$2", String.valueOf(total)).toString();
    }

    public Bitmap exportBitmap(Bitmap logoBitmap, Bitmap barcodeBitmap, int number) {
        Bitmap exportBitmap = Bitmap.createBitmap(Constant.PRINT_WIDTH, Constant.PRINT_HEIGHT, Bitmap.Config.ARGB_8888); // this creates a MUTABLE bitmap
        Canvas canvas = new Canvas(exportBitmap);


        //Draw logo
        if (logoBitmap == null)
            logoBitmap = Bitmap.createBitmap(Constant.PRINT_WIDTH, Constant.LOGO_HEIGHT, Bitmap.Config.ALPHA_8);
        Rect rectLogo = new Rect(0, 8, canvas.getWidth(), 107);
        canvas.drawBitmap(logoBitmap, null, rectLogo, null);

        //Draw Barcode
        Rect rectBarcode = new Rect(0, 114, canvas.getWidth(), 196);
        canvas.drawBitmap(barcodeBitmap, null, rectBarcode, null);

        //Draw number
        Paint pNumber = new Paint();
        pNumber.setColor(Color.BLACK);
        pNumber.setTextSize(Constant.NUMBER_TEXT_SIZE);
        pNumber.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        int xPos = (int) ((canvas.getWidth() / 2) - (pNumber.measureText(String.format("%07d", number)) / 2));
        int yPos = (int) ((238) - ((pNumber.descent() + pNumber.ascent()) / 2));
        canvas.drawText(String.format("%07d", number), xPos, yPos, pNumber);

        //Draw frame
//        Paint pFrame = new Paint();
//        pFrame.setColor(Color.BLACK);
//        pFrame.setStyle(Paint.Style.STROKE);
//        Rect r = new Rect(1, 1, canvas.getWidth() - 1, canvas.getHeight() - 1);
//        canvas.drawRect(r, pFrame);

        return configureBitmap(flipAndRotate(exportBitmap), Color.TRANSPARENT, Color.WHITE, -1);
    }

    private Bitmap flipAndRotate(Bitmap src) {
        Matrix m = new Matrix();

        //Rotate
        m.postRotate(-90);

        //Flip
//        m.preScale(1, -1);

        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return dst;
    }

    public void export(Bitmap logoBitmap, int from, int to) {
        new ExportTask(logoBitmap, from, to).execute(new Void[]{});
    }

    private class ExportTask extends AsyncTask<Void, Integer, Boolean> {

        private FileHelper fileHelper;
        private Bitmap logoBitmap;
        private int from;
        private int to;

        public ExportTask(Bitmap logoBitmap, int from, int to) {
            this.from = from;
            Log.d(TAG, "doInBackground: From : " + from);
            this.to = to;
            Log.d(TAG, "doInBackground: To : " + to);
            this.logoBitmap = logoBitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            view.onStartingExport();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                prepareFile();

                //Create document
                Document document = new Document();
                document.setPageSize(new Rectangle(Constant.A1_WIDTH, Constant.A1_HEIGHT));
                document.setMargins(0, 0, 0, 0);
                PdfWriter.getInstance(document, new FileOutputStream(fileHelper.getFile()));
                document.open();
                Log.d(TAG, "doInBackground: create document success");

                validateFinishNumber();

                for (int i = from; i <= to; i += MAX_ITEM_WIDTH) {
                    Log.d(TAG, "doInBackground: create total bitmap");
                    Bitmap totalBitmap = Bitmap.createBitmap(Constant.PRINT_HEIGHT * MAX_ITEM_WIDTH,
                            Constant.PRINT_WIDTH, Bitmap.Config.ARGB_8888); // this creates a MUTABLE bitmap
                    Canvas canvas = new Canvas(totalBitmap);
                    Log.d(TAG, "doInBackground: create total bitmap success");

                    int startNumber = i;
                    int stopNumber = startNumber + (MAX_ITEM_WIDTH - 1);
                    int pos = 0;
                    for (int j = startNumber; j <= stopNumber; j++) {
                        Log.d(TAG, "doInBackground: pos = " + (pos + startNumber));
                        publishProgress(pos + startNumber);
                        Bitmap bp = exportBitmap(logoBitmap, encodeAsBitmap(String.format("%07d", j), BarcodeFormat.CODE_128, Constant.PRINT_WIDTH, Constant.BARCODE_HEIGHT), j);
                        Rect rect = new Rect(Constant.PRINT_HEIGHT * pos, 0, (Constant.PRINT_HEIGHT * pos) + Constant.PRINT_HEIGHT, Constant.PRINT_WIDTH);
                        canvas.drawBitmap(bp, null, rect, null);
                        pos++;
                    }

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    totalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());
                    document.add(image);

                    //Create space
                    Bitmap spaceBitmap = Bitmap.createBitmap(Constant.PRINT_HEIGHT * MAX_ITEM_WIDTH,
                            Constant.PRINT_WIDTH, Bitmap.Config.ARGB_8888); // this creates a MUTABLE bitmap
                    spaceBitmap.eraseColor(Color.WHITE);
                    ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
                    spaceBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream2);
                    Image image2 = Image.getInstance(stream2.toByteArray());
                    document.add(image2);

                }
                document.close();

                try {
                    Runtime.getRuntime().exec("sync");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "export: Convert success");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "doInBackground: " + e.getMessage());
                return false;
            }
        }

        private void validateFinishNumber() {
            if (to < MAX_ITEM_WIDTH)
                to = MAX_ITEM_WIDTH;
            else if (to > MAX_ITEM_WIDTH) {
                if (to % MAX_ITEM_WIDTH != 0) {
                    int temp = to;
                    while (true) {
                        temp = temp + 1;
                        if (temp % MAX_ITEM_WIDTH == 0) {
                            break;
                        }
                    }
                    to = temp;
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            view.onExportProgressUpdate(convertProgress(activity.getString(R.string.exporting), values[0], to));
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean)
                view.onExportSuccess();
            else
                view.onExportFail();
        }

        private void prepareFile() {
            fileHelper = new FileHelper();
            fileHelper.generateFile(new SimpleDateFormat(Constant.FILE_NAME_FORMAT).format(new Date()).toString() + Constant.EXTENSION_NAME);
        }

    }
}
