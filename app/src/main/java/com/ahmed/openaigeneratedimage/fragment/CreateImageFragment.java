package com.ahmed.openaigeneratedimage.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

public class CreateImageFragment extends Fragment {

    public static ImageView generatedImg;
    private EditText promptEt;
    private Button generateBtn;
    private GenerateImageRequest generateImageRequest;
    private String imaheUrl = "";
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_image, null);

        generatedImg = (ImageView) view.findViewById(R.id.generatedImg);
        promptEt = (EditText) view.findViewById(R.id.promptEt);
        promptEt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getBaseContext().getSystemService(getActivity().getBaseContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(promptEt.getWindowToken(), 0);
                    generateBtn.performClick();
                    return true;
                }
                return false;
            }
        });
        generateBtn = (Button) view.findViewById(R.id.generateBtn);

        generateImageRequest = new GenerateImageRequest(getActivity());

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(promptEt.getText().toString().length() > 0){

                    generateImageRequest.createImage(promptEt.getText().toString());

                }else{
                    promptEt.setError("Add image description");
                }
            }
        });

        if(isNetworkAvailable() == false){
            showNoConnectionAlert();
        }
        return view;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void showNoConnectionAlert(){
        builder = new AlertDialog.Builder(getActivity().getBaseContext());
        builder.setCancelable(false);
        builder.setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("Error")
            .setMessage("No Internet connection")
            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getActivity().finish();
                }
            })
            .show();
    }

}