package com.highstreet.wallet.base;

import java.util.ArrayList;

import static com.highstreet.wallet.base.BaseConstant.IS_TEST;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_HTLC_BINANCE_BNB;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_HTLC_BINANCE_TEST_BNB;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_HTLC_BINANCE_TEST_BTC;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_HTLC_KAVA_BNB;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_HTLC_KAVA_TEST_BNB;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_HTLC_KAVA_TEST_BTC;

public enum BaseChain {
    COSMOS_LEGACY1("cosmoshub-1"),
    COSMOS_LEGACY2("cosmoshub-2"),
    COSMOS_LEGACY3("cosmoshub-3"),
    COSMOS_MAIN("cosmoshub-3"),
    IRIS_MAIN("irishub"),
    IOV_MAIN("iov-mainnet-2"),
    BNB_MAIN("Binance-Chain-Tigris"),
    KAVA_LEGACY1("kava-1"),
    KAVA_LEGACY2("kava-2"),
    KAVA_MAIN("kava-3"),
    BAND_MAIN("band-wenchang-mainnet"),

    BNB_TEST("Binance-Chain-Nile"),
    KAVA_TEST_LEGACY4("kava-testnet-4000"),
    KAVA_TEST_LEGACY5("kava-testnet-5000"),
    KAVA_TEST_LEGACY6("kava-testnet-6000"),
    KAVA_TEST_LEGACY8("kava-testnet-8000"),
    KAVA_TEST_3("kava-3-test"),
    KAVA_TEST("kava-testnet-9000"),
    IOV_TEST("iovns-galaxynet"),
    OK_TEST_LEGACY("okchain"),
    OK_TEST_LEGACY1("okchain-testnet1"),
    OK_TEST("okexchain-testnet1"),
    CERTIK_TEST("shentu-incentivized-3"),
    DIP_TEST("dip-testnet"),
    DIP_MAIN("dipperhub");

    private final String chainName;

    BaseChain(final String chainname) {
        this.chainName = chainname;
    }

    public String getChain() {
        return chainName;
    }

    public static BaseChain getChain(String chainName) {
        if (chainName.equals(COSMOS_LEGACY1.chainName) ||
                chainName.equals(COSMOS_LEGACY2.chainName) ||
                chainName.equals(COSMOS_LEGACY3.chainName) ||
                chainName.equals(COSMOS_MAIN.chainName)) {
            return COSMOS_MAIN;
        }
        if (chainName.equals(IRIS_MAIN.chainName)) {
            return IRIS_MAIN;
        }
        if (chainName.equals(BNB_MAIN.chainName)) {
            return BNB_MAIN;
        }
        if (chainName.equals(IOV_MAIN.chainName)) {
            return IOV_MAIN;
        }
        if (chainName.equals(KAVA_LEGACY1.chainName) ||
                chainName.equals(KAVA_LEGACY2.chainName) ||
                chainName.equals(KAVA_MAIN.chainName)) {
            return KAVA_MAIN;
        }
        if (chainName.equals(BAND_MAIN.chainName)) {
            return BAND_MAIN;
        }

        if (chainName.equals(BNB_TEST.chainName)) {
            return BNB_TEST;
        }
        if (chainName.equals(KAVA_TEST_LEGACY4.chainName) ||
                chainName.equals(KAVA_TEST_LEGACY5.chainName) ||
                chainName.equals(KAVA_TEST_LEGACY6.chainName) ||
                chainName.equals(KAVA_TEST_LEGACY8.chainName) ||
                chainName.equals(KAVA_TEST_3.chainName) ||
                chainName.equals(KAVA_TEST.chainName)) {
            return KAVA_TEST;
        }
        if (chainName.equals(IOV_TEST.chainName)) {
            return IOV_TEST;
        }
        if (chainName.equals(OK_TEST.chainName)) {
            return OK_TEST;
        }
        if (chainName.equals(CERTIK_TEST.chainName)) {
            return CERTIK_TEST;
        }
        if (chainName.equals(DIP_TEST.chainName)) {
            return DIP_TEST;
        }
        if (chainName.equals(DIP_MAIN.chainName)) {
            return DIP_MAIN;
        }
        return null;
    }

