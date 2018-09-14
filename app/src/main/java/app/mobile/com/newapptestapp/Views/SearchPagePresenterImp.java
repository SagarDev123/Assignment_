package app.mobile.com.newapptestapp.Views;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.mobile.com.newapptestapp.Adapter.MovieAdapter;
import app.mobile.com.newapptestapp.Rest.ApiCall;
import app.mobile.com.newapptestapp.model.Movie;
import app.mobile.com.newapptestapp.model.Result;
import app.mobile.com.newapptestapp.viewmodel.MovieItemViewModel;

public class SearchPagePresenterImp implements SearchPagePresenter{

    private SearchPageView searchPageView;
    Context context;
    String query;

    @Override
    public void setView(SearchPageView view) {
        this.searchPageView=view;
    }

    @Override
    public void onSearch(String query, Context context) {
        this.context=context;
        this.query=query;
       ApiCall mApiCall = new ApiCall(new ApiCall.Delegate() {
            @Override
            public void onSuccess(Movie movie) {
                List<MovieItemViewModel> movieItemViewModelList= new ArrayList<>();
                for (Result result : movie.getResults()) {
                    MovieItemViewModel movieItemViewModel = new MovieItemViewModel(result);
                    movieItemViewModelList.add(movieItemViewModel);
                }
                searchPageView.onSearchCompleted(movieItemViewModelList,movie.getTotalPages(),movie.getPage(),movie.getTotalResults());
            }

            @Override
            public void onFailure(Object t) {
                searchPageView.onError(t.toString());
            }
        }, context,query);
        mApiCall.executeApi();
    }

    @Override
    public void onLoadMore(int page) {
        ApiCall mApiCall = new ApiCall(new ApiCall.Delegate() {
            @Override
            public void onSuccess(Movie movie) {
                List<MovieItemViewModel> movieItemViewModelList= new ArrayList<>();
                for (Result result : movie.getResults()) {
                    MovieItemViewModel movieItemViewModel = new MovieItemViewModel(result);
                    movieItemViewModelList.add(movieItemViewModel);
                }
                searchPageView.onSearchCompleted(movieItemViewModelList,movie.getTotalPages(),movie.getPage(),movie.getTotalResults());
            }

            @Override
            public void onFailure(Object t) {
                searchPageView.onError(t.toString());
            }
        }, context,query);
        mApiCall.executeLoadMoreApi(page);
    }
}
