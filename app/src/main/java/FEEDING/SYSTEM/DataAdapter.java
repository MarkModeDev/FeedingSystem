package FEEDING.SYSTEM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyHolder> {

    Context context;
    private List<DataModel> list;

    public DataAdapter(Context context, List<DataModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public DataAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_recyle_view, parent,false);

        return new DataAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.MyHolder holder, int position) {
        String dt = list.get(position).getDate();
        String tm = list.get(position).getTime();
        String am = list.get(position).getAmmonia();
        String phl = list.get(position).getPhLevel();
        String tem = list.get(position).getTemperature();

        holder.Date.setText(dt);
        holder.Time.setText(tm);
        holder.ammonia.setText(am);
        holder.phlevel.setText(phl);
        holder.tempt.setText(tem);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView Date, Time, ammonia, phlevel, tempt;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Date = itemView.findViewById(R.id.hdt);
            Time = itemView.findViewById(R.id.ht);
            ammonia = itemView.findViewById(R.id.hamm);
            phlevel = itemView.findViewById(R.id.hph);
            tempt = itemView.findViewById(R.id.htmp);


        }
    }
}
