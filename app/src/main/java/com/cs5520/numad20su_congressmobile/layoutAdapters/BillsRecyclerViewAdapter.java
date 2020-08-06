package com.cs5520.numad20su_congressmobile.layoutAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs5520.numad20su_congressmobile.R;
import com.cs5520.numad20su_congressmobile.content.models.Bill;

import java.util.List;

public class BillsRecyclerViewAdapter extends RecyclerView.Adapter<BillsRecyclerViewAdapter.ViewHolder> {

    private List<Bill> items;
    private int lastPosition = -1;
    private Context context;
    private OnBillListener mOnBillListener;

    public BillsRecyclerViewAdapter(List<Bill> items, OnBillListener onBillListener) {
        this.items = items;
        this.mOnBillListener = onBillListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(this.context)
                .inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view, mOnBillListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bill = items.get(position);
        holder.idView.setText(items.get(position).bill_id);
        holder.contentView.setText(items.get(position).title);
        holder.followIcon.setImageResource(R.drawable.heart_open);

        Animation animation = AnimationUtils.loadAnimation(this.context,
                (position > lastPosition) ? R.anim.slide_right_anim : R.anim.load_up_anim);
        holder.itemView.startAnimation(animation);
        lastPosition = position;

        if (lastPosition == items.size()) {

        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View view;
        public final TextView idView;
        public final TextView contentView;
        public Bill bill;
        public ImageView followIcon;
        OnBillListener onBillListener;

        public ViewHolder(View view, OnBillListener onBillListener) {
            super(view);
            this.view = view;
            idView = view.findViewById(R.id.item_number);
            contentView = view.findViewById(R.id.content);
            followIcon = view.findViewById(R.id.follow_icon);
            this.onBillListener = onBillListener;

            itemView.setOnClickListener(this);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + contentView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            onBillListener.onBillClick(bill);
        }
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    /**
     * Interface for clicking on Bills
     */
    public interface OnBillListener {
        void onBillClick(Bill bill);
    }
}