package com.concavenp.nanodegree.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    private OnFragmentInteractionListener mListener;
    String mSortOrder = "popularity.desc";

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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = "https://api.themoviedb.org/3/discover/movie?sort_by=" + mSortOrder + "&api_key=" + getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN);

        // Request a string response from the provided URL.
        GsonRequest<MovieItems> request = new GsonRequest<>(url, MovieItems.class, null, new Response.Listener<MovieItems>() {
            @Override
            public void onResponse(MovieItems response) {
                // First clear out any old data
           //     mAdapter.clear();

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
        queue.add(request);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String id);
    }

}
