package com.moutamid.streamingapp;

import com.moutamid.streamingapp.models.ChannelsModel;

public interface Clicklistners {
    void click (ChannelsModel model);
    void favrouite(ChannelsModel model, boolean isfvrt);
}

