package vn.tungdx.mediapicker.activities;

import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import vn.tungdx.mediapicker.R;

/**
 * @author TUNGDX
 */

/**
 * Create dialog for media picker module. Should only use in this module.
 */
public class MediaPickerErrorDialog extends DialogFragment {
    private String mMessage;
    private OnClickListener mOnPositionClickListener;

    public static MediaPickerErrorDialog newInstance(String msg) {
        MediaPickerErrorDialog dialog = new MediaPickerErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMessage = getArguments().getString("msg");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(mMessage)
                .setPositiveButton(R.string.ok, mOnPositionClickListener)
                .create();
    }

    public void setOnOKClickListener(OnClickListener mOnClickListener) {
        this.mOnPositionClickListener = mOnClickListener;
    }
}