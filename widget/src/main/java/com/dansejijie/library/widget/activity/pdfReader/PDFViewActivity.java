package com.dansejijie.library.widget.activity.pdfReader;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.dansejijie.library.widget.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.shockwave.pdfium.PdfDocument;
import java.util.List;

/**
 * Created by tygzx on 17/4/26.
 */

public class PDFViewActivity extends Activity {

    private static final String TAG = PDFViewActivity.class.getSimpleName();

    private final static int REQUEST_CODE = 42;
    public static final int PERMISSION_CODE = 42042;

    public static final String SAMPLE_FILE = "sample.pdf";
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";

    CustomPDFView pdfView;

    Uri uri;


    public static void start(Context context){
        Intent intent=new Intent(context,PDFViewActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view);
        pdfView= (CustomPDFView) findViewById(R.id.pdfView);

        findViewById(R.id.pdfView_choose_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pickFile();
                String url="http://onlinelibrary.wiley.com/doi/10.1002/9781118295472.fmatter/pdf";
                Uri uri=Uri.parse(url);
                pdfView.open(uri);
            }
        });
        findViewById(R.id.pdfView_choose_file2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pickFile();
                String url="http://onlinelibrary.wiley.com/doi/10.1002/9781118295472.app1/pdf";
                Uri uri=Uri.parse(url);
                pdfView.open(uri);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        //final String fileName= new String(Base64.encode(url.getBytes(),Base64.DEFAULT))+".pdf";
        //String path= Environment.getExternalStorageDirectory().getAbsolutePath()+ "/a/"+fileName;

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    void pickFile() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                READ_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{READ_EXTERNAL_STORAGE},
                    PERMISSION_CODE
            );

            return;
        }

        launchPicker();
    }

    void launchPicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        try {
            startActivityForResult(intent, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {
            //alert user that file manager not working
            Toast.makeText(this, "文件查找失败", Toast.LENGTH_SHORT).show();
        }
    }

    void afterViews() {
        if (uri != null) {
            displayFromUri(uri);
        } else {
            displayFromAsset(SAMPLE_FILE);
        }
    }

    private void displayFromAsset(String assetFileName) {


    }

    private void displayFromUri(Uri uri) {

        pdfView.open(uri);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            displayFromUri(uri);
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

    /**
     * Listener for response to user permission request
     *
     * @param requestCode  Check that permission request code matches
     * @param permissions  Permissions that requested
     * @param grantResults Whether permissions granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchPicker();
            }
        }
    }

}