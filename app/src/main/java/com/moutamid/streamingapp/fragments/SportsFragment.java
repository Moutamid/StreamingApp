package com.moutamid.streamingapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.moutamid.streamingapp.R;
import com.moutamid.streamingapp.VolleySingleton;
import com.moutamid.streamingapp.adapters.ChannelsAdapter;
import com.moutamid.streamingapp.databinding.FragmentSportsBinding;
import com.moutamid.streamingapp.models.ChannelsModel;
import com.moutamid.streamingapp.models.StreamLinksModel;

import java.util.ArrayList;

public class SportsFragment extends Fragment {
    FragmentSportsBinding binding;
    private RequestQueue requestQueue;
    Context context;
    ArrayList<ChannelsModel> channelsList;
    ArrayList<StreamLinksModel> streamLinks;
    ChannelsAdapter adapter;
    int MY_SOCKET_TIMEOUT_MS = 10000;

    public SportsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSportsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = view.getContext();

        channelsList = new ArrayList<>();
        streamLinks = new ArrayList<>();


        binding.recycler.setHasFixedSize(false);

        requestQueue = VolleySingleton.getmInstance(context).getRequestQueue();

        // fetchData();

        //fetchDataByMoutamid();

        return view;
    }

   /* private void fetchDataByMoutamid() {
        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("http://95.217.210.178/api/v1/channel/");
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

            Log.d("TAG", "compress: " + htmlData);

            requireActivity().runOnUiThread(() -> {
                try {
                    JSONObject jsonObject = new JSONObject(htmlData);
                    JSONObject data = jsonObject.getJSONObject("data");

                    JSONArray sports = data.getJSONArray("sports");

                    for (int i = 0; i < sports.length(); i++) {
                        JSONObject obj = sports.getJSONObject(i);
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

                        for (int j = 0; j < streamingLinks.length(); j++) {
                            JSONObject stream = streamingLinks.getJSONObject(j);
                            StreamLinksModel model1 = new StreamLinksModel();
                            model1.set_id(stream.getString("_id"));
                            model1.setName(stream.getString("name"));
                            model1.setToken(stream.getString("token"));
                            model1.setPriority(stream.getInt("priority"));
                            model1.setRequest_header(stream.getString("request_header"));
                            model1.setPlayer_header(stream.getString("player_header"));
                            streamLinks.add(model1);
                        }

                        channelsModel.setStreamingLinks(streamLinks);

                        channelsList.add(channelsModel);

                    }
                    if(channelsList.size() == 1){
                        binding.recycler.setLayoutManager(new GridLayoutManager(context, 1));
                    } else if(channelsList.size() == 2){
                        binding.recycler.setLayoutManager(new GridLayoutManager(context, 2));
                    } else {
                        binding.recycler.setLayoutManager(new GridLayoutManager(context, 3));
                    }
                    adapter = new ChannelsAdapter(context, channelsList);
                    binding.recycler.setAdapter(adapter);

                } catch (JSONException error) {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }).start();
    }

    private void fetchData() {
        String url = Constants.channel;

        *//*try {
            URL url = new URL(Constants.channel);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            Toast.makeText(context, urlConnection.getResponseMessage(), Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            Toast.makeText(context, "ee : " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }*//*


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");

                        JSONArray sports = data.getJSONArray("sports");

                        for (int i = 0; i < sports.length(); i++) {
                            Toast.makeText(context, "FOR", Toast.LENGTH_LONG).show();
                            JSONObject obj = sports.getJSONObject(i);
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

                            for (int j = 0; j < streamingLinks.length(); j++) {
                                JSONObject stream = streamingLinks.getJSONObject(j);
                                StreamLinksModel model1 = new StreamLinksModel();
                                model1.set_id(stream.getString("_id"));
                                model1.setName(stream.getString("name"));
                                model1.setToken(stream.getString("token"));
                                model1.setPriority(stream.getInt("priority"));
                                model1.setRequest_header(stream.getString("request_header"));
                                model1.setPlayer_header(stream.getString("player_header"));
                                streamLinks.add(model1);
                            }

                            channelsModel.setStreamingLinks(streamLinks);

                            channelsList.add(channelsModel);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // MENE YE TOAST REMOVE KAR DIA HE QK YE METHOD SAI HE :)
                        //Toast.makeText(context, "eee : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    adapter = new ChannelsAdapter(context, channelsList);
                    binding.recycler.setAdapter(adapter);

                },
                error -> {
                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }*/
}