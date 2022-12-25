package com.ahmed.openaigeneratedimage.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmed.openaigeneratedimage.R;
import com.ahmed.openaigeneratedimage.async.GenerateImageRequest;

import java.util.ArrayList;

public class EditImageFragment extends Fragment {


    public static ImageView editImagegeneratedImg;
    private EditText editImagepromptEt;
    private Button editImagegenerateBtn;
    private GenerateImageRequest generateImageRequest;
    private String imaheUrl = "";
    private AlertDialog.Builder builder;

    private Bitmap myBitmap;
    private Uri picUri;

    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 107;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_image, null);

        editImagegeneratedImg = (ImageView) view.findViewById(R.id.edit_image_generatedImg);
        editImagepromptEt = (EditText) view.findViewById(R.id.edit_image_promptEt);
        editImagepromptEt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getBaseContext().getSystemService(getActivity().getBaseContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editImagepromptEt.getWindowToken(), 0);
                    editImagegenerateBtn.performClick();
                    return true;
                }
                return false;
            }
        });
        editImagegenerateBtn = (Button) view.findViewById(R.id.edit_image_generateBtn);

        return view;
    }
}