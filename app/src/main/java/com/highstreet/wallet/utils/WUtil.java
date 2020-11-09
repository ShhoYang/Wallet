package com.highstreet.wallet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.common.BitMatrix;
import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.base.BaseData;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dao.Balance;
import com.highstreet.wallet.dao.BnbToken;
import com.highstreet.wallet.dao.BondingState;
import com.highstreet.wallet.dao.IovToken;
import com.highstreet.wallet.dao.IrisToken;
import com.highstreet.wallet.dao.OkToken;
import com.highstreet.wallet.dao.Reward;
import com.highstreet.wallet.dao.UnBondingState;
import com.highstreet.wallet.model.StarNameResource;
import com.highstreet.wallet.model.type.Coin;
import com.highstreet.wallet.model.type.IrisProposal;
import com.highstreet.wallet.model.type.Proposal;
import com.highstreet.wallet.model.type.Validator;
import com.highstreet.wallet.model.type.Vote;
import com.highstreet.wallet.network.res.ResBnbAccountInfo;
import com.highstreet.wallet.network.res.ResBnbTic;
import com.highstreet.wallet.network.res.ResCdpParam;
import com.highstreet.wallet.network.res.ResKavaMarketPrice;
import com.highstreet.wallet.network.res.ResLcdAccountInfo;
import com.highstreet.wallet.network.res.ResLcdBonding;
import com.highstreet.wallet.network.res.ResLcdIrisReward;
import com.highstreet.wallet.network.res.ResLcdKavaAccountInfo;
import com.highstreet.wallet.network.res.ResLcdUnBonding;
import com.highstreet.wallet.network.res.ResOkAccountToken;
import com.highstreet.wallet.network.res.ResOkTokenList;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

import static com.highstreet.wallet.base.BaseChain.BAND_MAIN;
import static com.highstreet.wallet.base.BaseChain.BNB_MAIN;
import static com.highstreet.wallet.base.BaseChain.CERTIK_TEST;
import static com.highstreet.wallet.base.BaseChain.COSMOS_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_TEST;
import static com.highstreet.wallet.base.BaseChain.IRIS_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_TEST;
import static com.highstreet.wallet.base.BaseChain.OK_TEST;
import static com.highstreet.wallet.base.BaseConstant.IRIS_PROPOAL_TYPE_BasicProposal;
import static com.highstreet.wallet.base.BaseConstant.IRIS_PROPOAL_TYPE_CommunityTaxUsageProposal;
import static com.highstreet.wallet.base.BaseConstant.IRIS_PROPOAL_TYPE_ParameterProposal;
import static com.highstreet.wallet.base.BaseConstant.IRIS_PROPOAL_TYPE_PlainTextProposal;
import static com.highstreet.wallet.base.BaseConstant.IRIS_PROPOAL_TYPE_SoftwareUpgradeProposal;
import static com.highstreet.wallet.base.BaseConstant.IRIS_PROPOAL_TYPE_SystemHaltProposal;
import static com.highstreet.wallet.base.BaseConstant.IRIS_PROPOAL_TYPE_TokenAdditionProposal;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_ATOM;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_BAND;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_BNB;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_CERTIK_TEST;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_IOV;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_IOV_TEST;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_IRIS;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_IRIS_ATTO;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_KAVA;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_OK_TEST;

public class WUtil {

