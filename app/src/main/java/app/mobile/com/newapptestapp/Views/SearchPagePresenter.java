package app.mobile.com.newapptestapp.Views;

import android.content.Context;

public interface SearchPagePresenter {
    void setView(SearchPageView view);
    void onSearch(String query,Context context);
    void onLoadMore(int pageNumber);
}
