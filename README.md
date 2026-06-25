# EconomySMP

A comprehensive economy mod for Minecraft Fabric 26.1 featuring:

- 💰 Economy system (coins, balance, transactions)
- 🛒 Shop GUI with buy/sell functionality
- 📦 Marketplace system with orders
- 💎 Shards (premium currency) - Coming Soon
- 🎁 Animated crates - Coming Soon
- ✨ 40+ custom enchantments - Coming Soon
- 📈 Skills and leveling system - Coming Soon
- 🏦 Bank system - Coming Soon
- 🏡 Homes and warps - Coming Soon
- ⚡ Teleport commands - Coming Soon
- 📊 Leaderboards - Coming Soon
- 🎯 Daily rewards - Coming Soon

## Phase 3 - Marketplace System ✅

- ✅ Order creation (buy/sell)
- ✅ Order management
- ✅ Order browsing
- ✅ Order transactions
- ✅ Order expiration (48 hours)
- ✅ Player order history
- ✅ Coin transfers between players
- ✅ Order cancellation

## How to Build

1. Clone this repository
2. Run: `./gradlew build`
3. The JAR will be in `build/libs/`
4. Move it to your Minecraft mods folder

## Commands

### Economy
- `/balance` - Check your coins and shards
- `/money` - Alternative to `/balance`
- `/pay <player> <amount>` - Send coins to another player
- `/addmoney <player> <amount>` - Admin command to add coins

### Shop
- `/shop` - Open the shop GUI

### Marketplace
- `/order sell <item> <quantity> <price>` - Create a sell order
- `/order buy <item> <quantity> <price>` - Create a buy order
- `/browse sell` - View all active sell orders
- `/browse buy` - View all active buy orders
- `/buy <orderId> <quantity>` - Purchase items from an order
- `/cancelorder <orderId>` - Cancel your own order
- `/myorders` - View your active orders

## Marketplace Features

- **Sell Orders**: Put items up for sale that other players can buy
- **Buy Orders**: Post what you want to buy and at what price
- **Auto Expiration**: Orders expire after 48 hours
- **Transaction History**: Track all your trades
- **Order Status**: View if orders are active, completed, or expired
- **Player Trading**: Direct player-to-player economy

## Example Marketplace Usage

```
/order sell dirt 64 10
  → Creates a sell order: 64 dirt at 10 coins each

/browse sell
  → Shows all active sell orders

/buy order_123456 32
  → Buy 32 dirt from order_123456 for 320 coins

/myorders
  → View all your active orders

/cancelorder order_123456
  → Cancel your order
```

## Next Phase

Shards & Crates system with animated crate openings and rewards.
