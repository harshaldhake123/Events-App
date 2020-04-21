package com.example.clubapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventAdapter1 extends RecyclerView.Adapter<EventAdapter1.MyViewHolder> {

    Context context;
    ArrayList<Event> eventList;
    String flag;
    Picasso picasso;

    public EventAdapter1(Context context, ArrayList<Event> e, String flag) {
        this.context = context;
        eventList = e;
        this.flag = flag;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_event_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvEventVenue.setText("⚫ Venue : " + eventList.get(position).getVenue());
        holder.tvEventDate.setText("⚫ Date : " + eventList.get(position).getDate());
        holder.tvEventName.setText("⚫ Event Name : " + eventList.get(position).getEventName());

        final String EventName = eventList.get(position).getEventName();

        if (!eventList.get(position).getImage().equals("NULL")) {
            picasso.with(context).load(eventList.get(position).getImage()).into(holder.ivEvent);
        } else {
            holder.ivEvent.setImageResource(R.drawable.no);
        }


        holder.btnEventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetailsActivity.class);
                intent.putExtra("EventName", EventName);
                intent.putExtra("flag", flag);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivEvent;
        TextView tvEventDate, tvEventVenue, tvEventName;
        Button btnEventDetails;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvEventVenue = itemView.findViewById(R.id.tvEventVenue);
            ivEvent = itemView.findViewById(R.id.ivEvent);
            btnEventDetails = itemView.findViewById(R.id.btnEventDetails);
            tvEventName = itemView.findViewById(R.id.tvEventName);
        }
    }

}