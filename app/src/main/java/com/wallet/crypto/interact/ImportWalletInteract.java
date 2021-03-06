package com.wallet.crypto.interact;

import com.wallet.crypto.entity.Wallet;
import com.wallet.crypto.interact.rx.operator.Operators;
import com.wallet.crypto.repository.TrustPasswordStore;
import com.wallet.crypto.repository.WalletRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ImportWalletInteract {

    private final WalletRepository walletRepository;
    private final TrustPasswordStore passwordStore;

    public ImportWalletInteract(WalletRepository walletRepository, TrustPasswordStore passwordStore) {
        this.walletRepository = walletRepository;
        this.passwordStore = passwordStore;
    }

    public Single<Wallet> importKeystore(String keystore, String password) {
        return passwordStore
                .generatePassword()
                .flatMap(newPassword -> walletRepository
                        .importKeystoreToWallet(keystore, password, newPassword)
                        .compose(Operators.savePassword(passwordStore, walletRepository, newPassword)))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<Wallet> importPrivateKey(String privateKey) {
        return passwordStore
                .generatePassword()
                .flatMap(newPassword -> walletRepository
                        .importPrivateKeyToWallet(privateKey, newPassword)
                        .compose(Operators.savePassword(passwordStore, walletRepository, newPassword)))
                .observeOn(AndroidSchedulers.mainThread());
    }
}
