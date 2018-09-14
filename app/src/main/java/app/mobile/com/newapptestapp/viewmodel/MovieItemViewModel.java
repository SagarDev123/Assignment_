package app.mobile.com.newapptestapp.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import app.mobile.com.newapptestapp.R;
import app.mobile.com.newapptestapp.Utility.Constants;
import app.mobile.com.newapptestapp.model.Movie;
import app.mobile.com.newapptestapp.model.Result;

public class MovieItemViewModel extends BaseObservable {
    public Result result;
    public String mTitle;
    public String mOverView;
    public String mPosterPath;
    public String mReleaseDate;


    public MovieItemViewModel(Result result) {
        this.result = result;
        getmOverView();
        getmReleaseDate();
        getmTitle();
        getmPosterPath();
    }

    public String getmTitle() {
        mTitle = result.getTitle();
        return mTitle;
    }

    public String getmOverView() {
        mOverView = result.getOverview();
        return mOverView;
    }

    public String getmPosterPath() {
        mPosterPath = Constants.IMAGE_BASE_URL+result.getPosterPath();
        return mPosterPath;
    }

    public String getmReleaseDate() {
        mReleaseDate = result.getReleaseDate();
        return mReleaseDate;
    }



    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(view);
    }
}
