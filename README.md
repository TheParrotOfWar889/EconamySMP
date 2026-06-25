# EconomySMP

A comprehensive economy mod for Minecraft Fabric 26.1 featuring:

- 💰 Economy system (coins, balance, transactions)
- 🛒 Shop GUI with buy/sell functionality
- 📦 Marketplace system with orders
- 🏛️ Auction House with bidding system
- 💎 Shards (premium currency)
- 🎁 Animated crates with 5 tiers
- ✨ 40+ custom enchantments - Coming Soon
- 📈 Skills and leveling system - Coming Soon
- 🏦 Bank system - Coming Soon
- 🏡 Homes and warps - Coming Soon
- ⚡ Teleport commands - Coming Soon
- 📊 Leaderboards - Coming Soon
- 🎯 Daily rewards - Coming Soon

## Phase 4 - Shards & Crates System ✅

- ✅ Shard currency system
- ✅ 5 Crate tiers (Common, Rare, Epic, Legendary, Mythic)
- ✅ Random reward system
- ✅ Animated crate opening
- ✅ Reward rarity system
- ✅ Crate cooldown system
- ✅ Shard shop display
- ✅ Rich reward descriptions

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

### Crates & Shards
- `/crate open <type>` - Open a crate (Common, Rare, Epic, Legendary, Mythic)
- `/crate info <type>` - Get info about a crate type
- `/shardshop` - View shard shop and crate costs

## Crate System

### Crate Tiers & Costs
- **Common Crate**: 10 Shards
- **Rare Crate**: 25 Shards
- **Epic Crate**: 50 Shards
- **Legendary Crate**: 100 Shards
- **Mythic Crate**: 250 Shards

### Possible Rewards by Tier

**Common**: Diamonds, Gold Ingots, Emeralds, Iron, 100 Coins
**Rare**: More Diamonds, More Gold, Pickaxes, 500 Coins
**Epic**: Large quantities of gems, Iron Pickaxe, 2000 Coins
**Legendary**: Massive gem hoards, Diamond Pickaxe, 5000 Coins, 100 Shards
**Mythic**: Ultimate treasures, 100 Diamonds, 200 Gold, 128 Emeralds, 10000 Coins, 500 Shards

## Example Crate Usage

```
/shardshop
  → See crate costs and your shard balance

/crate info legendary
  → Learn about Legendary crates

/crate open common
  → Open a common crate (costs 10 shards)
  → Get random reward!

/crate open mythic
  → Open ultimate crate (costs 250 shards)
  → Chance at 500 shards or 10000 coins!
```

## Complete Feature Set

✅ Economy System (Phase 1)
✅ Shop GUI (Phase 2)
✅ Marketplace (Phase 3)
✅ Auction House (Phase 3.5)
✅ Shards & Crates (Phase 4)

## Next Phase

Custom Enchantments & Skills system.
