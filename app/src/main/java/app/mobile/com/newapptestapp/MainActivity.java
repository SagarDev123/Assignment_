package app.mobile.com.newapptestapp;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.mobile.com.newapptestapp.Adapter.MovieAdapter;
import app.mobile.com.newapptestapp.Rest.ApiCall;
import app.mobile.com.newapptestapp.Utility.Constants;
import app.mobile.com.newapptestapp.Utility.InternetConnectivity;
import app.mobile.com.newapptestapp.databinding.ActivityMainBinding;


import app.mobile.com.newapptestapp.model.Movie;
import app.mobile.com.newapptestapp.model.Result;
import app.mobile.com.newapptestapp.viewmodel.MovieItemViewModel;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private ActivityMainBinding mViewBinding;
    ApiCall mApiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mViewBinding.movieList.setLayoutManager(new LinearLayoutManager(this));
        mViewBinding.searchMovie.setOnQueryTextListener(this);
        mViewBinding.searchMovie.setOnCloseListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (InternetConnectivity.checkInternetConenction(this)) {
            searchMovieList(query);
        } else {
            Toast.makeText(this, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void searchMovieList(String query) {

        mApiCall = new ApiCall(mViewBinding.progressBar, new ApiCall.Delegate() {
            @Override
            public void onSuccess(Movie movie) {
                List<MovieItemViewModel> lists = new ArrayList<>();
                for (Result result : movie.getResults()) {
                    MovieItemViewModel movieItemViewModel = new MovieItemViewModel(result);
                    lists.add(movieItemViewModel);
                }

                if (lists.size() > 0) {
                    mViewBinding.movieList.setVisibility(View.VISIBLE);
                    mViewBinding.movieList.setAdapter(new MovieAdapter(lists));
                }else {
                    mViewBinding.movieList.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Object t) {
                mViewBinding.movieList.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Error : " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        }, this,query);
        mApiCall.executeApi();
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onClose() {
        mViewBinding.movieList.setVisibility(View.GONE);
        return false;
    }
}
