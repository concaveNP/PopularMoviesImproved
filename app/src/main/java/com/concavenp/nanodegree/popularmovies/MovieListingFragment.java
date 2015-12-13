package com.concavenp.nanodegree.popularmovies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

/**
 * A fragment representing a list of MovieItem(s).
 * <p>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class MovieListingFragment extends Fragment {

    /**
     * The logging tag string to be associated with log data for this class
     */
    private static final String TAG = MovieListingFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RequestQueue mRequestQueue;

    private SharedPreferences mPrefs;

    String mSortOrder;

    /**
     * The fragment's GridView.
     */
    private AbsListView mGridView;

    /**
     * The Adapter which will be used to populate the GridView with Views.
     */
    private MovieAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static MovieListingFragment newInstance(String param1, String param2) {
        MovieListingFragment fragment = new MovieListingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieListingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movieitem_grid, container, false);

        mGridView = (GridView)view.findViewById(R.id.main_Movies_GridView);
        mAdapter = new MovieAdapter(getActivity(), R.id.main_Movies_GridView, new MovieItems());
        mGridView.setAdapter(mAdapter);

        // Instantiate the RequestQueue.
        mRequestQueue = Volley.newRequestQueue(getActivity());

        mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mSortOrder = mPrefs.getString(getResources().getString(R.string.sorting_preference_type_key),getResources().getString(R.string.default_sorting_preference_value)) ;

        requestData();

        return view;
    }


    public void requestData() {

        String url = "https://api.themoviedb.org/3/discover/movie?sort_by=" + mSortOrder + "&api_key=" + getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN);

        // Request a string response from the provided URL.
        GsonRequest<MovieItems> request = new GsonRequest<>(url, MovieItems.class, null, new Response.Listener<MovieItems>() {
            @Override
            public void onResponse(MovieItems response) {
                // First clear out any old data
                mAdapter.clear();

                // Add the new data
                mAdapter.add(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                Log.d(TAG, "some error");
            }
        });

        // Add the request to the RequestQueue.
        mRequestQueue.add(request);
    }


}
