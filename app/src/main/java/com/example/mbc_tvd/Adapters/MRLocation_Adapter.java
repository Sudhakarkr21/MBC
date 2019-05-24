package com.example.mbc_tvd.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mbc_tvd.R;
import com.example.mbc_tvd.Values.GetSetValues;

import java.util.ArrayList;

public class MRLocation_Adapter extends RecyclerView.Adapter<MRLocation_Adapter.TicketHolder> implements Filterable {

    String latitude = "", longitude = "", MRname = "", MRcode = "";
    private static int currentPosition = 0;
    private ArrayList<GetSetValues> arrayList;
    private ArrayList<GetSetValues> filteredList;
    private Context context;
    private GetSetValues getSetValues;

    public MRLocation_Adapter(Context context, ArrayList<GetSetValues> arrayList, GetSetValues getSetValues) {
        this.arrayList = arrayList;
        this.context = context;
        this.filteredList = arrayList;
        this.getSetValues = getSetValues;
    }



    @NonNull
    @Override
    public TicketHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mrlocation_adapter, null);
        return new TicketHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketHolder holder, int position) {
        GetSetValues getsetvalues = arrayList.get(position);
        if(!getsetvalues.getMRCODE().equals(""))
        holder.mrcode.setText(getsetvalues.getMRCODE());
        else holder.mrcode.setText("NA");

        if (!getsetvalues.getMRNAME().equals(""))
        holder.mrname.setText(getsetvalues.getMRNAME());
        else holder.mrname.setText("NA");

        if(!getsetvalues.getMOBILE_NO().equals(""))
        holder.mrphone.setText(getsetvalues.getMOBILE_NO());
        else holder.mrphone.setText("NA");

        if (!getsetvalues.getDEVICE_ID().equals(""))
        holder.mriemi.setText(getsetvalues.getDEVICE_ID());
        else holder.mriemi.setText("NA");

        holder.show_hide.setVisibility(View.GONE);

        if (currentPosition == position) {
            Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
            holder.show_hide.setVisibility(View.VISIBLE);
            holder.show_hide.startAnimation(slideDown);
            holder.expand.setVisibility(View.GONE);
        } else {
            holder.expand.setVisibility(View.VISIBLE);
        }

        holder.expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPosition = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String search = constraint.toString();
                if(search.isEmpty())
                    arrayList = filteredList;
                else {
                    ArrayList<GetSetValues> filterlist = new ArrayList<>();
                    for(int i=0;i<filteredList.size();i++){
                        GetSetValues getSetValues = filteredList.get(i);
                        if(getSetValues.getMRCODE().contains(search)){
                            filterlist.add(getSetValues);
                        }
                    }

                    arrayList = filterlist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = arrayList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayList = (ArrayList<GetSetValues>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class TicketHolder extends RecyclerView.ViewHolder{
        TextView mrcode, mrname, mrphone, mriemi;
        // ImageView phone, location;
        ImageView expand;
        Button call, location;
        LinearLayout show_hide;


        public TicketHolder(@NonNull View itemView) {
        super(itemView);

            mrcode =  itemView.findViewById(R.id.txt_mr_code);
            mrname =  itemView.findViewById(R.id.txt_mr_name);
            mrphone =  itemView.findViewById(R.id.txt_phone);
            mriemi =  itemView.findViewById(R.id.txt_iemi);
            show_hide =  itemView.findViewById(R.id.lin_hide);
            expand =  itemView.findViewById(R.id.img_expand);
    }
}
}
