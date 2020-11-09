package com.highstreet.wallet.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.highstreet.wallet.BuildConfig;
import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.AccountListActivity;
import com.highstreet.wallet.activities.AppLockSetActivity;
import com.highstreet.wallet.activities.MainActivity;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseFragment;
import com.highstreet.wallet.dialog.Dialog_Currency_Set;

import java.util.Locale;

import static com.highstreet.wallet.base.BaseChain.BAND_MAIN;
import static com.highstreet.wallet.base.BaseChain.BNB_MAIN;
import static com.highstreet.wallet.base.BaseChain.BNB_TEST;
import static com.highstreet.wallet.base.BaseChain.CERTIK_TEST;
import static com.highstreet.wallet.base.BaseChain.COSMOS_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_MAIN;
import static com.highstreet.wallet.base.BaseChain.IRIS_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_TEST;
import static com.highstreet.wallet.base.BaseChain.OK_TEST;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_BAND_MAIN;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_BINANCE_MAIN;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_BINANCE_TEST;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_CERTIK_TEST;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_COSMOS_MAIN;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_IOV_MAIN;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_IRIS_MAIN;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_KAVA_MAIN;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_KAVA_TEST;
import static com.highstreet.wallet.base.BaseConstant.EXPLORER_OKEX_TEST;

public class MainSettingFragment extends BaseFragment implements View.OnClickListener {

    public final static int SELECT_CURRENCY = 9034;
    public final static int SELECT_MARKET = 9035;

    private FrameLayout mBtnWallet, mBtnAlaram, mBtnAppLock, mBtnCurrency, mBtnBasePrice,
                        mBtnGuide, mBtnTelegram, mBtnExplore, mBtnHomepage,
                        mBtnTerm, mBtnGithub, mBtnVersion;

    private TextView    mTvAppLock, mTvCurrency, mTvBasePrice, mTvVersion;
    private TextView    mTvTitleExplore;

