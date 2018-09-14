package app.mobile.com.newapptestapp.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import app.mobile.com.newapptestapp.databinding.MovieItemBinding;
import app.mobile.com.newapptestapp.viewmodel.MovieItemViewModel;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    List<MovieItemViewModel> movieItemViewModels;
    LayoutInflater layoutInflater;

    public MovieAdapter(List<MovieItemViewModel> movieItemViewModels) {
        this.movieItemViewModels = movieItemViewModels;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }

        MovieItemBinding binding = MovieItemBinding.inflate(layoutInflater,parent,false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        MovieItemViewModel movieItemViewModel=movieItemViewModels.get(position);
        holder.bind(movieItemViewModel);

    }

    @Override
    public int getItemCount() {
        return movieItemViewModels.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
       private MovieItemBinding movieItemBinding;

        public MovieViewHolder(MovieItemBinding movieItemBinding) {
            super(movieItemBinding.getRoot());
            this.movieItemBinding = movieItemBinding;
        }

        public  void bind(MovieItemViewModel movieItemViewModel){
             this.movieItemBinding.setItem(movieItemViewModel);
        }
        public MovieItemBinding getMovieItemBinding(){
            return this.movieItemBinding;
        }
    }
}
