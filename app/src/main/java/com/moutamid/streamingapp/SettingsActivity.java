package com.moutamid.streamingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.moutamid.streamingapp.databinding.ActivitySettingsBinding;
import com.moutamid.streamingapp.models.TabsModel;

import java.util.ArrayList;
import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;
    ArrayList<TabsModel> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        boolean lc = Stash.getBoolean("lockState", false);
        String player = Stash.getString("buttonTXT", "Always Ask");

        binding.playername.setText(player);

        //Stash.clear("hidden");

        if (lc){
            binding.lockCheck.setChecked(true);
        } else {
            binding.lockCheck.setChecked(false);
        }

        binding.lockSetting.setOnClickListener(v->{
            if (binding.lockCheck.isChecked()){
                binding.lockCheck.setChecked(false);
                Stash.put("lockState", false);
            } else {
                binding.lockCheck.setChecked(true);
                Stash.put("lockState", true);
            }
        });

        binding.hideCategory.setOnClickListener(v -> {
            hideCategory();
        });

        binding.lockCheck.setOnClickListener(v -> {
            if (binding.lockCheck.isChecked()) {
                Stash.put("lockState", true);
                String s = Stash.getString("password", "");
                if (s.isEmpty()){
                    Stash.put("password", "1234");
                }
            } else {
                Stash.put("lockState", false);
            }
        });

        //binding.adjust.setVisibility(View.GONE);

        binding.adjust.setOnClickListener(v -> {
            startActivity(new Intent(this, AdjustTabsActivity.class));
        });

        binding.videoPlayer.setOnClickListener(v -> player());

        binding.password.setOnClickListener(v -> {
            Dialog();
        });
    }

    private void hideCategory() {
        final Dialog hide = new Dialog(this);
        hide.requestWindowFeature(Window.FEATURE_NO_TITLE);
        hide.setContentView(R.layout.hide_category_layout);

        Button cancel = hide.findViewById(R.id.cancel);
        Button ok = hide.findViewById(R.id.ok);

        cancel.setOnClickListener(v -> {
            hide.dismiss();
        });

        ok.setOnClickListener(v -> {
            ArrayList<String> finalList = Stash.getArrayList("hid", String.class);
            Stash.put("hidden", finalList);
            hide.dismiss();
        });

        list = Stash.getArrayList(Constants.channelsTab, TabsModel.class);

        LinearLayout linearLayout = hide.findViewById(R.id.layout);
        //Toast.makeText(this, ""+list.size(), Toast.LENGTH_SHORT).show();
        // Create Checkbox Dynamically
        for (TabsModel s : list) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("\t\t" + s.getName().toUpperCase(Locale.ROOT));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                checkBox.setButtonTintList( AppCompatResources.getColorStateList(SettingsActivity.this, R.color.orange));
            }
            checkBox.setTextColor(getResources().getColor(R.color.grey));
            ArrayList<String> list1 = Stash.getArrayList("hidden", String.class);
            Log.d("HiddenList", list1.toString());
            for (String r : list1){
                if (r.equalsIgnoreCase(s.getName())){
                    checkBox.setChecked(true);
                }
            }

            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                ArrayList<String> finalList = Stash.getArrayList("hidden", String.class);
                if (isChecked) {
                    Stash.put(buttonView.getText().toString().trim().toLowerCase(Locale.ROOT), buttonView.getText().toString().trim().toLowerCase(Locale.ROOT));
                    finalList.add(buttonView.getText().toString().trim());
                    Log.d("HiddenList", finalList.toString());
                    Stash.put("hidden", finalList);
                } else {
                    Stash.clear(buttonView.getText().toString().trim().toLowerCase(Locale.ROOT));
                    finalList.remove(buttonView.getText().toString().trim());
                    Log.d("HiddenList", finalList.toString());
                    Stash.put("hidden", finalList);
                }
            });

            // Add Checkbox to LinearLayout
            if (linearLayout != null) {
                linearLayout.addView(checkBox);
            }
        }

        hide.show();
        hide.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        hide.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        hide.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        hide.getWindow().setGravity(Gravity.CENTER);

    }

    private void player() {
        final Dialog players = new Dialog(this);
        players.requestWindowFeature(Window.FEATURE_NO_TITLE);
        players.setContentView(R.layout.players);

        RadioGroup radioGroup = players.findViewById(R.id.radiogroup);

        int id = Stash.getInt("buttonID", R.id.alwaysAsk);

        radioGroup.check(id);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
                int radioButtonID = radioGroup.getCheckedRadioButtonId();
                Stash.put("buttonID", radioButtonID);
                View radioButton = radioGroup.findViewById(radioButtonID);
                int idx = radioGroup.indexOfChild(radioButton);
                MaterialRadioButton r = (MaterialRadioButton) radioGroup.getChildAt(idx);
                String selectedText = r.getText().toString();
                selectedText = selectedText.replace("\t\t", "");
                Stash.put("buttonIDX", idx);
                Stash.put("buttonIDDD", idx);
                Stash.put("buttonTXT", selectedText);
                binding.playername.setText(selectedText);
                players.dismiss();
            }
        });

        Button cancel = players.findViewById(R.id.cancel);

        cancel.setOnClickListener(v -> players.dismiss());

        players.show();
        players.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        players.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        players.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        players.getWindow().setGravity(Gravity.CENTER);
    }

    private void Dialog() {
        final Dialog password = new Dialog(this);
        password.requestWindowFeature(Window.FEATURE_NO_TITLE);
        password.setContentView(R.layout.password_layout);

        TextInputLayout pasw = password.findViewById(R.id.et_password);
        TextInputEditText passwordEt = password.findViewById(R.id.passwordEt);
        Button ok = password.findViewById(R.id.ok);
        Button cancel = password.findViewById(R.id.cancel);

        pasw.requestFocus();
        passwordEt.requestFocus();
        pasw.getEditText().requestFocus();

        cancel.setOnClickListener(v -> password.dismiss());

        ok.setOnClickListener(v -> {
            if (pasw.getEditText().getText().toString().isEmpty()) {
                Toast.makeText(this, "Add Password", Toast.LENGTH_SHORT).show();
            } else {
                Stash.put("password", pasw.getEditText().getText().toString());
                Toast.makeText(this, "Password Updated", Toast.LENGTH_SHORT).show();
                password.dismiss();
            }
        });

        password.show();
        password.getWindow().clearFlags( WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        password.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        password.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        password.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        password.getWindow().setGravity(Gravity.CENTER);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}