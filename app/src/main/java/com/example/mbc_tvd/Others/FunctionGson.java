package com.example.mbc_tvd.Others;

import com.example.mbc_tvd.Values.GetSetValues;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class FunctionGson {

    public List<GetSetValues> getResponses(String data) {
        return Arrays.asList(new Gson().fromJson(data, GetSetValues[].class));
    }
}
