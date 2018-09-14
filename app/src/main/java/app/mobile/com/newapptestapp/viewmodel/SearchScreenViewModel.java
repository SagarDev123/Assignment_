package app.mobile.com.newapptestapp.viewmodel;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.mobile.com.newapptestapp.Adapter.MovieAdapter;
import app.mobile.com.newapptestapp.MainActivity;
import app.mobile.com.newapptestapp.Rest.ApiCall;
import app.mobile.com.newapptestapp.model.Movie;
import app.mobile.com.newapptestapp.model.Result;

public class SearchScreenViewModel {
   private ProgressBar progressBar;
   private Context context;

    public SearchScreenViewModel(ProgressBar mProgress, Context context) {
        this.progressBar = mProgress;
        this.context=context;
    }

    public void onSearch(String query) {
        ApiCall mApiCall;
        mApiCall = new ApiCall(progressBar, new ApiCall.Delegate() {
            @Override
            public void onSuccess(Movie movie) {
                List<MovieItemViewModel> lists = new ArrayList<>();
                for (Result result : movie.getResults()) {
                    MovieItemViewModel movieItemViewModel = new MovieItemViewModel(result);
                    lists.add(movieItemViewModel);
                }
            }

            @Override
            public void onFailure(Object t) {
            }
        }, context, query);
        mApiCall.executeApi();
    }

}
