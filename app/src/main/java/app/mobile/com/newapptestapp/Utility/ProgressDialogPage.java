package app.mobile.com.newapptestapp.Utility;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogPage {
    ProgressDialog progressDialog;
    Context context;
    public ProgressDialogPage(Context context) {
        this.context=context;
        createProgress();

    }

    public ProgressDialog createProgress() {
         progressDialog = new ProgressDialog(context);
         progressDialog.setMessage("Load..");
         progressDialog.setCancelable(false);
         progressDialog.setCanceledOnTouchOutside(false);
         return progressDialog;
    }



}
