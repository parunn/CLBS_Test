package test.clbs.android.example.com.clbsandroidtest.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import test.clbs.android.example.com.clbsandroidtest.R;
import test.clbs.android.example.com.clbsandroidtest.model.LocationListViewModel;

/**
 * Created by parunpichaiwong on 5/28/2017 AD.
 */

public class MapRecyclerAdapter extends RecyclerView.Adapter<MapRecyclerAdapter.ViewHolder> {

    private List<LocationListViewModel> locationList;
    private Context context;

    public MapRecyclerAdapter(Context context, List<LocationListViewModel> locationList) {
        this.locationList = locationList;
        this.context = context;
    }

    @Override
    public MapRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.map_search_result_list_row_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MapRecyclerAdapter.ViewHolder viewHolder, int i) {

        viewHolder.txtProvinceName.setText(locationList.get(i).getLocationName()); //Name
        viewHolder.txtDistanceFromUserUnit.setText(String.valueOf(locationList.get(i).getLocationDistance())); //Distance
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public LocationListViewModel getItem(int position) {
        return locationList.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtProvinceName;
        private TextView txtDistanceFromUserUnit;

        public ViewHolder(View view) {
            super(view);

            txtProvinceName = (TextView) view.findViewById(R.id.txtProvinceName);
            txtDistanceFromUserUnit = (TextView)view.findViewById(R.id.txtDistanceFromUserUnit);
        }
    }
}
