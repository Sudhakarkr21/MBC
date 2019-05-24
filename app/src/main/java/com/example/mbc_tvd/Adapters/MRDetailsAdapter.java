package com.example.mbc_tvd.Adapters;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.mbc_tvd.Main_Activity2;
import com.example.mbc_tvd.R;
import com.example.mbc_tvd.Values.TimeStampValues;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Sourav
 */

public class MRDetailsAdapter extends RecyclerView.Adapter<MRDetailsAdapter.DetailsHolder> {
    private ArrayList<TimeStampValues> arrayList;
    private Context context;
    private static int currentPosition = 0;
    int lastPosition = -1;
    public MRDetailsAdapter(Context context,ArrayList<TimeStampValues> arrayList)
    {
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public MRDetailsAdapter.DetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mrrecycleview, null);
        return new DetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(MRDetailsAdapter.DetailsHolder holder, int position) {
        TimeStampValues getsetvalues = arrayList.get(position);
        holder.mrcode.setText(getsetvalues.getMRCode());

        if(position >lastPosition) {

            Animation animation = AnimationUtils.loadAnimation(context,
                    R.anim.slide_down);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }
//        holder.download.setText(getsetvalues.getDownload_Record());
//        if (!StringUtils.equalsIgnoreCase(getsetvalues.getDownload_Record(),"0"))
//            holder.status.setText(getsetvalues.getStatus());
//        holder.downloadtime.setText(getsetvalues.getDownload_DateTime());
//        holder.billed.setText(getsetvalues.getBilled_Record());
//        holder.billedtime.setText(getsetvalues.getBilled_DateTime());
//        holder.unbilled.setText(getsetvalues.getUnBilled_Record());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DetailsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mrcode,status,download,downloadtime,billed,billedtime,unbilled;
        ImageView call;
        CardView cardView;
        public DetailsHolder(View itemView) {
            super(itemView);
            mrcode =  itemView.findViewById(R.id.txt_mrcode);
            cardView = itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
//            download =  itemView.findViewById(R.id.txt_downloaded_record);
//            status =  itemView.findViewById(R.id.txt_status);
//            downloadtime =  itemView.findViewById(R.id.txt_download_time);
//            billed =  itemView.findViewById(R.id.txt_billed_record);
//            billedtime =  itemView.findViewById(R.id.txt_billed_time);
//            unbilled =  itemView.findViewById(R.id.txt_unbilled_record);
//            call =  itemView.findViewById(R.id.img_calling);
//            call.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            TimeStampValues getsetvalues = arrayList.get(pos);
            switch (v.getId())
            {
                case R.id.card_view:
                    ((Main_Activity2) Objects.requireNonNull(context)).mr_details_display(Main_Activity2.Steps.FORM6,arrayList,pos);
                    break;
//                case R.id.img_calling:
//                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "9594563456", null));
//                    context.startActivity(intent);
//                    break;
            }
        }
    }

}
