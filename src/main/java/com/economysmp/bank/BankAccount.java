package com.economysmp.bank;

import java.util.UUID;

public class BankAccount {
    public UUID playerUuid;
    public long balance;
    public long lastInterestTime;
    public static final long INTEREST_RATE = 5; // 5% interest
    public static final long INTEREST_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours

    public BankAccount(UUID playerUuid) {
        this.playerUuid = playerUuid;
        this.balance = 0;
        this.lastInterestTime = System.currentTimeMillis();
    }

    public void deposit(long amount) {
        this.balance += amount;
    }

    public boolean withdraw(long amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        return false;
    }

    public long calculateInterest() {
        long timeSinceLastInterest = System.currentTimeMillis() - lastInterestTime;
        if (timeSinceLastInterest >= INTEREST_INTERVAL) {
            long interest = (this.balance * INTEREST_RATE) / 100;
            this.balance += interest;
            this.lastInterestTime = System.currentTimeMillis();
            return interest;
        }
        return 0;
    }

    public long getTimeUntilNextInterest() {
        long timeSinceLastInterest = System.currentTimeMillis() - lastInterestTime;
        long timeUntilNextInterest = INTEREST_INTERVAL - timeSinceLastInterest;
        return Math.max(0, timeUntilNextInterest);
    }
}
