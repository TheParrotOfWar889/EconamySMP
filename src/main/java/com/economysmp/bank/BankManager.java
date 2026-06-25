package com.economysmp.bank;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankManager {
    private static final Map<UUID, BankAccount> accounts = new HashMap<>();

    public static BankAccount getOrCreateAccount(UUID playerUuid) {
        return accounts.computeIfAbsent(playerUuid, BankAccount::new);
    }

    public static BankAccount getAccount(UUID playerUuid) {
        return accounts.get(playerUuid);
    }

    public static boolean depositCoins(UUID playerUuid, long amount) {
        BankAccount account = getOrCreateAccount(playerUuid);
        account.deposit(amount);
        return true;
    }

    public static boolean withdrawCoins(UUID playerUuid, long amount) {
        BankAccount account = getOrCreateAccount(playerUuid);
        return account.withdraw(amount);
    }

    public static long getBalance(UUID playerUuid) {
        BankAccount account = accounts.get(playerUuid);
        return account != null ? account.balance : 0;
    }
}
