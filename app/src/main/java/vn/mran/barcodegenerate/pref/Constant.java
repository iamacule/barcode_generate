package vn.mran.barcodegenerate.pref;

/**
 * Created by Mr An on 20/09/2017.
 */

public interface Constant {
    int SELECT_LOGO_CODE = 0;
    int ID_CALLBACK_WRITE_EXTERNAL_STORAGE_PERMISSION = 0;

    int ACTION_CHOOSE_PICTURE = 0;
    int ACTION_EXPORT = 1;

    int MAX_ITEM_WIDTH = 20;

    int A1_WIDTH = 7016;
    int A1_HEIGHT = 9933;
    int PRINT_WIDTH = 400;
    int PRINT_HEIGHT = 350;
    int SPACE_HEIGHT = 600;
    int LOGO_HEIGHT = 70;
    int BARCODE_HEIGHT = 70;
    int NUMBER_TEXT_SIZE = 80;

    String BARCODE_EXPORT_FOLDER_NAME = "/BARCODE_EXPORT";
    String FILE_NAME_FORMAT = "yyyyMMdd-hhmm";
    String EXTENSION_NAME = ".pdf";
}
