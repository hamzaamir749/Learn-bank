package com.coderpakistan.learningbank.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.coderpakistan.learningbank.R;

public class LoadingDialog {
    Context context;
    Dialog dialogView;

    public LoadingDialog(Context activity) {
        this.context = activity;
    }

    public void show() {
        dialogView = new Dialog(context, R.style.CustomAlertDialog);
        dialogView.setCanceledOnTouchOutside(false);
        dialogView.setContentView(R.layout.progress_dialouge_layout);
        dialogView.show();
    }

    public void dismiss() {
        dialogView.dismiss();
    }
}