    public static Account getAccountFromLcd(long id, ResLcdAccountInfo lcd) {
        Account result = new Account();
        result.id = id;
        if (lcd.result != null && lcd.height != null) {
            if (lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT) ||
                    lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT_LEGACY) ||
                    lcd.result.type.equals(BaseConstant.IRIS_BANK_TYPE_ACCOUNT)) {
                result.address = lcd.result.value.address;
                result.sequenceNumber = Integer.parseInt(lcd.result.value.sequence);
                result.accountNumber = Integer.parseInt(lcd.result.value.account_number);
                return result;
            } else {
                result.address = lcd.result.value.BaseVestingAccount.BaseAccount.address;
                result.sequenceNumber = Integer.parseInt(lcd.result.value.BaseVestingAccount.BaseAccount.sequence);
                result.accountNumber = Integer.parseInt(lcd.result.value.BaseVestingAccount.BaseAccount.account_number);
                return result;
            }
        }
        if (lcd.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT) ||
                lcd.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT_LEGACY) ||
                lcd.type.equals(BaseConstant.IRIS_BANK_TYPE_ACCOUNT)) {
            result.address = lcd.value.address;
            result.sequenceNumber = Integer.parseInt(lcd.value.sequence);
            result.accountNumber = Integer.parseInt(lcd.value.account_number);
            return result;
        } else {
            result.address = lcd.value.BaseVestingAccount.BaseAccount.address;
            result.sequenceNumber = Integer.parseInt(lcd.value.BaseVestingAccount.BaseAccount.sequence);
            result.accountNumber = Integer.parseInt(lcd.value.BaseVestingAccount.BaseAccount.account_number);
            return result;
        }
    }

    public static Account getAccountFromBnbLcd(long id, ResBnbAccountInfo lcd) {
        Account result = new Account();
        result.id = id;
        result.address = lcd.address;
        result.sequenceNumber = Integer.parseInt(lcd.sequence);
        result.accountNumber = Integer.parseInt(lcd.account_number);
        return result;
    }

    public static Account getAccountFromKavaLcd(long id, ResLcdKavaAccountInfo lcd) {
        Account result = new Account();
        result.id = id;
        if (lcd.result != null && lcd.height != null) {
            if (lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT)) {
                result.address = lcd.result.value.address;
                result.sequenceNumber = Integer.parseInt(lcd.result.value.sequence);
                result.accountNumber = Integer.parseInt(lcd.result.value.account_number);

            } else if (lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_VESTING_ACCOUNT) || lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_P_VESTING_ACCOUNT)) {
                result.address = lcd.result.value.address;
                result.sequenceNumber = Integer.parseInt(lcd.result.value.sequence);
                result.accountNumber = Integer.parseInt(lcd.result.value.account_number);
            }
        }
        return result;
    }

    public static ArrayList<Balance> getBalancesFromLcd(long accountId, ResLcdAccountInfo lcd) {
        long time = System.currentTimeMillis();
        ArrayList<Balance> result = new ArrayList<>();
        if (lcd.result != null && lcd.height != null) {
            if(lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT) ||
                    lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT_LEGACY) ||
                    lcd.result.type.equals(BaseConstant.IRIS_BANK_TYPE_ACCOUNT)) {
                if (lcd.result.value.coins != null && lcd.result.value.coins.size() > 0){
                    for (Coin coin : lcd.result.value.coins) {
                        Balance temp = new Balance();
                        temp.accountId = accountId;
                        temp.symbol = coin.denom;
                        temp.balance = new BigDecimal(coin.amount);
                        temp.fetchTime = time;
                        result.add(temp);
                    }
                }
                return result;
            } else {
                if (lcd.result.value.BaseVestingAccount.BaseAccount.coins != null && lcd.result.value.BaseVestingAccount.BaseAccount.coins.size() > 0){
                    for(Coin coin : lcd.result.value.BaseVestingAccount.BaseAccount.coins) {
                        Balance temp = new Balance();
                        temp.accountId = accountId;
                        temp.symbol = coin.denom;
                        temp.balance = new BigDecimal(coin.amount);
                        temp.fetchTime = time;
                        result.add(temp);
                    }
                }
                return result;
            }
        }
        if(lcd.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT) ||
                lcd.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT_LEGACY) ||
                lcd.type.equals(BaseConstant.IRIS_BANK_TYPE_ACCOUNT)) {
            if (lcd.value.coins != null && lcd.value.coins.size() > 0){
                for(Coin coin : lcd.value.coins) {
                    Balance temp = new Balance();
                    temp.accountId = accountId;
                    temp.symbol = coin.denom;
                    temp.balance = new BigDecimal(coin.amount);
                    temp.fetchTime = time;
                    result.add(temp);
                }
            }
            return result;
        } else {
            if (lcd.value.BaseVestingAccount.BaseAccount.coins != null && lcd.value.BaseVestingAccount.BaseAccount.coins.size() > 0){
                for(Coin coin : lcd.value.BaseVestingAccount.BaseAccount.coins) {
                    Balance temp = new Balance();
                    temp.accountId = accountId;
                    temp.symbol = coin.denom;
                    temp.balance = new BigDecimal(coin.amount);
                    temp.fetchTime = time;
                    result.add(temp);
                }
            }
            return result;
        }
    }

    public static ArrayList<Balance> getBalancesFromBnbLcd(long accountId, ResBnbAccountInfo lcd) {
        long time = System.currentTimeMillis();
        ArrayList<Balance> result = new ArrayList<>();
        if (lcd.balances != null && lcd.balances.size() > 0) {
            for(ResBnbAccountInfo.BnbBalance coin : lcd.balances) {
                Balance temp = new Balance();
                temp.accountId = accountId;
                temp.symbol = coin.symbol;
                temp.balance = new BigDecimal(coin.free);
                temp.locked = new BigDecimal(coin.locked);
                temp.frozen = new BigDecimal(coin.frozen);
                temp.fetchTime = time;
                result.add(temp);
            }
        }
        return result;
    }

    public static ArrayList<Balance> getBalancesFromKavaLcd(long accountId, ResLcdKavaAccountInfo lcd) {
        long time = System.currentTimeMillis();
        ArrayList<Balance> result = new ArrayList<>();
        if (lcd.result != null && lcd.height != null) {
            if (lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_ACCOUNT)) {
                if (lcd.result.value.coins != null && lcd.result.value.coins.size() > 0) {
                    for (Coin coin : lcd.result.value.coins) {
                        Balance temp = new Balance();
                        temp.accountId = accountId;
                        temp.symbol = coin.denom;
                        temp.balance = new BigDecimal(coin.amount);
                        temp.fetchTime = time;
                        result.add(temp);
                    }
                }
                return result;

            }  else if (lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_VESTING_ACCOUNT) || lcd.result.type.equals(BaseConstant.COSMOS_AUTH_TYPE_P_VESTING_ACCOUNT)) {
                BigDecimal dpBalance = BigDecimal.ZERO;
                BigDecimal dpVesting = BigDecimal.ZERO;
                BigDecimal originalVesting = BigDecimal.ZERO;
                BigDecimal remainVesting = BigDecimal.ZERO;
                BigDecimal delegatedVesting = BigDecimal.ZERO;

                if (lcd.result.value.coins != null && lcd.result.value.coins.size() > 0) {
                    for (Coin coin : lcd.result.value.coins) {
                        if (coin.denom.equals(TOKEN_KAVA)) {
                            dpBalance = new BigDecimal(coin.amount);

                            if (lcd.result.value.original_vesting != null && lcd.result.value.original_vesting.size() > 0) {
                                for (Coin vesting : lcd.result.value.original_vesting) {
                                    originalVesting = originalVesting.add(new BigDecimal(vesting.amount));
                                }
                            }

                            if (lcd.result.value.delegated_vesting != null && lcd.result.value.delegated_vesting.size() > 0) {
                                for (Coin vesting : lcd.result.value.delegated_vesting) {
                                    delegatedVesting = delegatedVesting.add(new BigDecimal(vesting.amount));
                                }
                            }

                            WLog.w("dpBalance " +  dpBalance);
                            WLog.w("originalVesting " +  originalVesting);
                            WLog.w("delegatedVesting " +  delegatedVesting);

                            remainVesting = lcd.result.value.getCVestingSum();
                            WLog.w("remainVesting " +  remainVesting);

                            dpVesting = remainVesting.subtract(delegatedVesting);
                            WLog.w("dpVesting " +  dpVesting);
                            if (dpVesting.compareTo(BigDecimal.ZERO) <= 0) {
                                dpVesting = BigDecimal.ZERO;
                            }
                            WLog.w("dpVesting1 " +  dpVesting);

                            if (remainVesting.compareTo(delegatedVesting) > 0) {
                                dpBalance = dpBalance.subtract(remainVesting).add(delegatedVesting);
                            }
                            WLog.w("dpBalancee " +  dpBalance);

                            Balance temp = new Balance();
                            temp.accountId = accountId;
                            temp.symbol = TOKEN_KAVA;
                            temp.balance = dpBalance;
                            temp.frozen = delegatedVesting;
                            temp.locked = dpVesting;
                            temp.fetchTime = time;
                            result.add(temp);


                        } else {
                            Balance temp = new Balance();
                            temp.accountId = accountId;
                            temp.symbol = coin.denom;
                            temp.balance = new BigDecimal(coin.amount);
                            temp.fetchTime = time;
                            result.add(temp);
                        }
                    }
                }
            }
        }
        return result;
    }

    public static ArrayList<Balance> getBalancesFromOkLcd(long accountId, ResOkAccountToken lcd) {
        long time = System.currentTimeMillis();
        ArrayList<Balance> result = new ArrayList<>();
        if (lcd.data != null && lcd.data.currencies != null && lcd.data.currencies.size() > 0) {
            for(ResOkAccountToken.OkCurrency currency : lcd.data.currencies) {
                Balance temp = new Balance();
                temp.accountId = accountId;
                temp.symbol = currency.symbol;
                temp.balance = new BigDecimal(currency.available);
                temp.locked = new BigDecimal(currency.locked);
                temp.fetchTime = time;
                result.add(temp);
            }
        }
        return result;
    }


    public static Balance getTokenBalance(ArrayList<Balance> list, String symbol) {
        for (Balance balance:list) {
            if (balance.symbol.equals(symbol)) {
                return balance;
            }
        }
        return null;
    }


    public static ArrayList<BondingState> getBondingFromLcds(long accountId, ArrayList<ResLcdBonding> list, BaseChain chain) {
        long time = System.currentTimeMillis();
        ArrayList<BondingState> result = new ArrayList<>();
        if (chain.equals(COSMOS_MAIN) || chain.equals(KAVA_MAIN) || chain.equals(BAND_MAIN) ||
                chain.equals(KAVA_TEST) || chain.equals(IOV_MAIN) || chain.equals(IOV_TEST) || chain.equals(CERTIK_TEST)) {
            for(ResLcdBonding val : list) {
                String valAddress = "";
                if(!TextUtils.isEmpty(val.validator_addr))
                    valAddress = val.validator_addr;
                if(!TextUtils.isEmpty(val.validator_address))
                    valAddress = val.validator_address;

                BondingState temp = new BondingState(accountId, valAddress, new BigDecimal(val.shares), time);
                result.add(temp);
            }

        } else if (chain.equals(IRIS_MAIN)) {
            for(ResLcdBonding val : list) {
                String valAddress = "";
                if(!TextUtils.isEmpty(val.validator_addr))
                    valAddress = val.validator_addr;
                if(!TextUtils.isEmpty(val.validator_address))
                    valAddress = val.validator_address;

                BondingState temp = new BondingState(accountId, valAddress, new BigDecimal(val.shares).movePointRight(18), time);
                result.add(temp);
            }
        }

        return result;
    }

    public static BondingState getBondingFromLcd(long accountId, ResLcdBonding lcd, BaseChain chain) {
        String valAddress = "";
        if(!TextUtils.isEmpty(lcd.validator_addr))
            valAddress = lcd.validator_addr;
        if(!TextUtils.isEmpty(lcd.validator_address))
            valAddress = lcd.validator_address;

        if (chain.equals(COSMOS_MAIN) || chain.equals(KAVA_MAIN) || chain.equals(BAND_MAIN) ||
                chain.equals(KAVA_TEST) || chain.equals(IOV_MAIN) || chain.equals(IOV_TEST) || chain.equals(CERTIK_TEST)) {
            return new BondingState(accountId, valAddress, new BigDecimal(lcd.shares), System.currentTimeMillis());

        } else if (chain.equals(IRIS_MAIN)) {
            return new BondingState(accountId, valAddress, new BigDecimal(lcd.shares).movePointRight(18), System.currentTimeMillis());

        }
        return null;
    }

    public static ArrayList<UnBondingState> getUnbondingFromLcds(Context c, BaseChain chain, long accountId, ArrayList<ResLcdUnBonding> list) {
        long time = System.currentTimeMillis();
        ArrayList<UnBondingState> result = new ArrayList<>();
        if (chain.equals(COSMOS_MAIN) || chain.equals(KAVA_MAIN) || chain.equals(BAND_MAIN) ||
                chain.equals(KAVA_TEST) || chain.equals(IOV_MAIN) || chain.equals(IOV_TEST) || chain.equals(CERTIK_TEST)) {
            for(ResLcdUnBonding val : list) {
                String valAddress = "";
                if(!TextUtils.isEmpty(val.validator_addr))
                    valAddress = val.validator_addr;
                if(!TextUtils.isEmpty(val.validator_address))
                    valAddress = val.validator_address;

                for(ResLcdUnBonding.Entry entry:val.entries) {
                    UnBondingState temp = new UnBondingState(
                            accountId,
                            valAddress,
                            entry.creation_height,
                            WUtil.cosmosTimetoLocalLong(c, entry.completion_time),
                            new BigDecimal(entry.getinitial_balance()),
                            new BigDecimal(entry.getbalance()),
                            time
                    );
                    result.add(temp);
                }
            }

        } else if (chain.equals(IRIS_MAIN)) {
            for(ResLcdUnBonding val : list) {
                String valAddress = "";
                if(!TextUtils.isEmpty(val.validator_addr))
                    valAddress = val.validator_addr;
                if(!TextUtils.isEmpty(val.validator_address))
                    valAddress = val.validator_address;

                UnBondingState temp = new UnBondingState(
                        accountId,
                        valAddress,
                        val.creation_height,
                        WUtil.cosmosTimetoLocalLong(c, val.min_time),
                        new BigDecimal(val.initial_balance.replace("iris","")).movePointRight(18),
                        new BigDecimal(val.balance.replace("iris","")).movePointRight(18),
                        time
                );
                result.add(temp);
            }
        }
        return result;
    }

    public static ArrayList<UnBondingState> getUnbondingFromLcd(Context c, long accountId, ResLcdUnBonding lcd) {
        long time = System.currentTimeMillis();
        ArrayList<UnBondingState> result = new ArrayList<>();
        for(ResLcdUnBonding.Entry entry:lcd.entries) {
            String valAddress = "";
            if(!TextUtils.isEmpty(lcd.validator_addr))
                valAddress = lcd.validator_addr;
            if(!TextUtils.isEmpty(lcd.validator_address))
                valAddress = lcd.validator_address;

            UnBondingState temp = new UnBondingState(
                    accountId,
                    valAddress,
                    entry.creation_height,
                    WUtil.cosmosTimetoLocalLong(c, entry.completion_time),
                    new BigDecimal(entry.getinitial_balance()),
                    new BigDecimal(entry.getbalance()),
                    time
            );
            result.add(temp);
        }
        return result;
    }

    public static String prettyPrinter(Object object) {
        String result = "";
        try {
            result = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(object);
        } catch (Exception e) {
            result = "Print json error";
        }
        return result;
    }


    public static boolean checkPasscodePattern(String pincode) {
        if(pincode.length() != 5)
            return false;
        String regex = "^\\d{4}+[A-Z]{1}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(pincode);
        boolean isNormal = m.matches();
        return isNormal;
    }


    public static long cosmosTimetoLocalLong(Context c, String rawValue) {
        try {
            SimpleDateFormat cosmosFormat = new SimpleDateFormat(c.getString(R.string.str_block_time_format));
            cosmosFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return cosmosFormat.parse(rawValue).getTime();
        } catch (Exception e) {

        }
        return 0;
    }


    public static Gson getPresentor(){
//        return new GsonBuilder().disableHtmlEscaping().serializeNulls().create();
        return new GsonBuilder().disableHtmlEscaping().create();
    }



    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static int[] Bytearray2intarray(byte[] barray) {
        int[] iarray = new int[barray.length];
        int i = 0;
        for (byte b : barray)
            iarray[i++] = b & 0xff;
        return iarray;
    }

    public static String BytearryToDecimalString(byte[] barray) {
        String result = "";
        int[] iarray = new int[barray.length];
        int i = 0;
        for (byte b : barray) {
            iarray[i++] = b & 0xff;
            result = result + " " + (b & 0xff);
        }
        return result;
    }

    public static byte[] integerToBytes(BigInteger s, int length) {
        byte[] bytes = s.toByteArray();

        if (length < bytes.length) {
            byte[] tmp = new byte[length];
            System.arraycopy(bytes, bytes.length - tmp.length, tmp, 0, tmp.length);
            return tmp;
        } else if (length > bytes.length) {
            byte[] tmp = new byte[length];
            System.arraycopy(bytes, 0, tmp, tmp.length - bytes.length, bytes.length);
            return tmp;
        }
        return bytes;
    }

    public static String str2Hex(String bin) {
        char[] digital = "0123456789abcdef".toCharArray();
        StringBuffer sb = new StringBuffer("");
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(digital[bit]);
            bit = bs[i] & 0x0f;
            sb.append(digital[bit]);
        }
        return sb.toString();
    }

    public static String hexToStr(String hex) {
        String digital = "0123456789abcdef";
        char[] hex2char = hex.toCharArray();
        byte[] bytes = new byte[hex.length() / 2];
        int temp;
        for (int i = 0; i < bytes.length; i++) {
            temp = digital.indexOf(hex2char[2 * i]) * 16;
            temp += digital.indexOf(hex2char[2 * i + 1]);
            bytes[i] = (byte) (temp & 0xff);
        }
        return new String(bytes);
    }



    //TODO for ssh ignore test
    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Bitmap toBitmap(BitMatrix matrix) {
        int height = matrix.getHeight();
        int width = matrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }


    /**
     * Sorts
     */
    public static void onSortByValidatorName(ArrayList<Validator> validators) {
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if(o1.description.moniker.equalsIgnoreCase("Cosmostation")) return -1;
                if(o2.description.moniker.equalsIgnoreCase("Cosmostation")) return 1;
                return o1.description.moniker.compareTo(o2.description.moniker);
            }
        });
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if (o1.jailed && !o2.jailed) return 1;
                else if (!o1.jailed && o2.jailed) return -1;
                else return 0;
            }
        });
    }

    public static void onSortByValidatorPower(ArrayList<Validator> validators) {
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if(o1.description.moniker.equalsIgnoreCase("Cosmostation")) return -1;
                if(o2.description.moniker.equalsIgnoreCase("Cosmostation")) return 1;

                if (Double.parseDouble(o1.tokens) > Double.parseDouble(o2.tokens)) return -1;
                else if (Double.parseDouble(o1.tokens) < Double.parseDouble(o2.tokens)) return 1;
                else return 0;
            }
        });
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if (o1.jailed && !o2.jailed) return 1;
                else if (!o1.jailed && o2.jailed) return -1;
                else return 0;
            }
        });
    }

    public static void onSortByOKValidatorPower(ArrayList<Validator> validators) {
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if(o1.description.moniker.equalsIgnoreCase("Cosmostation")) return -1;
                if(o2.description.moniker.equalsIgnoreCase("Cosmostation")) return 1;

                if (Double.parseDouble(o1.delegator_shares) > Double.parseDouble(o2.delegator_shares)) return -1;
                else if (Double.parseDouble(o1.delegator_shares) < Double.parseDouble(o2.delegator_shares)) return 1;
                else return 0;
            }
        });
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if (o1.jailed && !o2.jailed) return 1;
                else if (!o1.jailed && o2.jailed) return -1;
                else return 0;
            }
        });
    }



    public static void onSortByDelegate(final long userId, ArrayList<Validator> validators, final BaseData dao) {
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if(o1.description.moniker.equalsIgnoreCase("Cosmostation")) return -1;
                if(o2.description.moniker.equalsIgnoreCase("Cosmostation")) return 1;

                BigDecimal bondingO1 = BigDecimal.ZERO;
                BigDecimal bondingO2 = BigDecimal.ZERO;
                if(dao.onSelectBondingState(userId, o1.operator_address) != null &&
                        dao.onSelectBondingState(userId, o1.operator_address).getBondingAmount(o1) != null) {
                    bondingO1  = dao.onSelectBondingState(userId, o1.operator_address).getBondingAmount(o1) ;
                }
                if(dao.onSelectBondingState(userId, o2.operator_address) != null &&
                        dao.onSelectBondingState(userId, o2.operator_address).getBondingAmount(o2)  != null) {
                    bondingO2  = dao.onSelectBondingState(userId, o2.operator_address).getBondingAmount(o2) ;
                }
                return bondingO2.compareTo(bondingO1);

            }
        });
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if (o1.jailed && !o2.jailed) return 1;
                else if (!o1.jailed && o2.jailed) return -1;
                else return 0;
            }
        });
    }

    public static void onSortByReward(ArrayList<Validator> validators, final ArrayList<Reward> rewards, String denom) {
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if(o1.description.moniker.equalsIgnoreCase("Cosmostation")) return -1;
                if(o2.description.moniker.equalsIgnoreCase("Cosmostation")) return 1;

                BigDecimal rewardO1 = WDp.getValidatorReward(rewards, o1.operator_address, denom);
                BigDecimal rewardO2 = WDp.getValidatorReward(rewards, o2.operator_address, denom);
                return rewardO2.compareTo(rewardO1);
            }
        });
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if (o1.jailed && !o2.jailed) return 1;
                else if (!o1.jailed && o2.jailed) return -1;
                else return 0;
            }
        });
    }

    public static void onSortByOnlyReward(ArrayList<Validator> validators, final ArrayList<Reward> rewards, String denom) {
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                BigDecimal rewardO1 = WDp.getValidatorReward(rewards, o1.operator_address, denom);
                BigDecimal rewardO2 = WDp.getValidatorReward(rewards, o2.operator_address, denom);
                return rewardO2.compareTo(rewardO1);
            }
        });
    }

    public static void onSortingByCommission(ArrayList<Validator> validators, final BaseChain chain) {
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if(o1.description.moniker.equalsIgnoreCase("Cosmostation")) return -1;
                if(o2.description.moniker.equalsIgnoreCase("Cosmostation")) return 1;
                if (chain.equals(COSMOS_MAIN) || chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)
                        || chain.equals(BAND_MAIN) || chain.equals(IOV_MAIN) || chain.equals(IOV_TEST) || chain.equals(CERTIK_TEST)) {
                    if (Float.parseFloat(o1.commission.commission_rates.rate) > Float.parseFloat(o2.commission.commission_rates.rate)) return 1;
                    else if (Float.parseFloat(o1.commission.commission_rates.rate) < Float.parseFloat(o2.commission.commission_rates.rate)) return -1;
                    else return 0;
                } else if (chain.equals(IRIS_MAIN)) {
                    if (Float.parseFloat(o1.commission.rate) > Float.parseFloat(o2.commission.rate)) return 1;
                    else if (Float.parseFloat(o1.commission.rate) < Float.parseFloat(o2.commission.rate)) return -1;
                    else return 0;
                }
                return 0;
            }
        });
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if (o1.jailed && !o2.jailed) return 1;
                else if (!o1.jailed && o2.jailed) return -1;
                else return 0;
            }
        });
    }

    public static void onSortingProposal(ArrayList<Proposal> proposals, BaseChain chain) {
        Collections.sort(proposals, new Comparator<Proposal>() {
            @Override
            public int compare(Proposal o1, Proposal o2) {
                if (chain.equals(COSMOS_MAIN)) {
                    if (Integer.parseInt(o1.id) < Integer.parseInt(o2.id)) return 1;
                    else if (Integer.parseInt(o1.id) > Integer.parseInt(o2.id)) return -1;
                    else return 0;

                } else if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST) || chain.equals(CERTIK_TEST)) {
                    if (Integer.parseInt(o1.id) < Integer.parseInt(o2.id)) return 1;
                    else if (Integer.parseInt(o1.id) > Integer.parseInt(o2.id)) return -1;
                    else return 0;

                }
                return 0;
            }
        });
    }

    public static void onSortingIrisProposal(ArrayList<IrisProposal> proposals) {
        Collections.sort(proposals, new Comparator<IrisProposal>() {
            @Override
            public int compare(IrisProposal o1, IrisProposal o2) {
                if (Integer.parseInt(o1.value.basicProposal.proposal_id) < Integer.parseInt(o2.value.basicProposal.proposal_id)) return 1;
                else if (Integer.parseInt(o1.value.basicProposal.proposal_id) > Integer.parseInt(o2.value.basicProposal.proposal_id)) return -1;
                else return 0;

            }
        });
    }


    public static void onSortIrisByReward(ArrayList<Validator> validators, final ResLcdIrisReward reward) {
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if(o1.description.moniker.equalsIgnoreCase("Cosmostation")) return -1;
                if(o2.description.moniker.equalsIgnoreCase("Cosmostation")) return 1;

                BigDecimal rewardO1 = reward.getPerValReward(o1.operator_address);
                BigDecimal rewardO2 = reward.getPerValReward(o2.operator_address);
                return rewardO2.compareTo(rewardO1);
            }
        });
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {
                if (o1.jailed && !o2.jailed) return 1;
                else if (!o1.jailed && o2.jailed) return -1;
                else return 0;
            }
        });
    }

    public static void onSortIrisOnlyByReward(ArrayList<Validator> validators, final ResLcdIrisReward reward) {
        Collections.sort(validators, new Comparator<Validator>() {
            @Override
            public int compare(Validator o1, Validator o2) {

                BigDecimal rewardO1 = reward.getPerValReward(o1.operator_address);
                BigDecimal rewardO2 = reward.getPerValReward(o2.operator_address);
                return rewardO2.compareTo(rewardO1);
            }
        });
    }

    public static void onSortingTokenByAmount(ArrayList<Balance> balances, final BaseChain chain) {
        Collections.sort(balances, new Comparator<Balance>() {
            @Override
            public int compare(Balance o1, Balance o2) {
                if (chain.equals(COSMOS_MAIN)) {
                    if(o1.symbol.equals(TOKEN_ATOM)) return -1;
                    if(o2.symbol.equals(TOKEN_ATOM)) return 1;

                } else if (chain.equals(IRIS_MAIN)) {
                    if(o1.symbol.equals(TOKEN_IRIS_ATTO)) return -1;
                    if(o2.symbol.equals(TOKEN_IRIS_ATTO)) return 1;

                } else if (chain.equals(BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
                    if(o1.symbol.equals(TOKEN_BNB)) return -1;
                    if(o2.symbol.equals(TOKEN_BNB)) return 1;

                } else if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)) {
                    if(o1.symbol.equals(TOKEN_KAVA)) return -1;
                    if(o2.symbol.equals(TOKEN_KAVA)) return 1;
                    return o2.balance.movePointLeft(WUtil.getKavaCoinDecimal(o2.symbol)).compareTo(o1.balance.movePointLeft(WUtil.getKavaCoinDecimal(o1.symbol)));

                } else if (chain.equals(IOV_MAIN)) {
                    if(o1.symbol.equals(TOKEN_IOV)) return -1;
                    if(o2.symbol.equals(TOKEN_IOV)) return 1;

                } else if (chain.equals(BAND_MAIN)) {
                    if(o1.symbol.equals(TOKEN_BAND)) return -1;
                    if(o2.symbol.equals(TOKEN_BAND)) return 1;

                } else if (chain.equals(IOV_TEST)) {
                    if(o1.symbol.equals(TOKEN_IOV_TEST)) return -1;
                    if(o2.symbol.equals(TOKEN_IOV_TEST)) return 1;

                } else if (chain.equals(OK_TEST)) {
                    if(o1.symbol.equals(TOKEN_OK_TEST)) return -1;
                    if(o2.symbol.equals(TOKEN_OK_TEST)) return 1;

                } else if (chain.equals(CERTIK_TEST)) {
                    if(o1.symbol.equals(TOKEN_CERTIK_TEST)) return -1;
                    if(o2.symbol.equals(TOKEN_CERTIK_TEST)) return 1;

                }
                return o2.balance.compareTo(o1.balance);
            }
        });
    }

    public static void onSortingTokenByName(ArrayList<Balance> balances, final BaseChain chain) {
        Collections.sort(balances, new Comparator<Balance>() {
            @Override
            public int compare(Balance o1, Balance o2) {
                if (chain.equals(COSMOS_MAIN)) {
                    if(o1.symbol.equals(TOKEN_ATOM)) return -1;
                    if(o2.symbol.equals(TOKEN_ATOM)) return 1;

                } else if (chain.equals(IRIS_MAIN)) {
                    if(o1.symbol.equals(TOKEN_IRIS_ATTO)) return -1;
                    if(o2.symbol.equals(TOKEN_IRIS_ATTO)) return 1;

                } else if (chain.equals(BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
                    if(o1.symbol.equals(TOKEN_BNB)) return -1;
                    if(o2.symbol.equals(TOKEN_BNB)) return 1;

                } else if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)) {
                    if(o1.symbol.equals(TOKEN_KAVA)) return -1;
                    if(o2.symbol.equals(TOKEN_KAVA)) return 1;

                } else if (chain.equals(IOV_MAIN)) {
                    if(o1.symbol.equals(TOKEN_IOV)) return -1;
                    if(o2.symbol.equals(TOKEN_IOV)) return 1;

                } else if (chain.equals(BAND_MAIN)) {
                    if(o1.symbol.equals(TOKEN_BAND)) return -1;
                    if(o2.symbol.equals(TOKEN_BAND)) return 1;

                } else if (chain.equals(IOV_TEST)) {
                    if(o1.symbol.equals(TOKEN_IOV_TEST)) return -1;
                    if(o2.symbol.equals(TOKEN_IOV_TEST)) return 1;

                } else if (chain.equals(OK_TEST)) {
                    if(o1.symbol.equals(TOKEN_OK_TEST)) return -1;
                    if(o2.symbol.equals(TOKEN_OK_TEST)) return 1;

                } else if (chain.equals(CERTIK_TEST)) {
                    if(o1.symbol.equals(TOKEN_CERTIK_TEST)) return -1;
                    if(o2.symbol.equals(TOKEN_CERTIK_TEST)) return 1;

                }
                return o1.symbol.compareTo(o2.symbol);
            }
        });
    }

    public static void onSortingBnbTokenByValue(ArrayList<Balance> balances, HashMap<String, ResBnbTic> tics) {
        Collections.sort(balances, new Comparator<Balance>() {
            @Override
            public int compare(Balance o1, Balance o2) {
                if(o1.symbol.equals(TOKEN_BNB)) return -1;
                if(o2.symbol.equals(TOKEN_BNB)) return 1;

                ResBnbTic tic1 = tics.get(WUtil.getBnbTicSymbol(o1.symbol));
                ResBnbTic tic2 = tics.get(WUtil.getBnbTicSymbol(o2.symbol));
                if (tic1 != null && tic2 != null) {
                    BigDecimal o1Amount = o1.exchangeToBnbAmount(tic1);
                    BigDecimal o2Amount = o2.exchangeToBnbAmount(tic2);
                    return o2Amount.compareTo(o1Amount);
                } else {
                    return 0;
                }
            }
        });
    }

    public static void onSortingKavaTokenByValue(ArrayList<Balance> balances, HashMap<String, ResKavaMarketPrice.Result> prices, ResCdpParam.Result params) {
        Collections.sort(balances, new Comparator<Balance>() {
            @Override
            public int compare(Balance o1, Balance o2) {
                if(o1.symbol.equals(TOKEN_KAVA)) return -1;
                if(o2.symbol.equals(TOKEN_KAVA)) return 1;

                return o2.kavaTokenDollorValue(prices, params).compareTo(o1.kavaTokenDollorValue(prices, params));
            }
        });
    }


    public static void onSortingCoins(ArrayList<Coin> coins, BaseChain chain) {
        Collections.sort(coins, new Comparator<Coin>() {
            @Override
            public int compare(Coin o1, Coin o2) {
                if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)) {
                    if(o1.denom.equals(TOKEN_KAVA)) return -1;
                    if(o2.denom.equals(TOKEN_KAVA)) return 1;
                    else return 0;
                } else if (chain.equals(OK_TEST)) {
                    if(o1.denom.equals(TOKEN_OK_TEST)) return -1;
                    if(o2.denom.equals(TOKEN_OK_TEST)) return 1;
                    else return 0;

                } else {
                    return 0;
                }
            }
        });
    }

    public static void onSortUnbondingsRecent(ArrayList<UnBondingState> UnBondingStates) {
        Collections.sort(UnBondingStates, new Comparator<UnBondingState>() {
            @Override
            public int compare(UnBondingState o1, UnBondingState o2) {
                return o1.completionTime < o2.completionTime ?  -1 : 1;

            }
        });
    }


    public static void onSortingAccount(ArrayList<Account> accounts) {
        Collections.sort(accounts, new Comparator<Account>() {
            @Override
            public int compare(Account o1, Account o2) {
                if (o1.sortOrder > o2.sortOrder) return 1;
                else if (o1.sortOrder < o2.sortOrder) return -1;
                else return 0;

            }
        });
    }

    public static ArrayList<Validator> getTopVals(ArrayList<Validator> allValidators) {
        ArrayList<Validator> result = new ArrayList<>();
        for(Validator v:allValidators) {
            if(v.status == Validator.BONDED) {
                result.add(v);
            }
        }
        return result;

    }

    public static ArrayList<Validator> getOthersVals(ArrayList<Validator> allValidators) {
        ArrayList<Validator> result = new ArrayList<>();
        for(Validator v:allValidators) {
            if(v.status != Validator.BONDED) {
                result.add(v);
            }
        }
        return result;
    }

    public static Validator selectValidatorByAddr(ArrayList<Validator> validators, String opAddr) {
        for (Validator v:validators) {
            if (v.operator_address.equals(opAddr)) {
                return v;
            }
        }
        return null;
    }


    public static int getCMCId(BaseChain chain) {
        if (chain.equals(COSMOS_MAIN)) {
            return BaseConstant.CMC_ATOM;

        } else if (chain.equals(IRIS_MAIN)) {
            return BaseConstant.CMC_IRIS;

        } else if (chain.equals(BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
            return BaseConstant.CMC_BNB;

        } else if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)) {
            return BaseConstant.CMC_KAVA;
        }
        return BaseConstant.CMC_ATOM;
    }

    public static String getCGCId(BaseChain chain) {
        if (chain.equals(COSMOS_MAIN)) {
            return BaseConstant.CGC_ATOM;

        } else if (chain.equals(IRIS_MAIN)) {
            return BaseConstant.CGC_IRIS;

        } else if (chain.equals(BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
            return BaseConstant.CGC_BNB;

        } else if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)) {
            return BaseConstant.CGC_KAVA;

        } else if (chain.equals(BAND_MAIN)) {
            return BaseConstant.CGC_BAND;

        } else if (chain.equals(IOV_MAIN) || chain.equals(IOV_TEST)) {
            return BaseConstant.CGC_IOV;
        }
        return BaseConstant.CGC_ATOM;
    }

    public static int getMaxMemoSize(BaseChain chain) {
        if (chain.equals(COSMOS_MAIN) || chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST) ||
                chain.equals(IOV_MAIN) || chain.equals(BAND_MAIN) || chain.equals(IOV_TEST) || chain.equals(OK_TEST) || chain.equals(CERTIK_TEST)) {
            return BaseConstant.MEMO_ATOM;

        } else if (chain.equals(IRIS_MAIN)) {
            return BaseConstant.MEMO_IRIS;

        } else if (chain.equals(BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
            return BaseConstant.MEMO_BNB;
        }
        return BaseConstant.MEMO_IRIS;
    }

    public static int getCharSize(String memo) {
        int result = 1000;
        try {
            result = memo.trim().getBytes("UTF-8").length;
        } catch (Exception e) { }

        return result;
    }

    public static ResCdpParam.KavaCollateralParam getCdpCoinParm(ResCdpParam.Result params, Balance balance) {
        if (params != null) {
            for (ResCdpParam.KavaCollateralParam param:params.collateral_params) {
                if (param.denom.equals(balance.symbol)) {
                    return param;
                }
            }
            return null;

        } else {
            return null;
        }
    }

    public static int getKavaCoinDecimal(ResCdpParam.Result params, Balance balance) {
        int result = 0;
        if (params != null) {
            if (params.debt_param != null) {
                return Integer.parseInt(params.debt_param.conversion_factor);
            }

            if (params.collateral_params != null) {
                for (ResCdpParam.KavaCollateralParam collateralParams: params.collateral_params) {
                    if (collateralParams.denom.equals(balance.symbol)) {
                        return Integer.parseInt(collateralParams.conversion_factor);
                    }
                }
            }
        }

        return result;
    }

    public static int getKavaCoinDecimal(Coin coin) {
        if (coin.denom.equalsIgnoreCase(TOKEN_KAVA)) {
            return 6;
        } else if (coin.denom.equalsIgnoreCase("xrp")) {
            return 6;
        } else if (coin.denom.equalsIgnoreCase("btc")) {
            return 8;
        } else if (coin.denom.equalsIgnoreCase("usdx")) {
            return 6;
        } else if (coin.denom.equalsIgnoreCase("bnb")) {
            return 8;
        } else if (coin.denom.equalsIgnoreCase("btcb")) {
            return 8;
        }
        return 0;

    }

    public static int getKavaCoinDecimal(String denom) {
        if (denom.equalsIgnoreCase(TOKEN_KAVA)) {
            return 6;
        } else if (denom.equalsIgnoreCase("xrp")) {
            return 6;
        } else if (denom.equalsIgnoreCase("btc")) {
            return 8;
        } else if (denom.equalsIgnoreCase("usdx")) {
            return 6;
        } else if (denom.equalsIgnoreCase("bnb")) {
            return 8;
        } else if (denom.equalsIgnoreCase("btcb")) {
            return 8;
        }
        return 100;
    }

    public static BnbToken getBnbToken(ArrayList<BnbToken> all, Balance balance) {
        if (all == null || balance == null) return null;
        for (BnbToken token:all) {
            if (token.symbol.equals(balance.symbol)) {
                return token;
            }
        }
        return null;
    }

    public static BnbToken getBnbToken(ArrayList<BnbToken> all, String symbol) {
        if (all == null || symbol == null) return null;
        for (BnbToken token:all) {
            if (token.symbol.equals(symbol)) {
                return token;
            }
        }
        return null;
    }


    public static BnbToken getBnbMainToken(ArrayList<BnbToken> all) {
        if (all == null) return null;
        for (BnbToken token:all) {
            if (token.original_symbol.equals(TOKEN_BNB)) {
                return token;
            }
        }
        return null;
    }

    public static IrisToken getIrisToken(ArrayList<IrisToken> all, Balance balance) {
        if (all == null || balance == null) return null;
        for (IrisToken token:all) {
            if(balance.symbol.split("-")[0].equals(token.base_token.id)) {
                return token;
            }
        }
        return null;
    }

    public static OkToken getOkToken(ResOkTokenList okTokenList, String denom) {
        if (okTokenList == null || okTokenList.data == null || TextUtils.isEmpty(denom)) return null;
        for (OkToken token:okTokenList.data) {
            if (token.symbol.equals(denom)) {
                return token;
            }
        }
        return null;
    }

    public static IrisToken getIrisMainToken(ArrayList<IrisToken> all) {
        if (all == null) return null;
        for (IrisToken token:all) {
            if (token.base_token.id.equals(TOKEN_IRIS)) {
                return token;
            }
        }
        return null;
    }

    public static IovToken getIovToken(ArrayList<IovToken> all, Balance balance) {
        if (all == null || balance == null) return null;
        for (IovToken token:all) {
            if(balance.symbol.equals(token.tokenTicker)) {
                return token;
            }
        }
        return null;
    }

    public static IovToken getIovMainToken(ArrayList<IovToken> all) {
        if (all == null) return null;
        for (IovToken token:all) {
            if (token.tokenTicker.equals(TOKEN_IOV)) {
                return token;
            }
        }
        return null;
    }

    public static BigDecimal getQuotient(String value) {
        BigDecimal dividend = new BigDecimal(value);
        return dividend.divide(BigDecimal.ONE, 0, RoundingMode.DOWN);
    }

    public static BigDecimal getRemainder(String value) {
        BigDecimal dividend = new BigDecimal(value);
        BigDecimal quotient = dividend.divide(BigDecimal.ONE, 0, RoundingMode.DOWN);
        return  dividend.subtract(quotient);
    }


    public static boolean isBnbBaseMarketToken(String symbol) {
        switch (symbol) {
            case "USDT.B-B7C":
                return true;
            case "ETH.B-261":
                return true;
            case "BTC.B-918":
                return true;


            case "USDSB-1AC":
                return true;
            case "THKDB-888":
                return true;
            case "TUSDB-888":
                return true;
            case "BTCB-1DE":
                return true;

            case "ETH-1C9":
                return true;
            case "IDRTB-178":
                return true;
            case "BUSD-BD1":
                return true;
            case "TAUDB-888":
                return true;


        }
        return false;
    }

    public static String getBnbTicSymbol(String symbol) {
        if (isBnbBaseMarketToken(symbol)) {
            return TOKEN_BNB + "_" + symbol;

        } else {
            return symbol + "_"+TOKEN_BNB;
        }
    }


    public static String getIrisProposalType(Context c, String type) {
        String result = c.getString(R.string.str_iris_proposal_type_BasicProposal);
        if (type.equals(IRIS_PROPOAL_TYPE_BasicProposal)) {
            result = c.getString(R.string.str_iris_proposal_type_BasicProposal);
        } else if (type.equals(IRIS_PROPOAL_TYPE_ParameterProposal)) {
            result = c.getString(R.string.str_iris_proposal_type_ParameterProposal);
        } else if (type.equals(IRIS_PROPOAL_TYPE_PlainTextProposal)) {
            result = c.getString(R.string.str_iris_proposal_type_PlainTextProposal);
        } else if (type.equals(IRIS_PROPOAL_TYPE_TokenAdditionProposal)) {
            result = c.getString(R.string.str_iris_proposal_type_TokenAdditionProposal);
        } else if (type.equals(IRIS_PROPOAL_TYPE_SoftwareUpgradeProposal)) {
            result = c.getString(R.string.str_iris_proposal_type_SoftwareUpgradeProposal);
        } else if (type.equals(IRIS_PROPOAL_TYPE_SystemHaltProposal)) {
            result = c.getString(R.string.str_iris_proposal_type_SystemHaltProposal);
        } else if (type.equals(IRIS_PROPOAL_TYPE_CommunityTaxUsageProposal)) {
            result = c.getString(R.string.str_iris_proposal_type_CommunityTaxUsageProposal);
        }
        return result;
    }


    public static String getIrisMonikerName(ArrayList<Validator> validators, String address) {
        String opAddress = WKey.convertDpAddressToDpOpAddress(address, IRIS_MAIN);
        String result = address;
        for (Validator v:validators) {
            if (v.operator_address.equals(opAddress)) {
                result = v.description.moniker;
            }
        }
        return result;
    }

    public static int getVoterTypeCnt(ArrayList<Vote> votes, String option) {
        int result = 0;
        if (votes == null) {
            return result;
        }
        for (Vote v:votes) {
            if (v.option.equals(option)) {
                result = result + 1;
            }
        }
        return result;
    }

    public static Vote getMyVote(ArrayList<Vote> votes, String address) {
        if (votes == null) {
            return null;
        }
        for (Vote v:votes) {
            if (v.voter.equals(address)) {
                return v;
            }
        }
        return null;

    }

    public static String getMonikerName(String opAddress, ArrayList<Validator> validators, boolean bracket) {
        String result = "";
        for (Validator val:validators) {
            if (val.operator_address.equals(opAddress)) {
                if (bracket) {
                    return  "(" + val.description.moniker + ")";
                } else {
                    return  val.description.moniker;
                }

            }
        }
        return result;
    }


    public static byte[] long2Bytes(long x){
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(x);
        return buffer.array();
    }

    public static boolean isValidStarName(String starname) {
        boolean result = false;
        String regex = "[0-9a-z.-]{0,64}+\\*[a-z0-9.-]{3,16}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(starname);
        if (m.matches()) {
            result = true;
        }
        return result;
    }

    public static boolean isValidDomain(String starname) {
        boolean result = false;
        String regex = "[a-z0-9]{4,32}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(starname);
        if (m.matches()) {
            result = true;
        }
        return result;
    }

    public static boolean isValidAccount(String starname) {
        boolean result = false;
        String regex = "[a-z0-9.-]{1,63}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(starname);
        if (m.matches()) {
            result = true;
        }
        return result;
    }


    public static ArrayList<StarNameResource> allResources() {
        ArrayList<StarNameResource> result = new ArrayList();
        result.add(new StarNameResource(COSMOS));
        result.add(new StarNameResource(BITCOIN));
        result.add(new StarNameResource(ETHEREUM));
        result.add(new StarNameResource(STARNAME));
        result.add(new StarNameResource(BINANCE));
        result.add(new StarNameResource(IRIS));
        result.add(new StarNameResource(KAVA));
        result.add(new StarNameResource(BAND));
        result.add(new StarNameResource(BITCOINCASH));
        result.add(new StarNameResource(LITECOIN));
        result.add(new StarNameResource(EMONEY));
        result.add(new StarNameResource(TEZOS));
        result.add(new StarNameResource(LISK));
        result.add(new StarNameResource(LUNA));
        return result;

    }

    public static ArrayList<StarNameResource> getInitStarnameResource() {
        ArrayList<StarNameResource> result = new ArrayList();
//        result.add(new StarNameResource(COSMOS));
//        result.add(new StarNameResource(BITCOIN));
//        result.add(new StarNameResource(ETHEREUM));
        result.add(new StarNameResource(STARNAME));
//        result.add(new StarNameResource(BINANCE));
//        result.add(new StarNameResource(IRIS));
//        result.add(new StarNameResource(KAVA));
//        result.add(new StarNameResource(BAND));
        return result;
    }

    public static ArrayList<StarNameResource> getAddableStarnameResource(ArrayList<StarNameResource> already) {
        ArrayList<StarNameResource> result = new ArrayList();
        for (StarNameResource resource:allResources()){
            if (!already.contains(resource)) {
                result.add(resource);
            }
        }
        return result;
    }

    public static BaseChain getBaseChainWithUri(String uri) {
        if (uri.equals(COSMOS)) {
            return COSMOS_MAIN;
        } else if (uri.equals(IRIS)) {
            return IRIS_MAIN;
        } else if (uri.equals(BINANCE)) {
            return BNB_MAIN;
        } else if (uri.equals(STARNAME)) {
            return IOV_MAIN;
        } else if (uri.equals(KAVA)) {
            return KAVA_MAIN;
        } else if (uri.equals(BAND)) {
            return BAND_MAIN;
        }
        return null;
    }



    public static String BITCOINCASH    = "asset:bch";
    public static String BITCOIN        = "asset:btc";
    public static String LITECOIN       = "asset:ltc";
    public static String BINANCE        = "asset:bnb";
    public static String LUNA           = "asset:luna";
    public static String COSMOS         = "asset:atom";
    public static String EMONEY         = "asset:ngm";
    public static String IRIS           = "asset:iris";
    public static String KAVA           = "asset:kava";
    public static String ETHEREUM       = "asset:eth";
    public static String STARNAME       = "asset:iov";
    public static String BAND           = "asset:band";
    public static String TEZOS          = "asset:xtz";
    public static String LISK           = "asset:lsk";

    public static Drawable getStarNameChainImg(Context c, StarNameResource res) {
        if (res.uri.equals(BITCOINCASH)) {
            return c.getResources().getDrawable(R.drawable.bcash_chain_img);

        } else if (res.uri.equals(BITCOIN)) {
            return c.getResources().getDrawable(R.drawable.bitcoin_chain_img);

        } else if (res.uri.equals(LITECOIN)) {
            return c.getResources().getDrawable(R.drawable.lite_chain_img);

        } else if (res.uri.equals(BINANCE)) {
            return c.getResources().getDrawable(R.drawable.binance_ch_img);

        } else if (res.uri.equals(LUNA)) {
            return c.getResources().getDrawable(R.drawable.terra_chain_img);

        } else if (res.uri.equals(COSMOS)) {
            return c.getResources().getDrawable(R.drawable.cosmos_wh_main);

        } else if (res.uri.equals(EMONEY)) {
            return c.getResources().getDrawable(R.drawable.emoney_chain_img);

        } else if (res.uri.equals(IRIS)) {
            return c.getResources().getDrawable(R.drawable.iris_wh);

        } else if (res.uri.equals(KAVA)) {
            return c.getResources().getDrawable(R.drawable.kava_img);

        } else if (res.uri.equals(ETHEREUM)) {
            return c.getResources().getDrawable(R.drawable.ethereum_chain_img);

        } else if (res.uri.equals(STARNAME)) {
            return c.getResources().getDrawable(R.drawable.iov_chain_img);

        } else if (res.uri.equals(BAND)) {
            return c.getResources().getDrawable(R.drawable.band_chain_img);

        } else if (res.uri.equals(TEZOS)) {
            return c.getResources().getDrawable(R.drawable.tezos_chain_img);

        } else if (res.uri.equals(LISK)) {
            return c.getResources().getDrawable(R.drawable.lisk_chain_img);

        }
        return c.getResources().getDrawable(R.drawable.default_chain_img);
    }

    public static String getStarNameChainName(StarNameResource res) {
        if (res.uri.equals(BITCOINCASH)) {
            return "BitCoin Cash";

        } else if (res.uri.equals(BITCOIN)) {
            return "BitCoin";

        } else if (res.uri.equals(LITECOIN)) {
            return "LiteCoin";

        } else if (res.uri.equals(BINANCE)) {
            return "Binance";

        } else if (res.uri.equals(LUNA)) {
            return "Terra";

        } else if (res.uri.equals(COSMOS)) {
            return "Cosmos";

        } else if (res.uri.equals(EMONEY)) {
            return "E-Money";

        } else if (res.uri.equals(IRIS)) {
            return "Iris";

        } else if (res.uri.equals(KAVA)) {
            return "Kava";

        } else if (res.uri.equals(ETHEREUM)) {
            return "Ethereum";

        } else if (res.uri.equals(STARNAME)) {
            return "Starname";

        } else if (res.uri.equals(BAND)) {
            return "Band";

        } else if (res.uri.equals(TEZOS)) {
            return "Tezos";

        } else if (res.uri.equals(LISK)) {
            return "Lisk";

        }
        return res.uri;

    }

}
