package shm.dim.dailybudget.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import shm.dim.dailybudget.model.Category;
import shm.dim.dailybudget.R;

public class CategoryDataAdapter extends RecyclerView.Adapter<CategoryDataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Category> categories;
    private Context context;
    private static int position;


    public CategoryDataAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public CategoryDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_list_item, parent, false);
        return new CategoryDataAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CategoryDataAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.categoryNameView.setText(category.getName());

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(holder.getAdapterPosition(), 0,  0, "Удалить");
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });

        holder.bind(categories.get(position));
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static int getPosition() {
        return position;
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        final CardView mCardView;
        final TextView categoryNameView;


        ViewHolder(View view) {
            super(view);
            categoryNameView =  view.findViewById(R.id.category_name);
            mCardView = view.findViewById(R.id.cardView);
        }

        public void bind(final Category item) {

        }
    }
}