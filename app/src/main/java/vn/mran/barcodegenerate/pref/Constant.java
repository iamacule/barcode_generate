package vn.mran.barcodegenerate.pref;

/**
 * Created by Mr An on 20/09/2017.
 */

public interface Constant {
    int SELECT_LOGO_CODE = 0;
    int ID_CALLBACK_WRITE_EXTERNAL_STORAGE_PERMISSION = 0;

    int ACTION_CHOOSE_PICTURE = 0;
    int ACTION_EXPORT = 1;

    int MAX_ITEM_WIDTH = 10;
    int MAX_ITEM_HEIGHT = 5;
    int MAX_ITEM_IN_ONE_PAGE = MAX_ITEM_WIDTH * MAX_ITEM_HEIGHT;

    int PRINT_WIDTH = 400;
    int PRINT_HEIGHT = 350;
    int SPACE_HEIGHT = 130;
    int LOGO_HEIGHT = 70;
    int BARCODE_HEIGHT = 70;
    int NUMBER_TEXT_SIZE = 80;

    String BARCODE_EXPORT_FOLDER_NAME = "/BARCODE_EXPORT";
    String MAIN_FILE_NAME_FORMAT = "yyyyMMdd-HHmm";
    String FOREGROUND_FILE_NAME_EXTENSION = "-FOREGROUND";
    String EXTENSION_NAME = ".pdf";
}
