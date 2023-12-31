package com.moutamid.streamingapp.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fxn.stash.Stash;
import com.google.common.base.Function;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.moutamid.streamingapp.Clicklistners;
import com.moutamid.streamingapp.Constants;
import com.moutamid.streamingapp.adapters.CountriesWiseAdapter;
import com.moutamid.streamingapp.databinding.FragmentCommonEventBinding;
import com.moutamid.streamingapp.dialog.LinkDialog;
import com.moutamid.streamingapp.dialog.VideoPlayerDialog;
import com.moutamid.streamingapp.models.ChannelsModel;
import com.moutamid.streamingapp.models.CountriesChannelModel;
import com.moutamid.streamingapp.models.StreamLinksModel;

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
import java.util.LinkedHashSet;
import java.util.Set;

public class CommonEventFragment extends Fragment {
    String title;
    Context context;
    FragmentCommonEventBinding binding;
    ArrayList<ChannelsModel> channelsList;
    //ArrayList<StreamLinksModel> streamLinks;
    ArrayList<CountriesChannelModel> countriesChannel;
    CountriesWiseAdapter adapter;
    ArrayList<ChannelsModel> favrtList;

    public CommonEventFragment() {
        // Required empty public constructor
    }

    public static CommonEventFragment newInstance(String response) {
        Bundle args = new Bundle();
        args.putString("eventResponse", response);
        CommonEventFragment fragment = new CommonEventFragment();
        fragment.setArguments(args);
        Log.d("testing123", "newInstance: "+response);
        return fragment;
    }

    public CommonEventFragment(String title) {
        this.title = title;
        Log.d("testing123", "OK  " + title);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("testing123", "OK  onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("testing123", "OK  onResume");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.title = getArguments().getString("eventResponse", "");
        Log.d("testing123", "OK  onCreate  " + this.title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCommonEventBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        channelsList = new ArrayList<>();
        //streamLinks = new ArrayList<>();
        countriesChannel = new ArrayList<>();

        binding.recycler.setHasFixedSize(false);

        Log.d("testing123", "onCreateView  ");
        favrtList = Stash.getArrayList(Constants.favrtList, ChannelsModel.class);
        if (favrtList == null){
            favrtList = new ArrayList<>();
        }
        // Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
        try {
            JSONArray jsonArray = new JSONArray(title);
            Log.d("testing123", "JSON TRY");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                ChannelsModel channelsModel = new ChannelsModel();
                channelsModel.set_id(obj.getString("_id"));
                channelsModel.setName(obj.getString("name"));
                channelsModel.setCategory(obj.getString("category"));
                channelsModel.setImage(obj.getString("image"));
                channelsModel.setImageUrl(obj.getString("imageUrl"));
                channelsModel.setHide(obj.getBoolean("hide"));
                channelsModel.setCountry(obj.getString("country"));
                channelsModel.setID(obj.getInt("ID"));
                channelsModel.set__v(obj.getInt("__v"));

                JSONArray streamingLinks = obj.getJSONArray("streamingLinks");
                ArrayList<StreamLinksModel> streamLinks = new ArrayList<>();
                if (streamingLinks.length() > 0) {
                    for (int j = 0; j < streamingLinks.length(); j++) {
                        Log.d("testing123", "For TRY");
                        JSONObject stream = streamingLinks.getJSONObject(j);
                        StreamLinksModel model1 = new StreamLinksModel();
                        model1.set_id(stream.getString("_id"));
                        model1.setName(stream.getString("name"));
                        model1.setToken(stream.getString("token"));
                        model1.setPriority(stream.getInt("priority"));
                        model1.setRequest_header(stream.getString("request_header"));
                        model1.setPlayer_header(stream.getString("player_header"));
                        model1.setStream_link(stream.getString("url"));
                        streamLinks.add(model1);
                        channelsModel.setStreamingLinks(streamLinks);
                    }
                } else {
                    channelsModel.setStreamingLinks(streamLinks);
                }

                channelsList.add(channelsModel);

//                countriesChannel.add(new CountriesChannelModel(channelsModel.getCountry(), channelsList));
//                Log.d("testing123", "channelList Down");
            }

                String TAG = "MOB";

                ArrayList<String> countriesAll = new ArrayList<>();

                ListMultimap<String, ChannelsModel> result =
                        Multimaps.index(channelsList, new Function<ChannelsModel, String>() {
                            @Override
                            public String apply(ChannelsModel input) {
                                Log.d(TAG, "apply: "+input.getCountry());
                                countriesAll.add(input.getCountry());
                                return input.getCountry();
                            }
                        });
                Log.d(TAG, "countriesAll: "+countriesAll);

                // list is some List of Strings
                Set<String> countries = new LinkedHashSet<>(countriesAll);
                Log.d(TAG, "countries: "+countries);

                for (String countryName: countries){
                    countriesChannel.add(new CountriesChannelModel(countryName, result.get(countryName)));

                    Log.d(TAG, "result "+countryName+" :"+result.get(countryName));
                    String names = "";
                    for (ChannelsModel channelModelw : result.get(countryName)){
                        names += " ,"+ channelModelw.getName();
                    }
                    Log.d(TAG, "channels: "+names);
                }

                Log.d(TAG, "onCreateView: ENDED");

                //Stash.put("newList", newList);
            getSorting();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("testing123", "Catch CommonEvents  - "+e.getMessage());
        }
       /*// Toast.makeText(context, "channel " + channelsList.size(), Toast.LENGTH_SHORT).show();
        binding.recycler.setLayoutManager(new GridLayoutManager(context, 3));

        adapter = new ChannelsAdapter(context, channelsList, clicklistners );

        Log.d("testing123", "adapter");
        binding.recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();*/

        return binding.getRoot();
    }

    private void getSorting() {
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL(Constants.countriesEvent);
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

            if (isAdded()) {
                requireActivity().runOnUiThread(() -> {
                    try {
                        JSONObject json = new JSONObject(htmlData);
                        JSONArray jsonArray = json.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            for (int j=0; j< countriesChannel.size(); j++){
                                if (countriesChannel.get(j).getName().equals(obj.getString("name"))) {
                                    countriesChannel.get(j).setId(obj.getInt("id"));
                                }
                            }
                            //Stash.put(Constants.channelsTab, countriesChannel);
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            Collections.sort(countriesChannel, Comparator.comparing(CountriesChannelModel::getId));
                        }
                        //Collections.reverse(list);
                        binding.recycler.setLayoutManager(new LinearLayoutManager(context));
                        adapter = new CountriesWiseAdapter(context, countriesChannel, clicklistners );
                        binding.recycler.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
            }
        }).start();
    }

    Clicklistners clicklistners = new Clicklistners() {
        @Override
        public void click(ChannelsModel model) {
            Stash.put(Constants.VIDEO_TITLE, model.getName());
            if (model.getStreamingLinks().size()>1) {
                LinkDialog ld = new LinkDialog(context, model);
                ld.show();
            } else if (model.getStreamingLinks().size() == 0) {
                Toast.makeText(context, "No Streaming link found", Toast.LENGTH_SHORT).show();
            } else {
                VideoPlayerDialog vd = new VideoPlayerDialog(context, model.getStreamingLinks().get(0), model);
                vd.showStream();
            }
        }

        @Override
        public void favrouite(ChannelsModel model, boolean isfvrt) {

        }
    };

}