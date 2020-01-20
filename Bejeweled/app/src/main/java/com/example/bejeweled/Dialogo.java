package com.example.bejeweled;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class Dialogo extends DialogFragment {

    public interface OnDialogListener{
        void onPositiveButtonClicked();
        void onNegativeButtonClicked();

    }

    private OnDialogListener onDialogListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        onDialogListener = (OnDialogListener) getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        builder.setTitle("¿Usted quiere volver atras?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onDialogListener.onPositiveButtonClicked();
                        }
                    })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onDialogListener.onNegativeButtonClicked();
                    }
                });


        return builder.create();
    }
}
