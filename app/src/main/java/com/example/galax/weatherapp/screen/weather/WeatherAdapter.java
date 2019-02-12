package com.example.galax.weatherapp.screen.weather;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.galax.weatherapp.R;
import com.example.galax.weatherapp.base.App;
import com.example.galax.weatherapp.data.models.Weather;
import com.example.galax.weatherapp.data.repository.WeatherLocalRepositoryImpl;
import com.example.galax.weatherapp.screen.weather.event.ClickCityEvent;
import com.example.galax.weatherapp.ui.WeatherIcon;
import com.example.galax.weatherapp.utils.Constants;
import com.squareup.otto.Bus;

import java.util.List;

import io.paperdb.Paper;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private List <Weather> weathers;
    private String units;
    private WeatherLocalRepositoryImpl weatherLocalRepository;
    private Bus bus;



    public WeatherAdapter(List<Weather> weathers, WeatherLocalRepositoryImpl weatherLocalRepository, Bus bus) {
        this.weathers = weathers;
        this.weatherLocalRepository = weatherLocalRepository;
        this.bus = bus;
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


        holder.clouds.setImageResource(WeatherIcon.getIconView(weather.getConditionId()));
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

                bus.post(new ClickCityEvent(weather.getCity()));
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
}
