package shm.dim.dailybudget.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import shm.dim.dailybudget.model.Costs;
import shm.dim.dailybudget.myView.PieChartView;
import shm.dim.dailybudget.R;

public class CostsDataAdapter extends RecyclerView.Adapter<CostsDataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Costs> costs;
    private Context context;

    public CostsDataAdapter(Context context, List<Costs> costs) {
        this.context = context;
        this.costs = costs;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.main_list_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Costs cost = costs.get(position);

        holder.colorView.setBackgroundColor(PieChartView.getColor(context, cost.getColor()));
        holder.categoryView.setText(cost.getCategoryName());
        holder.sumCostView.setText(cost.getSumCosts());

        holder.bind(costs.get(position));
    }

    @Override
    public int getItemCount() {
        return costs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final CardView mCardView;
        final TextView colorView, categoryView, sumCostView;


        ViewHolder(View view) {
            super(view);
            colorView =  view.findViewById(R.id.color_rect);
            categoryView =  view.findViewById(R.id.category_name);
            sumCostView =  view.findViewById(R.id.sum_cost);
            mCardView = view.findViewById(R.id.cardView);
        }

        public void bind(final Costs item) {
        }
    }
}