    public static MainSettingFragment newInstance(Bundle bundle) {
        MainSettingFragment fragment = new MainSettingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (getMainActivity().mBaseChain.equals(COSMOS_MAIN)) {
            if (getMainActivity().mAccount.pushAlarm) {
                getMainActivity().getMenuInflater().inflate(R.menu.main_menu_alaram_on, menu);
            } else {
                getMainActivity().getMenuInflater().inflate(R.menu.main_menu_alaram_off, menu);
            }
        } else {
            getMainActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_accounts :
                getMainActivity().onShowTopAccountsView();
                break;
            case R.id.menu_notification_off:
                getMainActivity().onUpdateUserAlarm(getMainActivity().mAccount, true);
                break;
            case R.id.menu_notification_on:
                getMainActivity().onUpdateUserAlarm(getMainActivity().mAccount, false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_setting, container, false);
        mBtnWallet = rootView.findViewById(R.id.card_wallet);
        mBtnAlaram = rootView.findViewById(R.id.card_alaram);
        mBtnAppLock = rootView.findViewById(R.id.card_applock);
        mBtnCurrency = rootView.findViewById(R.id.card_currency);
        mBtnBasePrice = rootView.findViewById(R.id.card_base_price);
        mBtnGuide = rootView.findViewById(R.id.card_guide);
        mBtnTelegram = rootView.findViewById(R.id.card_telegram);
        mBtnExplore = rootView.findViewById(R.id.card_explore);
        mBtnHomepage = rootView.findViewById(R.id.card_homepage);
        mBtnTerm = rootView.findViewById(R.id.card_term);
        mBtnGithub = rootView.findViewById(R.id.card_github);
        mBtnVersion = rootView.findViewById(R.id.card_version);
        mTvAppLock = rootView.findViewById(R.id.applock_text);
        mTvCurrency = rootView.findViewById(R.id.currency_text);
        mTvBasePrice = rootView.findViewById(R.id.base_price_text);
        mTvVersion = rootView.findViewById(R.id.version_text);
        mTvTitleExplore = rootView.findViewById(R.id.title_explore);
        mBtnWallet.setOnClickListener(this);
        mBtnAlaram.setOnClickListener(this);
        mBtnAppLock.setOnClickListener(this);
        mBtnCurrency.setOnClickListener(this);
        mBtnBasePrice.setOnClickListener(this);
        mBtnGuide.setOnClickListener(this);
        mBtnTelegram.setOnClickListener(this);
        mBtnExplore.setOnClickListener(this);
        mBtnHomepage.setOnClickListener(this);
        mBtnTerm.setOnClickListener(this);
        mBtnGithub.setOnClickListener(this);
        mBtnVersion.setOnClickListener(this);

        mTvVersion.setText("v" + BuildConfig.VERSION_NAME);

        mBtnAlaram.setVisibility(View.GONE);
        return rootView;
    }


    @Override
    public void onRefreshTab() {
        if(!isAdded()) return;
        mTvCurrency.setText(getBaseDao().getCurrencyString());
        mTvBasePrice.setText(getBaseDao().getMarketString(getMainActivity()));
        if(getBaseDao().getUsingAppLock()) {
            mTvAppLock.setText(R.string.str_app_applock_enabled);
        } else {
            mTvAppLock.setText(R.string.str_app_applock_diabeld);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBtnWallet)) {
            startActivity(new Intent(getBaseActivity(), AccountListActivity.class));

        } else if (v.equals(mBtnAlaram)) {
            Toast.makeText(getBaseActivity(), R.string.str_preparing, Toast.LENGTH_SHORT).show();

        } else if (v.equals(mBtnAppLock)) {
            startActivity(new Intent(getBaseActivity(), AppLockSetActivity.class));

        } else if (v.equals(mBtnCurrency)) {
            Dialog_Currency_Set currency_dialog = Dialog_Currency_Set.newInstance(null);
            currency_dialog.setCancelable(true);
            currency_dialog.setTargetFragment(this, SELECT_CURRENCY);
            getFragmentManager().beginTransaction().add(currency_dialog, "dialog").commitNowAllowingStateLoss();
            return;

        } else if (v.equals(mBtnBasePrice)) {
            //NO more coinmarketcap always using coingecko
//            Dialog_Market market = Dialog_Market.newInstance(null);
//            market.setCancelable(true);
//            market.setTargetFragment(this, SELECT_MARKET);
//            getFragmentManager().beginTransaction().add(market, "dialog").commitNowAllowingStateLoss();
//            return;
            return;

        } else if (v.equals(mBtnGuide)) {
            if(Locale.getDefault().getLanguage().toLowerCase().equals("ko")) {
                Intent guideIntent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://guide.cosmostation.io/app_wallet_ko.html"));
                startActivity(guideIntent);
            } else {
                Intent guideIntent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://guide.cosmostation.io/app_wallet_en.html"));
                startActivity(guideIntent);
            }

        } else if (v.equals(mBtnTelegram)) {
            Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://t.me/cosmostation"));
            startActivity(telegram);

        } else if (v.equals(mBtnExplore)) {
            if (getMainActivity().mBaseChain.equals(COSMOS_MAIN)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_COSMOS_MAIN));
                startActivity(intent);
            } else if (getMainActivity().mBaseChain.equals(IRIS_MAIN)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_IRIS_MAIN));
                startActivity(intent);
            } else if (getMainActivity().mBaseChain.equals(BNB_MAIN)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_BINANCE_MAIN));
                startActivity(intent);
            } else if (getMainActivity().mBaseChain.equals(KAVA_MAIN)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_KAVA_MAIN));
                startActivity(intent);
            } else if (getMainActivity().mBaseChain.equals(IOV_MAIN)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_IOV_MAIN));
                startActivity(intent);
            } else if (getMainActivity().mBaseChain.equals(BAND_MAIN)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_BAND_MAIN));
                startActivity(intent);
            } else if (getMainActivity().mBaseChain.equals(BNB_TEST)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_BINANCE_TEST));
                startActivity(intent);
            } else if (getMainActivity().mBaseChain.equals(OK_TEST)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_OKEX_TEST));
                startActivity(intent);
            } else if (getMainActivity().mBaseChain.equals(KAVA_TEST)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_KAVA_TEST));
                startActivity(intent);
            } else if (getMainActivity().mBaseChain.equals(CERTIK_TEST)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(EXPLORER_CERTIK_TEST));
                startActivity(intent);
            }

        } else if (v.equals(mBtnHomepage)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cosmostation.io/"));
            startActivity(intent);

        } else if (v.equals(mBtnTerm)) {
            if(Locale.getDefault().getLanguage().toLowerCase().equals("ko")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cosmostation.io/service_ko.html"));
                startActivity(intent);
            }  else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.cosmostation.io/service_en.html"));
                startActivity(intent);
            }

        } else if (v.equals(mBtnGithub)) {
            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("https://github.com/cosmostation/cosmostation-mobile"));
            startActivity(intent);

        } else if (v.equals(mBtnVersion)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + getMainActivity().getPackageName()));
            startActivity(intent);
        }

    }

    public MainActivity getMainActivity() {
        return (MainActivity)getBaseActivity();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SELECT_CURRENCY && resultCode == Activity.RESULT_OK) {
            getBaseDao().setCurrency(data.getIntExtra("currency", 0));
            mTvCurrency.setText(getBaseDao().getCurrencyString());
        } else if (requestCode == SELECT_MARKET && resultCode == Activity.RESULT_OK) {
            getBaseDao().setMarket(data.getIntExtra("market", 0));
            mTvBasePrice.setText(getBaseDao().getMarketString(getMainActivity()));
        }
        getMainActivity().onPriceTic(BaseChain.getChain(getMainActivity().mAccount.baseChain));
    }
}
