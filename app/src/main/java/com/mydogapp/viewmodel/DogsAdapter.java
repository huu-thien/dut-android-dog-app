package com.mydogapp.viewmodel;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.mydogapp.R;
import com.mydogapp.databinding.DogItemBinding;
import com.mydogapp.model.DogBreed;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DogsAdapter extends RecyclerView.Adapter<DogsAdapter.ViewHolder> implements Filterable {

    private List<DogBreed> dogBreeds;
    private List<DogBreed> oldDogBreeds;

    public DogsAdapter(ArrayList<DogBreed> dogBreeds) {
        this.dogBreeds = dogBreeds;
        this.oldDogBreeds = dogBreeds;
    }
    @NonNull
    @Override
    public DogsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dog_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DogsAdapter.ViewHolder holder, int position) {
        Picasso.get().load(dogBreeds.get(position).getUrl()).into(holder.ivAvatar);
        holder.tvName.setText(dogBreeds.get(position).getName());
        holder.tvOrigin.setText(dogBreeds.get(position).getBredFor());
    }

    @Override
    public int getItemCount() {
        return dogBreeds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public TextView tvOrigin;
        public ImageView ivAvatar;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvOrigin = view.findViewById(R.id.tv_bred_for);
            ivAvatar = view.findViewById(R.id.iv_url);

            // su kien kich vao 1 item cua recycle view
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DogBreed dogBreed = dogBreeds.get(getAdapterPosition());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("dogBreed", dogBreed);
                    Navigation.findNavController(view).navigate(R.id.detailsFragment, bundle);
                }
            });
            view.findViewById(R.id.iv_url).setOnTouchListener(new onSwipeTouchListener() {
//            view.setOnTouchListener(new onSwipeTouchListener() {
                @Override

                public void onSwipeLeft() {
                    if(view.findViewById(R.id.layout_1).getVisibility() == View.GONE) {
                        view.findViewById(R.id.layout_1).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.layout_2).setVisibility(View.GONE);
                    } else {
                        view.findViewById(R.id.layout_2).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.layout_1).setVisibility(View.GONE);
                    }
                }

                @Override
                public void onSwipeRight() {
                    if(view.findViewById(R.id.layout_1).getVisibility() == View.GONE) {
                        view.findViewById(R.id.layout_1).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.layout_2).setVisibility(View.GONE);
                    } else {
                        view.findViewById(R.id.layout_2).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.layout_1).setVisibility(View.GONE);
                    }
                }
            });
            view.findViewById(R.id.layout_2).setOnTouchListener(new onSwipeTouchListener() {
                public void onSwipeRight() {
                    if(view.findViewById(R.id.layout_1).getVisibility() == View.GONE) {
                        view.findViewById(R.id.layout_1).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.layout_2).setVisibility(View.GONE);
                    } else {
                        view.findViewById(R.id.layout_2).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.layout_1).setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String input = charSequence.toString();
                List<DogBreed> DogtListFilter = new ArrayList<>();
                if (input.isEmpty()) {
                    DogtListFilter.addAll(oldDogBreeds);
                } else {
                    for (DogBreed dog : oldDogBreeds) {
                        if (dog.getName().toLowerCase().contains(input.toLowerCase())) {
                            DogtListFilter.add(dog);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = DogtListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dogBreeds = (List<DogBreed>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // Su kien onSwipe
    public class onSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());



        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }


        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 50;
            private static final int SWIPE_VELOCITY_THRESHOLD = 50;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onClick();
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                        }
                        result = true;
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                    result = true;

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }

        public void onSwipeRight() {
        }

        public void onSwipeLeft() {
        }

        public void onSwipeTop() {
        }

        public void onSwipeBottom() {
        }

        public void onClick() {
        }

    }
}
