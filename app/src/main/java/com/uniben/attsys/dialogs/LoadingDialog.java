package com.uniben.attsys.dialogs;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Cyberman on 5/31/2018.
 */

public class LoadingDialog extends SweetAlertDialog {


    public LoadingDialog(Context context, String text) {
        super(context);
        changeAlertType(SweetAlertDialog.PROGRESS_TYPE);
        setTitleText(text);
    }

}
