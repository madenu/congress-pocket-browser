package com.cs5520.numad20su_congressmobile.content.services;


import android.content.Context;
import com.android.volley.Response.Listener;
import com.cs5520.numad20su_congressmobile.content.VolleySingleton;
import com.cs5520.numad20su_congressmobile.content.models.Committee;
import com.cs5520.numad20su_congressmobile.content.services.AbstractViewContent.ProPublicaRequest;
import com.cs5520.numad20su_congressmobile.layoutAdapters.CommitteesRecyclerViewAdapter;
import com.google.gson.Gson;
import java.util.List;


public class MyFeedCommitteesContent implements Listener<String> {

    private static final String ENDPOINT = "https://api.propublica.org/congress/v1/116/";

    private final VolleySingleton volleySingleton;
    private final CommitteesRecyclerViewAdapter viewAdapter;

    public MyFeedCommitteesContent(List<String> committeeIds,
        Context context,
        CommitteesRecyclerViewAdapter viewAdapter) {
        this.volleySingleton = VolleySingleton.getInstance(context);
        this.viewAdapter = viewAdapter;
        requestCommittees(committeeIds);
    }

    private void requestCommittees(List<String> committeeIds) {
        String chamber = "";
        for (String id : committeeIds) {
            switch (id.substring(0, 1)) {
                case "H":
                    chamber = "house";
                    break;
                case "J":
                    chamber = "joint";
                    break;
                case "S":
                    chamber = "senate";
                    break;
            }
            this.volleySingleton.getRequestQueue()
                .add(
                    new ProPublicaRequest(
                        String.format("%s%s/committees/%s.json", ENDPOINT, chamber, id),
                        this, null));
        }
    }

    @Override
    public void onResponse(String jsonText) {
        Gson gson = new Gson();
        Response response = gson.fromJson(jsonText, Response.class);
        viewAdapter.add(response.results.get(0));
    }

    public CommitteesRecyclerViewAdapter getAdapter() {
        return this.viewAdapter;
    }

    static class Response {

        List<Committee> results;
    }
}


