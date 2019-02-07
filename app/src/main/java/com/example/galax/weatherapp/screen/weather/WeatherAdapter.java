package com.example.galax.weatherapp.screen.weather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.models.WeatherForecast;
import com.example.galax.weatherapp.data.repository.WeatherLocalRepositoryImpl;
import com.example.galax.weatherapp.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List <Weather> weathers;
    private String units;
    private WeatherLocalRepositoryImpl weatherLocalRepository;

    public WeatherAdapter(List<Weather> weathers, WeatherLocalRepositoryImpl weatherLocalRepository) {
        this.weathers = weathers;
        this.weatherLocalRepository = weatherLocalRepository;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather weather = weathers.get(position);

        if(Paper.book().read(Constants.UNIT_TEMP) == null) {
            Paper.book().write(Constants.UNIT_TEMP, App.getInstance().getString(R.string.celsius));
            units = Paper.book().read(Constants.UNIT_TEMP);
        }else units = Paper.book().read(Constants.UNIT_TEMP);


        holder.clouds.setImageResource(setIconView(weather.getConditionId()));
        holder.temperature.setText(Double.toString(weather.getTemp())+ " " + units);
        holder.city.setText(weather.getCity());
        holder.weather.setText(weather.getDescription());
        holder.deleteBtn.setOnClickListener(v ->{
                weatherLocalRepository.deleteWeather(weathers.get(position)).subscribe();
                weathers.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,weathers.size());

        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void notifyData(List<Weather> weathers) {
        this.weathers = weathers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView clouds;
        TextView temperature;
        TextView city;
        TextView weather;
        View deleteBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            clouds = itemView.findViewById(R.id.clouds);
            temperature = itemView.findViewById(R.id.temperature);
            city = itemView.findViewById(R.id.city);
            weather = itemView.findViewById(R.id.weather);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
        }

    }
    private int setIconView(int conditionId){

        if (conditionId>=200 && conditionId<210){
            return R.drawable.ic_thunderstorm_with_rain;
        }
        if(conditionId>=210 && conditionId<300){
            return R.drawable.ic_thunderstorm;
        }
        if(conditionId>=500 && conditionId<600){
            return R.drawable.ic_rain;
        }
        if(conditionId>=600 && conditionId<700){
            return R.drawable.ic_snowy;
        }
        if(conditionId>=300 && conditionId<500){
            return R.drawable.ic_drizzle;
        }
        if(conditionId>=701 && conditionId<800){
            return R.drawable.ic_fog;
        }
        if(conditionId==800){
            return R.drawable.ic_clear;
        }
        if(conditionId==801){
            return R.drawable.ic_few_clouds;
        }
        if(conditionId==802){
            return R.drawable.ic_cloud;
        }
        if(conditionId>=803){
            return R.drawable.ic_clouds;
        }

        return  R.drawable.ic_error;
    }
}
