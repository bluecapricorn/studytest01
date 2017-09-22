package biz.mybaidumap.mybaidumap.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import biz.mybaidumap.mybaidumap.R;
import biz.mybaidumap.mybaidumap.adapter.AddressAdapter;
import biz.mybaidumap.mybaidumap.model.AddressModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ice Wu on 2017/9/16.
 */

public class MainActivity extends AppCompatActivity {

    /*@BindView(R.id.btn_exit)
    Button btnExit;*/

    @BindView(R.id.address_list)
    RecyclerView addressList;

    private List<AddressModel> modelList;
    private AddressAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        modelList = new ArrayList<AddressModel>();
        initListDatas(modelList);
        adapter = new AddressAdapter(this, modelList);
        addressList.setAdapter(adapter);
        addressList.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                /*进入百度地图定位*/
                Intent intent = LocationActivity.getIntent(MainActivity.this);
                intent.putExtra("city", modelList.get(position).getCity());
                intent.putExtra("district", modelList.get(position).getDescription());
                intent.putExtra("street_name", modelList.get(position).getStreetName());
                intent.putExtra("street_number", modelList.get(position).getStreetNumber());
                startActivity(intent);
            }
        });
    }

    private void initListDatas(List<AddressModel> modelList) {
        modelList.add(new AddressModel("黄浦区", "河南中路", "135号"));
        modelList.add(new AddressModel("静安区", "汉中路", "176号"));
        modelList.add(new AddressModel("静安区", "共和新路", "5000弄"));
        modelList.add(new AddressModel("浦东新区", "博兴路", "863号"));
        modelList.add(new AddressModel("杨浦区", "内江路", "467号"));
    }

    @OnClick(R.id.btn_exit)
    public void OnClick(View view) {
        finish();
    }
}