    public static String getDpChain(String chain) {
        if (chain.equals(COSMOS_MAIN.chainName) ||
                chain.equals(COSMOS_LEGACY1.chainName) ||
                chain.equals(COSMOS_LEGACY2.chainName) ||
                chain.equals(COSMOS_LEGACY3.chainName)) {
            if (IS_TEST) {
                return "gaia-13006";
            } else {
                return COSMOS_MAIN.getChain();
            }

        }
        if (chain.equals(IRIS_MAIN.chainName)) {
            return IRIS_MAIN.chainName;
        }
        if (chain.equals(BNB_MAIN.chainName)) {
            return BNB_MAIN.chainName;
        }
        if (chain.equals(IOV_MAIN.chainName)) {
            return IOV_MAIN.chainName;
        }
        if (chain.equals(KAVA_LEGACY1.chainName) ||
                chain.equals(KAVA_LEGACY2.chainName) ||
                chain.equals(KAVA_MAIN.chainName)) {
            return KAVA_MAIN.chainName;
        }
        if (chain.equals(BAND_MAIN.chainName)) {
            return BAND_MAIN.chainName;
        }

        if (chain.equals(KAVA_TEST_LEGACY4.chainName) ||
                chain.equals(KAVA_TEST_LEGACY5.chainName) ||
                chain.equals(KAVA_TEST_LEGACY6.chainName) ||
                chain.equals(KAVA_TEST_LEGACY8.chainName) ||
                chain.equals(KAVA_TEST_3.chainName) ||
                chain.equals(KAVA_TEST.chainName)) {
            return KAVA_TEST.chainName;
        }
        if (chain.equals(BNB_TEST.chainName)) {
            return BNB_TEST.chainName;
        }
        if (chain.equals(IOV_TEST.chainName)) {
            return IOV_TEST.chainName;
        }
        if (chain.equals(OK_TEST.chainName)) {
            return OK_TEST.chainName;
        }
        if (chain.equals(CERTIK_TEST.chainName)) {
            return CERTIK_TEST.chainName;
        }
        if (chain.equals(DIP_TEST.chainName)) {
            return DIP_TEST.chainName;
        }
        if (chain.equals(DIP_MAIN.chainName)) {
            return DIP_MAIN.chainName;
        }
        return null;
    }

    public static ArrayList<BaseChain> SUPPORT_CHAINS() {
        ArrayList<BaseChain> result = new ArrayList<>();
        result.add(COSMOS_MAIN);
        result.add(IRIS_MAIN);
        result.add(BNB_MAIN);
        result.add(IOV_MAIN);
        result.add(KAVA_MAIN);
        result.add(BAND_MAIN);

        result.add(BNB_TEST);
        result.add(KAVA_TEST);
        result.add(IOV_TEST);
        result.add(OK_TEST);
        result.add(CERTIK_TEST);
        result.add(DIP_TEST);
        result.add(DIP_MAIN);
        return result;
    }

    public static boolean IS_SUPPORT_CHAIN(String chain) {
        return SUPPORT_CHAINS().contains(getChain(chain));
    }

    public static ArrayList<BaseChain> getHtlcSendable(BaseChain fromChain) {
        ArrayList<BaseChain> result = new ArrayList<>();
        if (fromChain.equals(BNB_TEST)) {
            result.add(KAVA_TEST);

        } else if (fromChain.equals(KAVA_TEST)) {
            result.add(BNB_TEST);

        } else if (fromChain.equals(KAVA_MAIN)) {
            result.add(BNB_MAIN);

        } else if (fromChain.equals(BNB_MAIN)) {
            result.add(KAVA_MAIN);

        }
        return result;
    }

    public static ArrayList<String> getHtlcSwappableCoin(BaseChain fromChain) {
        ArrayList<String> result = new ArrayList<>();
        if (fromChain.equals(BNB_MAIN)) {
            result.add(TOKEN_HTLC_BINANCE_BNB);

        } else if (fromChain.equals(KAVA_MAIN)) {
            result.add(TOKEN_HTLC_KAVA_BNB);

        } else if (fromChain.equals(BNB_TEST)) {
            result.add(TOKEN_HTLC_BINANCE_TEST_BNB);
            result.add(TOKEN_HTLC_BINANCE_TEST_BTC);

        } else if (fromChain.equals(KAVA_TEST)) {
            result.add(TOKEN_HTLC_KAVA_TEST_BNB);
            result.add(TOKEN_HTLC_KAVA_TEST_BTC);
        }
        return result;
    }
}
