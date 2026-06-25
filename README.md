# EconomySMP

A comprehensive economy mod for Minecraft Fabric 26.1 featuring:

- 💰 Economy system (coins, balance, transactions)
- 🛒 Shop GUI with buy/sell functionality
- 📦 Marketplace system with orders
- 🏪 Auction House with bidding system
- 💎 Shards (premium currency) - Coming Soon
- 🎁 Animated crates - Coming Soon
- ✨ 40+ custom enchantments - Coming Soon
- 📈 Skills and leveling system - Coming Soon
- 🏦 Bank system - Coming Soon
- 🏡 Homes and warps - Coming Soon
- ⚡ Teleport commands - Coming Soon
- 📊 Leaderboards - Coming Soon
- 🎯 Daily rewards - Coming Soon

## Phase 3.5 - Auction House System ✅

- ✅ Auction creation and listing
- ✅ Bidding system
- ✅ Real-time bid tracking
- ✅ Auto-completion on expiration
- ✅ 7-day auction duration
- ✅ Bid history
- ✅ Seller and bidder management
- ✅ Automatic refunds for outbid players

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

### Auction House
- `/ah sell <item> <quantity> <price>` - List item for auction
- `/ah view` - View all active auctions
- `/ah bid <auctionId> <amount>` - Place a bid on an auction
- `/ah cancel <auctionId>` - Cancel your auction
- `/ah myauctions` - View your listed auctions
- `/ah mybids` - View your active bids

## Auction House Features

- **7-Day Auctions**: Items listed for 7 days
- **Bidding System**: Players can bid higher to win
- **Auto Refunds**: Outbid players automatically get refunded
- **Auction History**: Track all your auctions and bids
- **Real-Time Tracking**: See time remaining on auctions
- **Persistent Storage**: All auctions saved to disk

## Example Auction House Usage

```
/ah sell diamond 1 1000
  → List 1 diamond starting bid 1000 coins

/ah view
  → See all active auctions

/ah bid auction_123456 1500
  → Bid 1500 coins on auction

/ah myauctions
  → View your listed auctions

/ah mybids
  → View auctions you're bidding on

/ah cancel auction_123456
  → Cancel your auction
```

## Next Phase

Shards & Crates system with animated crate openings and rewards.
