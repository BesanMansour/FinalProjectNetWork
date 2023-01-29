package com.example.finalprojectnetwork;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.finalprojectnetwork.databinding.ActivityCourseBinding;
import com.example.finalprojectnetwork.modle.Util;

public class CourseActivity extends AppCompatActivity {
ActivityCourseBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = new Intent(CourseActivity.this, UploadActivity.class);

        binding.btnNetWork.setOnClickListener(view -> {
            intent.putExtra(Util.document,Util.NetWork);
            startActivity(intent);
        });
        binding.btnSoftWere.setOnClickListener(view -> {
            intent.putExtra(Util.document,Util.SoftWare);
            startActivity(intent);
        });
        binding.btnWEB.setOnClickListener(view -> {
            intent.putExtra(Util.document,Util.WEB);
            startActivity(intent);
        });
        binding.btnWorkShop.setOnClickListener(view -> {
            intent.putExtra(Util.document,Util.WorkShop);
            startActivity(intent);
        });

    }
}