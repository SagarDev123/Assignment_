package app.mobile.com.newapptestapp.Views;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.paginate.Paginate;

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
    private Paginate paginate;
    private boolean loadingInProgress = false;
    private boolean hasLoadedAllItems = false;
    private int offset = 1;

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
       // setUpPagination();
        /*mViewBinding.movieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mViewBinding.movieList.canScrollVertically(1)){
                    Toast.makeText(MainActivity.this, "Last", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


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

        if (movieItemViewModels.size() > 0) {
            lists.addAll(movieItemViewModels);
        }

        Log.e("LIST SIZE",""+lists.size());
        

        mViewBinding.movieList.setAdapter(new MovieAdapter(lists));
        mViewBinding.movieList.scrollToPosition(scrollToPosition);
        if(offset==1){
            setUpPagination();
        }

    }



    private void setUpPagination() {
        Log.e("ON SETUP PAGINATION","DATA");
        if (paginate != null) {
            paginate.unbind();
        }

        Paginate.Callbacks callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                // Load next page of data (e.g. network or database)
                offset++;
                Log.e("ON LOAD MORE","offset: "+offset+" total pages:"+totalPages );
                Log.e("total result:"+totalResult,"listsize "+lists.size());
                if (offset == 2 || totalPages>=offset && totalResult>lists.size()) {
                    mSearchPagePresenter.onLoadMore(offset);
                } else {
                    Log.e("ON ELSE","ELSE");
                    hasLoadedAllItems = true;
                }
            }

            @Override
            public boolean isLoading() {
                mViewBinding.loadMoreProgressBar.setVisibility(View.VISIBLE);
                // Indicate whether new page loading is in progress or not
                return loadingInProgress;
            }

            @Override
            public boolean hasLoadedAllItems() {
                mViewBinding.loadMoreProgressBar.setVisibility(View.GONE);
                // Indicate whether all data (pages) are loaded or not
                return hasLoadedAllItems;
            }
        };
        paginate = Paginate.with(mViewBinding.movieList, callbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();
    }


}
