package FEEDING.SYSTEM;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.MyHolder>{

    Context context;
    private List<LogsActivityModel> list;


    public LogsAdapter(Context context, List<LogsActivityModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public LogsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.logs_recycle_view, parent,false);

        return new LogsAdapter.MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogsAdapter.MyHolder holder, int position) {

        String dt = list.get(position).getDate();
        String tm = list.get(position).getTime();
        String st = list.get(position).getStatus();

        holder.Date.setText(dt);
        holder.Time.setText(tm);
        holder.Status.setText(st);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyHolder extends RecyclerView.ViewHolder{
        TextView Date, Time, Status;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Date = itemView.findViewById(R.id.date_logs);
            Time = itemView.findViewById(R.id.time_logs);
            Status = itemView.findViewById(R.id.user_logs);


        }
    }
}
