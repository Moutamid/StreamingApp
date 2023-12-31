package com.moutamid.streamingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.moutamid.streamingapp.databinding.ActivityEventsBinding;
import com.moutamid.streamingapp.fragments.CommonEventFragment;
import com.moutamid.streamingapp.models.TabLocal;
import com.moutamid.streamingapp.models.TabsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class EventsActivity extends AppCompatActivity {
    ActivityEventsBinding binding;

    Context context;
    private ProgressDialog progressDialog;
    ArrayList<TabsModel> tabs;
    TabsModel tabsModel;
    ArrayList<TabsModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabs = new ArrayList<>();

        progressDialog = new ProgressDialog(this, R.style.AlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        Log.d("testing123", "Event OnCreate");

        /*requireContext().setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        JSONObject data = (JSONObject) Stash.getObject(Constants.eventsData, JSONObject.class);

        progressDialog.show();
        getData();

        /*if (data == null) {
            progressDialog.show();
            getData();
            Log.d("testing123", "If Data");
        } else {
            progressDialog.show();
            list = Stash.getArrayList(Constants.eventsTab, TabsModel.class);
            getTabs();
            Log.d("testing123", "else Data");
        }*/

    }
    private void getLocalTabs() {
        ArrayList<TabLocal> tabLocals = new ArrayList<>();
        tabLocals = Stash.getArrayList(Constants.localTab, TabLocal.class);
        for (int i = 0; i < tabLocals.size(); i++) {
            for (int j=0; j< list.size(); j++){
                if (list.get(j).getName().equals(tabLocals.get(i).getName())) {
                    list.get(j).setId(tabLocals.get(i).getId());
                }
            }
          // Stash.put(Constants.eventsTab, list);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(list, Comparator.comparing(TabsModel::getId));
        }
        //Collections.reverse(list);
        setTabs();
    }

    private void getTabs() {
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(Constants.categoriesEvent);
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                Log.d("TAG", "compress: ERROR: " + e.toString());
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            Log.d("TAG", "data: " + htmlData);

                runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(htmlData);
                        JSONArray jsonArray = json.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            for (int j=0; j< list.size(); j++){
                                if (list.get(j).getName().equals(obj.getString("name"))) {
                                    list.get(j).setId(obj.getInt("id"));
                                }
                            }
                            // Stash.put(Constants.channelsTab, list);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Collections.sort(list, Comparator.comparing(TabsModel::getId));
                        }
                        //Collections.reverse(list);
                        setTabs();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                });
        }).start();
    }

    private void setTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        ArrayList<String> t = Stash.getArrayList("hidden", String.class);
        for (TabsModel s : list) {
            //Toast.makeText(context, ""+s.getId(), Toast.LENGTH_SHORT).show();
            if(t.isEmpty() || t == null){
                CommonEventFragment fragment = new CommonEventFragment(s.getObject());
                adapter.addFrag(s.getObject(), s.getName());
            } else {
                for (String ss : t) {
                    if (!ss.equals(s.getName())){
                        CommonEventFragment fragment = new CommonEventFragment(s.getObject());
                        adapter.addFrag(s.getObject(), s.getName());
                    }
                }
            }
        }

        Log.d("testing123", "ViewPager Adapter before");
        binding.viewpager.setAdapter(adapter);

        Log.d("testing123", "ViewPager Adapter After");
        binding.tablayout.setupWithViewPager(binding.viewpager);
        progressDialog.dismiss();
    }

    private void getData() {
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(Constants.event);
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                Log.d("TAG", "compress: ERROR: " + e.toString());
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            Log.d("TAG", "data: " + htmlData);

                runOnUiThread(() -> {
                    try {
                        //Toast.makeText(context, "Html  " + htmlData, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject = new JSONObject(htmlData);
                        JSONObject data = jsonObject.getJSONObject("data");
                        Stash.put(Constants.eventsData, data);
                        /*ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity()
                                .getSupportFragmentManager());*/

                        for (String s : iterate(data.keys())) {
                            JSONArray channelsArray = data.getJSONArray(s);
                            // Toast.makeText(context, "Data " + channelsArray.toString(), Toast.LENGTH_SHORT).show();
                            //CommonEventFragment fragment = new CommonEventFragment(channelsArray.toString());
                            //adapter.addFrag(fragment, s);
                            TabsModel model = new TabsModel(s, channelsArray.toString(), false);
                            list.add(model);
                        }
                        Stash.put(Constants.eventsTab, list);
                        boolean ta = Stash.getBoolean(Constants.isAdjusted, false);
                        if (ta){
                            getLocalTabs();
                        } else {
                            getTabs();
                        }
                        /*binding.viewpager.setAdapter(adapter);
                        binding.tablayout.setupWithViewPager(binding.viewpager);*/
                        //progressDialog.dismiss();

                    } catch (JSONException error) {
                        Toast.makeText(EventsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        }).start();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("testing123", "getCount: " + mFragmentList.size());
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            Log.d("testing123", "getCount: " + mFragmentList.size());
            return mFragmentList.size();
        }

        public void addFrag(String obj, String title) {
            mFragmentList.add(CommonEventFragment.newInstance(obj));
            mFragmentTitleList.add(title);

            Log.d("testing123", "getCount: " + mFragmentList.size());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private <T> Iterable<T> iterate(final Iterator<T> i) {
        return () -> i;
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

}