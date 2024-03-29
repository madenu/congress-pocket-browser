package com.cs5520.numad20su_congressmobile.layoutAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cs5520.numad20su_congressmobile.R;
import com.cs5520.numad20su_congressmobile.content.models.Member;
import com.cs5520.numad20su_congressmobile.controllers.FollowInterface;
import com.cs5520.numad20su_congressmobile.controllers.FollowInterface.TYPE;
import com.cs5520.numad20su_congressmobile.controllers.MemberDetailsActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Member}. TODO: Replace the implementation
 * with code for your data type.
 */
public class MembersRecyclerViewAdapter
    extends RecyclerView.Adapter<MembersRecyclerViewAdapter.MemberViewHolder>
    implements Filterable {

    private final List<Member> memberList;
    private List<Member> memberListFiltered;
    private List<Member> preFilteredList;

    private int lastPosition = -1;
    private final Context context;
    private final FollowInterface followInterface;

    public MembersRecyclerViewAdapter(Context context, List<Member> items,
        FollowInterface followInterface) {
        this.context = context;
        this.memberList = items;
        this.memberListFiltered = new ArrayList<>(this.memberList);
        this.preFilteredList = new ArrayList<>(this.memberList);
        this.followInterface = followInterface;
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener {

        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView followIcon;
        private final Context context;
        private final FollowInterface followInterface;
        public Member mItem;
        public boolean isFollowing;

        public MemberViewHolder(View view, Context context, FollowInterface followInterface) {
            super(view);

            this.mIdView = view.findViewById(R.id.item_number);
            this.mContentView = view.findViewById(R.id.content);
            this.followIcon = view.findViewById(R.id.follow_icon);
            this.context = context;
            this.followInterface = followInterface;

            this.followIcon.setOnClickListener(this);
            super.itemView.setOnClickListener(this);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.card_view:
                    context.startActivity(
                        new Intent(context, MemberDetailsActivity.class)
                            .putExtra("member", mItem));
                    break;
                case R.id.follow_icon:
                    if (isFollowing) {
                        followInterface.unfollow(TYPE.Member, mItem.id);
                        followIcon.setImageResource(R.drawable.heart_open);
                    } else {
                        followInterface.follow(TYPE.Member, mItem.id);
                        followIcon.setImageResource(R.drawable.heart_closed);
                    }
                    isFollowing = !isFollowing;
                    break;
            }
        }
    }

    public void add(Member member) {
        this.memberList.add(member);
        this.notifyItemInserted(this.memberList.size() - 1);
    }

    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.card_layout, parent, false);
        return new MemberViewHolder(view, this.context, followInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        Member member = memberList.get(position);
        holder.mItem = member;
        holder.mContentView
            .setText(String.format("%s %s %s", member.id, member.state, member.party));
        holder.isFollowing = (this.followInterface.following(TYPE.Member).contains(member.id));
        String text = member.short_title + " " + member.first_name + " " + member.last_name;
        holder.mIdView.setText(text);
        holder.followIcon
            .setImageResource(
                (holder.isFollowing) ? R.drawable.heart_closed : R.drawable.heart_open);

        Animation animation = AnimationUtils.loadAnimation(this.context,
            (position > lastPosition) ? R.anim.slide_right_anim : R.anim.load_up_anim);
        holder.itemView.startAnimation(animation);
        lastPosition = position;
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    @Override
    public void onViewDetachedFromWindow(
        @NonNull MembersRecyclerViewAdapter.MemberViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public Filter getFilter() {
        return memberFilter;
    }

    private final Filter memberFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Member> filteredList = new ArrayList<>();
            String filterPattern = constraint.toString().toLowerCase().trim();

            if (filterPattern.isEmpty()) {
                filteredList.clear();
                filteredList.addAll(preFilteredList);
            } else {
                for (Member member : memberList) {
                    if (isInFilter(member, filterPattern)) {
                        filteredList.add(member);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            memberList.clear();
            memberListFiltered = (ArrayList<Member>) filterResults.values;
            memberList.addAll(memberListFiltered);
            notifyDataSetChanged();
        }
    };

    private boolean isInFilter(Member member, String filterPattern) {

        String fullName = "";

        if (member.first_name != null) {
            fullName = fullName + member.first_name.toLowerCase() + " ";
        }

        if (member.middle_name != null) {
            fullName = fullName + member.middle_name.toLowerCase() + " ";
        }

        if (member.last_name != null) {
            fullName = fullName + member.last_name.toLowerCase();
        }

        return fullName.contains(filterPattern);

    }

    public List<Member> getPreFilteredList() {
        return preFilteredList;
    }

    public void setPreFilteredList(List<Member> preFilteredList) {
        this.preFilteredList = preFilteredList;
    }


}

