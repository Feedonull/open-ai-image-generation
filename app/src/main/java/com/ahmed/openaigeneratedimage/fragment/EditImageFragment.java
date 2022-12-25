package com.ahmed.openaigeneratedimage.fragment;

import static com.ahmed.openaigeneratedimage.util.Constants.CAMERA_PERMISSION_CODE;
import static com.ahmed.openaigeneratedimage.util.Constants.READ_STORAGE_PERMISSION_CODE;
import static com.ahmed.openaigeneratedimage.util.Constants.WRITE_STORAGE_PERMISSION_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
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
import android.widget.Toast;

import com.ahmed.openaigeneratedimage.R;
import com.ahmed.openaigeneratedimage.async.GenerateImageRequest;
import com.ahmed.openaigeneratedimage.util.Constants;

import java.util.ArrayList;

public class EditImageFragment extends Fragment implements View.OnClickListener {


    public static ImageView editImageGeneratedImg;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_image, null);

        editImageGeneratedImg = (ImageView) view.findViewById(R.id.edit_image_generatedImg);
        editImageGeneratedImg.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.edit_image_generatedImg){
            checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Constants.WRITE_STORAGE_PERMISSION_CODE);
            checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Constants.READ_STORAGE_PERMISSION_CODE);

            
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            if (requestCode == CAMERA_PERMISSION_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
                else {
                }
            } else if (requestCode == WRITE_STORAGE_PERMISSION_CODE) {
                if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
            } else if (requestCode == READ_STORAGE_PERMISSION_CODE) {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                }
            }
        }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {

        }
    }
}