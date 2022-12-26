package com.ahmed.openaigeneratedimage.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.ahmed.openaigeneratedimage.util.Constants.CAMERA_PERMISSION_CODE;
import static com.ahmed.openaigeneratedimage.util.Constants.CHOOSE_FROM_GALLERY_REQUEST_CODE;
import static com.ahmed.openaigeneratedimage.util.Constants.READ_STORAGE_PERMISSION_CODE;
import static com.ahmed.openaigeneratedimage.util.Constants.TAKE_PHOTO_REQUEST_CODE;
import static com.ahmed.openaigeneratedimage.util.Constants.WRITE_STORAGE_PERMISSION_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

            selectImage(getActivity());


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case TAKE_PHOTO_REQUEST_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        editImageGeneratedImg.setImageBitmap(selectedImage);
                    }

                    break;
                case CHOOSE_FROM_GALLERY_REQUEST_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                editImageGeneratedImg.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                try {
                                    editImageGeneratedImg.setImageBitmap(convertToPNG(BitmapFactory.decodeFile(picturePath)));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
        } else {

        }
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose image");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, TAKE_PHOTO_REQUEST_CODE);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , CHOOSE_FROM_GALLERY_REQUEST_CODE);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private Bitmap convertToPNG(Bitmap image) throws IOException {

        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                "newImage",  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        FileOutputStream outStream = null;
        try {
            outStream = new FileOutputStream(imageFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resizedBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()),1024,1024);
        //return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
    }

    private Bitmap resizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

}