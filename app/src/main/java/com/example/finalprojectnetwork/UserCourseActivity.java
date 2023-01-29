package com.example.finalprojectnetwork;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.example.finalprojectnetwork.databinding.ActivityUserCourseBinding;
import com.example.finalprojectnetwork.modle.FragmentAdapter;
import com.example.finalprojectnetwork.modle.Util;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class UserCourseActivity extends AppCompatActivity {
    ActivityUserCourseBinding binding;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        UserId = getIntent().getStringExtra(Util.UserId);

        ArrayList<String> tab = new ArrayList<>();
        tab.add("NetWork");
        tab.add("SoftWare");
        tab.add("WEB");
        tab.add("WorkShop");

        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < tab.size(); i++) {
            fragments.add(BlankFragment.newInstance(tab.get(i), UserId));
        }

        FragmentAdapter adapter = new FragmentAdapter(this, fragments);
        binding.VP.setAdapter(adapter);
        new TabLayoutMediator(binding.TL, binding.VP, (tab2, position) -> tab2.setText(tab.get(position))).attach();

    }
}