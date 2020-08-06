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
import com.cs5520.numad20su_congressmobile.content.models.Member;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Member}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MembersRecyclerViewAdapter extends RecyclerView.Adapter<MembersRecyclerViewAdapter.ViewHolder> {

    private final List<Member> mValues;
    private int lastPosition = -1;
    private Context context;
    private OnMemberListener mOnMemberListener;

    public MembersRecyclerViewAdapter(List<Member> items, OnMemberListener onMemberListener) {
        mValues = items;
        this.mOnMemberListener = onMemberListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(this.context)
                .inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view, mOnMemberListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Member member = mValues.get(position);
        holder.mItem = member;
        holder.mIdView.setText(member.id);
        String text = member.short_title + " " + member.first_name + " " + member.last_name;
        holder.mContentView.setText(text);
        holder.followIcon.setImageResource(R.drawable.heart_open);

        Animation animation = AnimationUtils.loadAnimation(this.context,
                (position > lastPosition) ? R.anim.slide_right_anim : R.anim.load_up_anim);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Member mItem;
        public ImageView followIcon;
        private OnMemberListener onMemberListener;

        public ViewHolder(View view, OnMemberListener onMemberListener) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
            followIcon = view.findViewById(R.id.follow_icon);
            this.onMemberListener = onMemberListener;

            itemView.setOnClickListener(this);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            onMemberListener.onMemberClick(mItem);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull MembersRecyclerViewAdapter.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    /**
     * Interface for clicking on Members
     */
    public interface OnMemberListener {
        void onMemberClick(Member member);
    }
}