package biz.mybaidumap.mybaidumap.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import biz.mybaidumap.mybaidumap.R;
import biz.mybaidumap.mybaidumap.model.AddressModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ice Wu on 2017/9/16.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AdrViewHolder> {

    private List<AddressModel> modelList;
    private LayoutInflater inflater;

    private OnItemClickListener onItemClickListener;

    public AddressAdapter(Context context, List<AddressModel> modelList) {
        this.modelList = modelList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public AdrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_address_layout, parent, false);
        return new AdrViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AdrViewHolder holder, final int position) {
        String discription = modelList.get(position).getDescription();
        String streetName = modelList.get(position).getStreetName();
        String streetNumber = modelList.get(position).getStreetNumber();
        holder.addressContent.setText(discription + streetName + streetNumber);
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public interface OnItemClickListener {
        void OnClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    class AdrViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.address_content)
        TextView addressContent;

        public AdrViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
