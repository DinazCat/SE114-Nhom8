package com.example.healthcareapp.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.healthcareapp.Adapter.ImageAdapter;
import com.example.healthcareapp.R;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AddImgFragment extends Fragment {
    public static ArrayList<Uri> images = new ArrayList<>();
    public static ListView listView;
    public static LinearLayout Layout;
    private static final int REQUEST_IMAGE_PICK = 1;
    ImageButton cam, gal;
    LinearLayout chooseImg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_img, container, false);
        ImageButton Addimg = view.findViewById(R.id.imgpicker);
        cam = view.findViewById(R.id.camera);
        gal = view.findViewById(R.id.gallery);
        chooseImg = view.findViewById(R.id.choose);
        listView = view.findViewById(R.id.listview);
        if(images.size()>0)
        {
            ImageAdapter adapter = new ImageAdapter(this.getActivity(), images);
            listView.setAdapter(adapter);
        }
        Layout = view.findViewById(R.id.imginP);
        Addimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ImagePicker.Companion.with(AddImgFragment.this)
//                        .saveDir(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES))
//                        .galleryOnly()
//                        .crop()
//                        .compress(768)
//                        .maxResultSize(800, 800)
//                        .createIntent(intent -> {
//                            startForMediaPickerResult.launch(intent);
//                            return null;
//                        });
//                Intent intent = new Intent(Intent.ACTION_PICK,
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(intent, REQUEST_IMAGE_PICK);
                if(chooseImg.getVisibility()==View.INVISIBLE)
                chooseImg.setVisibility(View.VISIBLE);
                else chooseImg.setVisibility(View.INVISIBLE);


            }
        });
        System.out.println(images.size()+"size");
        cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImg.setVisibility(View.INVISIBLE);
                ImagePicker.Companion.with(AddImgFragment.this)
                        .cameraOnly()
                        .maxResultSize(1080,1080)
                        .start(101);
            }
        });

        gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImg.setVisibility(View.INVISIBLE);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });


        return view;
    }

    //    private final ActivityResultLauncher<Intent> startForMediaPickerResult = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                Intent data = result.getData();
//                if (data != null && result.getResultCode() == Activity.RESULT_OK) {
//                    Uri uri = data.getData();
//                    if(uri != null) images.add(uri);
//                    ImageAdapter adapter = new ImageAdapter(this.getActivity(), images);
//                    listView.setAdapter(adapter);
//                }});
//@Override
//public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//    super.onActivityResult(requestCode, resultCode, data);
//
//    if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
//        if (data != null) {
//            Uri uri = data.getData();
//            if(uri != null) images.add(uri);
//                    ImageAdapter adapter = new ImageAdapter(this.getActivity(), images);
//                    listView.setAdapter(adapter);
//        }
//    }
//}
    public static final int PICK_IMAGE = 1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 101 && data != null) {
            if (data != null) {
                Uri uri = data.getData();
                if ( uri != null ) images.add(uri);
                ImageAdapter adapter = new ImageAdapter(this.getActivity(), images);
                listView.setAdapter(adapter);
            }
        }
       else  if (requestCode == PICK_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                if ( uri != null ) images.add(uri);
                ImageAdapter adapter = new ImageAdapter(this.getActivity(), images);
                listView.setAdapter(adapter);
            }
        }
    }

}
