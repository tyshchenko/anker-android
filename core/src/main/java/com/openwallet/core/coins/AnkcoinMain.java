package com.openwallet.core.coins;

import com.openwallet.core.coins.families.BitFamily;

/**
 * @author John L. Jegutanis
 */
public class AnkcoinMain extends BitFamily {
    private AnkcoinMain() {
        id = "ank.main"; // Do not change this id as wallets serialize this string

        addressHeader = 55;
        p2shHeader = 13;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 100;
        dumpedPrivateKeyHeader = 212;

        name = "Anker";
        symbol = "ANK";
        uriScheme = "anker"; // TODO add multi uri, darkcoin
        bip44Index = 267;
        unitExponent = 8;
        feeValue = value(10000);
        minNonDust = value(10000); // 0.000001 ANK mininput
        softDustLimit = value(100000); // 0.0001 ANK
        softDustPolicy = SoftDustPolicy.BASE_FEE_FOR_EACH_SOFT_DUST_TXO;
        signedMessageHeader = toBytes("Anker Signed Message:\n");
    }

    private static AnkcoinMain instance = new AnkcoinMain();
    public static synchronized CoinType get() {
        return instance;
    }
}
