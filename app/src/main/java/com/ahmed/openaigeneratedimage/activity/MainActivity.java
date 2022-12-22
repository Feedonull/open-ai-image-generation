package com.ahmed.openaigeneratedimage.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmed.openaigeneratedimage.R;
import com.ahmed.openaigeneratedimage.async.GenerateImageRequest;

public class MainActivity extends AppCompatActivity {

    public static ImageView generatedImg;
    private EditText promptEt;
    private Button generateBtn;
    private GenerateImageRequest generateImageRequest;
    private String imaheUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generatedImg = (ImageView) findViewById(R.id.generatedImg);
        promptEt = (EditText) findViewById(R.id.promptEt);
        promptEt.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(getBaseContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(promptEt.getWindowToken(), 0);
                    generateBtn.performClick();
                    return true;
                }
                return false;
            }
        });
        generateBtn = (Button) findViewById(R.id.generateBtn);

        generateImageRequest = new GenerateImageRequest(this);

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(promptEt.getText().toString().length() > 0){

                    generateImageRequest.send(promptEt.getText().toString());

                }else{
                    promptEt.setError("Add image description");
                }
            }
        });

    }
}