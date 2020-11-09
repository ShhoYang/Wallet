package com.highstreet.wallet.dao;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;

import com.highstreet.lib.adapter.BaseItem;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.utils.WDp;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static com.highstreet.wallet.base.BaseConstant.TOKEN_IOV;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_IOV_TEST;

public class Account implements BaseItem {
    public Long id;
    public String uuid;
    public String nickName;
    public Boolean isFavo;
    public String address;
    public String baseChain;

    public Boolean hasPrivateKey;
    public String resource;
    public String spec;
    public Boolean fromMnemonic;
    public String path;

    public Boolean isValidator;
    public Integer sequenceNumber;
    public Integer accountNumber;
    public Long fetchTime;
    public int msize;
    public Long importTime;

    public String lastTotal;
    public Long sortOrder;

    /**
     * 用这个字段标识是否备份
     */
    public Boolean pushAlarm;
    public Boolean newBip44;

    public ArrayList<Balance> balances;


    public static Account getNewInstance() {
        Account result = new Account();
        result.uuid = UUID.randomUUID().toString();
        return result;
    }

    public Account() {
    }

    public Account(Long id, String uuid, String nickName, boolean isFavo, String address,
                   String baseChain, boolean hasPrivateKey, String resource, String spec,
                   boolean fromMnemonic, String path, boolean isValidator, int sequenceNumber,
                   int accountNumber, Long fetchTime, int msize, long importTime, String lastTotal,
                   long sortOrder, boolean pushAlarm, boolean newBip) {
        this.id = id;
        this.uuid = uuid;
        this.nickName = nickName;
        this.isFavo = isFavo;
        this.address = address;
        this.baseChain = baseChain;
        this.hasPrivateKey = hasPrivateKey;
        this.resource = resource;
        this.spec = spec;
        this.fromMnemonic = fromMnemonic;
        this.path = path;
        this.isValidator = isValidator;
        this.sequenceNumber = sequenceNumber;
        this.accountNumber = accountNumber;
        this.fetchTime = fetchTime;
        this.msize = msize;
        this.importTime = importTime;
        this.lastTotal = lastTotal;
        this.sortOrder = sortOrder;
        this.pushAlarm = pushAlarm;
        this.newBip44 = newBip;
    }

    public ArrayList<Balance> getBalances() {
        return balances;
    }

    public void setBalances(ArrayList<Balance> balances) {
        this.balances = balances;
    }


    public BigDecimal getAtomBalance() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BaseConstant.TOKEN_ATOM) || balance.symbol.equals(BaseConstant.TOKEN_MUON)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getPhotonBalance() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BaseConstant.COSMOS_PHOTON) || balance.symbol.equals(BaseConstant.COSMOS_PHOTINO)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getIrisBalance() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BaseConstant.TOKEN_IRIS_ATTO)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getBnbBalance() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BaseConstant.TOKEN_BNB)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getBnbBalanceScale() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BaseConstant.TOKEN_BNB)) {
                result = balance.balance.movePointRight(8);
                break;
            }
        }
        return result;
    }

    public BigDecimal getBnbTokenBalance(String symbol) {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(symbol)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getIrisTokenBalance(String symbol) {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.split("-")[0].equals(symbol)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getKavaBalance() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BaseConstant.TOKEN_KAVA)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getKavaDelegable() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BaseConstant.TOKEN_KAVA)) {
                result = balance.balance.add(balance.locked);
                break;
            }
        }
        return result;
    }

    public BigDecimal getTokenBalance(String symbol) {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equalsIgnoreCase(symbol)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getIovBalance() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(TOKEN_IOV) || balance.symbol.equals(TOKEN_IOV_TEST)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }

    public BigDecimal getBandBalance() {
        BigDecimal result = BigDecimal.ZERO;
        if (balances == null || balances.size() == 0) {
            return result;
        }
        for (Balance balance : balances) {
            if (balance.symbol.equals(BaseConstant.TOKEN_BAND)) {
                result = balance.balance;
                break;
            }
        }
        return result;
    }


    public SpannableString getLastTotal(Context c, BaseChain chain) {
        if (TextUtils.isEmpty(lastTotal)) {
            return SpannableString.valueOf("--");
        }
        try {
            if (chain.equals(BaseChain.COSMOS_MAIN)) {
                return WDp.getDpAmount2(c, new BigDecimal(lastTotal), 6, 6);
            } else if (chain.equals(BaseChain.IRIS_MAIN)) {
                return WDp.getDpAmount2(c, new BigDecimal(lastTotal), 18, 6);

            } else if (chain.equals(BaseChain.BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
                return WDp.getDpAmount2(c, new BigDecimal(lastTotal), 0, 6);

            } else if (chain.equals(BaseChain.KAVA_MAIN) || chain.equals(BaseChain.KAVA_TEST)) {
                return WDp.getDpAmount2(c, new BigDecimal(lastTotal), 6, 6);

            } else if (chain.equals(BaseChain.IOV_MAIN) || chain.equals(BaseChain.IOV_TEST)) {
                return WDp.getDpAmount2(c, new BigDecimal(lastTotal), 6, 6);

            } else if (chain.equals(BaseChain.BAND_MAIN)) {
                return WDp.getDpAmount2(c, new BigDecimal(lastTotal), 6, 6);

            } else if (chain.equals(BaseChain.OK_TEST)) {
                return WDp.getDpAmount2(c, new BigDecimal(lastTotal), 0, 6);

            } else if (chain.equals(BaseChain.CERTIK_TEST)) {
                return WDp.getDpAmount2(c, new BigDecimal(lastTotal), 6, 6);

            } else {
                return WDp.getDpAmount2(c, BigDecimal.ZERO, 6, 6);
            }

        } catch (Exception e) {
            return SpannableString.valueOf("--");
        }

    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", isFavo=" + isFavo +
                ", address='" + address + '\'' +
                ", baseChain='" + baseChain + '\'' +
                ", hasPrivateKey=" + hasPrivateKey +
                ", resource='" + resource + '\'' +
                ", spec='" + spec + '\'' +
                ", fromMnemonic=" + fromMnemonic +
                ", path='" + path + '\'' +
                ", isValidator=" + isValidator +
                ", sequenceNumber=" + sequenceNumber +
                ", accountNumber=" + accountNumber +
                ", fetchTime=" + fetchTime +
                ", msize=" + msize +
                ", importTime=" + importTime +
                ", lastTotal='" + lastTotal + '\'' +
                ", sortOrder=" + sortOrder +
                ", pushAlarm=" + pushAlarm +
                ", newBip44=" + newBip44 +
                ", balances=" + balances +
                '}';
    }

    @NotNull
    @Override
    public String uniqueKey() {
        return address;
    }

    public Boolean isBackup() {
        return pushAlarm;
    }
}
