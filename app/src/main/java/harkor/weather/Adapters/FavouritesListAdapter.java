package harkor.weather.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import harkor.weather.Model.SingleCityPOJO;
import harkor.weather.R;
import harkor.weather.Services.RealmDatabaseController;
import java.util.List;


public class FavouritesListAdapter extends RecyclerView.Adapter<FavouritesListAdapter.MyViewHolder>{
    private List<SingleCityPOJO> favourites;
    private ItemClickListener itemClickListener;
    private String mainCity;

    public void setFavourites(List<SingleCityPOJO> favourites) {
        this.favourites = favourites;
    }

    public void setMainColor(String mainCity){
        this.mainCity=mainCity;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView singleCityName;
        public ImageView deleteFromFavourites;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            singleCityName=itemView.findViewById(R.id.city_name);
            deleteFromFavourites=itemView.findViewById(R.id.unfavourite_icon);
            deleteFromFavourites.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener!=null){
                itemClickListener.OnDeleteClick(v,getAdapterPosition());
            }
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FavouritesListAdapter(Context context, List<SingleCityPOJO> favourites) {
        this.favourites = favourites;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.favoutites_list_item,
                viewGroup,false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        final SingleCityPOJO element=favourites.get(i);
        myViewHolder.singleCityName.setText(favourites.get(i).getCity());
        if(mainCity!=null){
            if(element.getCity().equals(mainCity)){
                myViewHolder.itemView.setBackgroundColor(Color.parseColor("#D4E79E"));
            }else{
                myViewHolder.itemView.setBackground(null);
            }
        }

         myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 RealmDatabaseController realmDatabaseController=new RealmDatabaseController();
                 realmDatabaseController.setMainCity(element);
                 Toast.makeText(v.getContext(),R.string.main_city_changed,Toast.LENGTH_SHORT).show();
                 mainCity=element.getCity();
                 notifyDataSetChanged();
             }
         });
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }


    public interface ItemClickListener{
        void OnDeleteClick(View view, int position);
    }
}

