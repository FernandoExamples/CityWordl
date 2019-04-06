package com.tecno.udemy.fernando.cityworld.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tecno.udemy.fernando.cityworld.R;
import com.tecno.udemy.fernando.cityworld.model.City;

import org.w3c.dom.Text;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private OnButtonClickListener buttonListener;
    private OnClickListener clickListener;
    private List<City> cities;
    private Context context;
    private int layout;

    public CityAdapter(List cities, Context context, int layout, OnButtonClickListener buttonListener, OnClickListener clickListener) {
        this.buttonListener = buttonListener;
        this.clickListener = clickListener;
        this.cities = cities;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        viewHolder.bind(cities.get(position), clickListener, buttonListener);
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView cityImage;
        TextView textViewName;
        TextView textViewDescription;
        TextView textViewStars;
        Button btnDelete;

        public ViewHolder(View view){
            super(view);
            cityImage = view.findViewById(R.id.imageViewCity);
            textViewName = view.findViewById(R.id.textViewName);
            textViewDescription = view.findViewById(R.id.textViewDescription);
            textViewStars = view.findViewById(R.id.textViewStars);
            btnDelete = view.findViewById(R.id.btnDelete);
        }

        public void bind(final City city, final OnClickListener clickListener, final OnButtonClickListener buttonListener){
            Picasso.get().load(city.getLinkBackground()).fit().into(cityImage);
            textViewName.setText(city.getName());
            textViewDescription.setText(city.getDescription());
            textViewStars.setText(city.getNumStarts()+"");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClickListener(city, getAdapterPosition());
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonListener.onButtonClick(city, getAdapterPosition());
                }
            });

        }

    }

    public interface OnButtonClickListener{
        void onButtonClick(City city, int position);
    }

    public interface OnClickListener{
        void onClickListener(City city, int position);
    }
}
