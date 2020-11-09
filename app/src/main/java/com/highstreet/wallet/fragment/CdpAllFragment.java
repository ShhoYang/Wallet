package com.highstreet.wallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.KavaCdpDetailActivity;
import com.highstreet.wallet.activities.KavaCdpListActivity;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.network.res.ResCdpParam;
import com.highstreet.wallet.utils.WDp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.highstreet.wallet.base.BaseConstant.KAVA_CDP_MARKET_IMG_URL;

public class CdpAllFragment extends BaseFragment {

    private SwipeRefreshLayout  mSwipeRefreshLayout;
    private RecyclerView        mRecyclerView;

    private ArrayList<ResCdpParam.KavaCollateralParam>  mKavaCollateralParam = new ArrayList<>();
    private AllCdpAdapter                               mAllCdpAdapter;

    public static CdpAllFragment newInstance(Bundle bundle) {
        CdpAllFragment fragment = new CdpAllFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_all_cdp, container, false);
        mSwipeRefreshLayout     = rootView.findViewById(R.id.layer_refresher);
        mRecyclerView           = rootView.findViewById(R.id.recycler);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMainActivity().onFetchCdpInfo();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);
        mAllCdpAdapter = new AllCdpAdapter();
        mRecyclerView.setAdapter(mAllCdpAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefreshTab();
    }

    @Override
    public void onRefreshTab() {
        if(!isAdded()) return;
        mKavaCollateralParam = getMainActivity().mCdpParam.collateral_params;
        mAllCdpAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void onBusyFetch() { }

    public KavaCdpListActivity getMainActivity() {
        return (KavaCdpListActivity)getBaseActivity();
    }

    private class AllCdpAdapter extends RecyclerView.Adapter<AllCdpAdapter.AllCdpHolder> {

        @NonNull
        @Override
        public AllCdpHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new AllCdpHolder(getLayoutInflater().inflate(R.layout.item_cdp_list_all, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AllCdpHolder holder, int position) {
            final ResCdpParam.KavaCollateralParam collateralParam = mKavaCollateralParam.get(position);

            holder.itemTitleMarket.setText(collateralParam.getDpMarketId());
            holder.itemCollateralRate.setText(WDp.getPercentDp(collateralParam.getDpLiquidationRatio(), 2));
            holder.itemStabilityFee.setText(WDp.getPercentDp(collateralParam.getDpStabilityFee(), 2));
            holder.itemLiquidationPenalty.setText(WDp.getPercentDp(collateralParam.getDpLiquidationPenalty(), 2));

            Picasso.get().cancelRequest(holder.itemImgMarket);
            try {
                Picasso.get().load(KAVA_CDP_MARKET_IMG_URL+  collateralParam.getImagePath())
                        .fit().into(holder.itemImgMarket);

            } catch (Exception e) { }
            holder.itemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getMainActivity(), KavaCdpDetailActivity.class);
                    intent.putExtra("denom", collateralParam.denom);
                    intent.putExtra("marketId", collateralParam.liquidation_market_id);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mKavaCollateralParam.size();
        }

        public class AllCdpHolder extends RecyclerView.ViewHolder {
            CardView itemRoot;
            ImageView itemImgMarket;
            TextView itemTitleMarket;
            TextView itemCollateralRate, itemStabilityFee, itemLiquidationPenalty;


            public AllCdpHolder(@NonNull View itemView) {
                super(itemView);
                itemRoot = itemView.findViewById(R.id.card_cdp_all);
                itemImgMarket = itemView.findViewById(R.id.cdp_market_img);
                itemTitleMarket = itemView.findViewById(R.id.cdp_market_title);
                itemCollateralRate = itemView.findViewById(R.id.cdp_collateral_rate);
                itemStabilityFee = itemView.findViewById(R.id.cdp_stability_fee);
                itemLiquidationPenalty = itemView.findViewById(R.id.cdp_str_liquidation_penalty);
            }
        }
    }
}
