package app.mobile.com.newapptestapp.Views;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Toast;

import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import java.util.ArrayList;
import java.util.List;

import app.mobile.com.newapptestapp.Adapter.MovieAdapter;
import app.mobile.com.newapptestapp.R;
import app.mobile.com.newapptestapp.Utility.Constants;
import app.mobile.com.newapptestapp.Utility.InternetConnectivity;
import app.mobile.com.newapptestapp.Utility.ProgressDialogPage;
import app.mobile.com.newapptestapp.databinding.ActivityMainBinding;
import app.mobile.com.newapptestapp.viewmodel.MovieItemViewModel;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener, SearchPageView {

    private ActivityMainBinding mViewBinding;
    LinearLayoutManager linearLayoutManager;
    int pageNumber = 0;
    int totalPages = 0;
    int totalResult = 0;
    int scrollToPosition = 0;
    int maxItemsPerRequest = 0;
    List<MovieItemViewModel> lists;
    ProgressDialog mLoadMoreProgress;

    private SearchPagePresenter mSearchPagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mSearchPagePresenter = new SearchPagePresenterImp();
        mSearchPagePresenter.setView(this);
        ProgressDialogPage progressDialogPage = new ProgressDialogPage(this);
        mLoadMoreProgress = progressDialogPage.createProgress();
        lists = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(this);

        mViewBinding.movieList.setLayoutManager(linearLayoutManager);
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

    private void showProgress() {
        mViewBinding.movieList.setVisibility(View.GONE);
        mViewBinding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        mViewBinding.movieList.setVisibility(View.VISIBLE);
        mViewBinding.progressBar.setVisibility(View.GONE);
    }

    private void searchMovieList(String query) {

        if (InternetConnectivity.checkInternetConenction(this)) {
            showProgress();
            mSearchPagePresenter.onSearch(query, this);
        } else {
            Toast.makeText(this, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }

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

    private InfiniteScrollListener createInfiniteScrollListener() {

        InfiniteScrollListener infiniteScrollListener = new InfiniteScrollListener(maxItemsPerRequest, linearLayoutManager) {
            @Override
            public void onScrolledToEnd(final int firstVisibleItemPosition) {
                // load your items here
                // logic of loading items will be different depending on your specific use case

                // when new items are loaded, combine old and new items, pass them to your adapter
                // and call refreshView(...) method from InfiniteScrollListener class to refresh RecyclerView

                scrollToPosition = firstVisibleItemPosition;
                LoadMore();


            }
        };


        return infiniteScrollListener;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mLoadMoreProgress.isShowing()) {
            mLoadMoreProgress.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLoadMoreProgress.isShowing()) {
            mLoadMoreProgress.dismiss();
        }
    }

    private void LoadMore() {
        if (InternetConnectivity.checkInternetConenction(this)) {
            if (pageNumber != totalPages) {
                mLoadMoreProgress.show();
                mSearchPagePresenter.onLoadMore(pageNumber + 1);
            } else {
                Toast.makeText(this, "No more data....", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, Constants.NO_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String error) {
        hideProgress();
        Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchCompleted(List<MovieItemViewModel> movieItemViewModels, int totalPages, int pageNumber, int totalResult) {
        hideProgress();
        if (mLoadMoreProgress.isShowing()) {
            mLoadMoreProgress.dismiss();
        }
        this.totalPages = totalPages;
        this.pageNumber = pageNumber;
        this.totalResult = totalResult;

        maxItemsPerRequest = totalResult / totalPages;
        mViewBinding.movieList.addOnScrollListener(createInfiniteScrollListener());
        if (movieItemViewModels.size() > 0) {
            lists.addAll(movieItemViewModels);
        }
        mViewBinding.movieList.setAdapter(new MovieAdapter(lists));
        mViewBinding.movieList.scrollToPosition(scrollToPosition);
    }

}
