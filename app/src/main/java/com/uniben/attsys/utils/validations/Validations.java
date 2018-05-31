package com.uniben.attsys.utils.validations;

import android.support.design.widget.TextInputEditText;
import android.widget.CheckBox;
import android.widget.EditText;

import com.uniben.attsys.R;

import java.util.List;



/**
 * Created by Cyberman on 5/24/2018.
 */

public class Validations {

    public static boolean validateUserData(List<TextInputEditText> editTexts){
        for (EditText editText: editTexts ) {
            if(EmptyValidation.isEmptyString(editText.getText().toString())){
                editText.setError(editText.getContext().getString(R.string.error_empty_edit_text));
                return false;
            }
        }


        return true;
    }



}
