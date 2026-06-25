# EconomySMP

A comprehensive economy mod for Minecraft Fabric 26.1 featuring:

- 💰 Economy system (coins, balance, transactions)
- 🛒 Shop GUI with buy/sell functionality
- 📦 Marketplace system with orders
- 🏛️ Auction House with bidding system
- 💎 Shards (premium currency)
- 🎁 Animated crates with 5 tiers
- 📊 Skills & Leveling (Mining, Combat, Fishing, Farming, Woodcutting)
- 🏦 Bank system with 5% daily interest
- 🏡 Homes & Warps (up to 6 homes)
- ⚡ Random Teleport (`/rtp`)
- ⚔️ Custom Enchantments - Coming Soon
- 📈 Leaderboards - Coming Soon
- 🎯 Daily rewards - Coming Soon

## Phase 5 - Skills, Bank, Homes & RTP ✅

- ✅ Skills & Leveling system (5 skills)
- ✅ Bank accounts with 5% daily interest
- ✅ Home system (up to 6 homes)
- ✅ Random teleport command
- ✅ Home management (set, go, list, delete)
- ✅ XP progression system
- ✅ Interest calculation

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

### Skills & Leveling
- `/skills` - View your skill levels and XP

### Bank System
- `/bank balance` - Check your bank balance
- `/bank deposit <amount>` - Deposit coins into bank
- `/bank withdraw <amount>` - Withdraw coins from bank

### Homes & Teleportation
- `/home set <name>` - Set a home at your current location
- `/home go <name>` - Teleport to a home
- `/home list` - List all your homes
- `/home delete <name>` - Delete a home
- `/rtp` - Random teleport to a random location in the world

## Skills System

Earn XP in 5 different skills:
- **Mining**: Break ore blocks
- **Combat**: Defeat mobs and players
- **Fishing**: Fish in water
- **Farming**: Harvest crops
- **Woodcutting**: Chop trees

Each skill levels up at 1000 XP per level!

## Bank System Features

- **Safe Storage**: Keep coins safe in the bank
- **5% Interest**: Earn 5% interest per day on your balance
- **Automated Interest**: Interest calculated automatically
- **Deposit/Withdraw**: Easy coin management

## Homes System

- **Up to 6 homes**: Set multiple home locations
- **Quick Teleport**: Instantly teleport to any home
- **Easy Management**: Add, view, and delete homes
- **Cross-World**: Works across multiple worlds

## Random Teleport

- **Explore**: Teleport to random locations
- **Adventure**: Discover new areas
- **Simple Command**: Just use `/rtp`

## Example Usage

```
/skills
  → View your skill levels and XP

/bank deposit 1000
  → Deposit 1000 coins, earn 50 coins per day in interest!

/home set spawn
  → Set a home at your current location

/home go spawn
  → Teleport back to your spawn home

/home list
  → See all 6 of your homes

/rtp
  → Teleport to a random location!
```

## Complete Feature Set

✅ Phase 1: Economy System
✅ Phase 2: Shop GUI
✅ Phase 3: Marketplace
✅ Phase 3.5: Auction House
✅ Phase 4: Shards & Crates
✅ Phase 5: Skills, Bank, Homes & RTP

## Next Phase

Custom Enchantments & Advanced Systems.
