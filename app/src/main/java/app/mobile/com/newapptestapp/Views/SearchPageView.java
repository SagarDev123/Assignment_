package app.mobile.com.newapptestapp.Views;

import java.util.List;

import app.mobile.com.newapptestapp.viewmodel.MovieItemViewModel;

public interface SearchPageView {
    void onError(String error);
    void onSearchCompleted(List<MovieItemViewModel> lists,int totalPages,int pageNumber,int totalCount);
}
