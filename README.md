# SC2002 Restaurant Rush

**SC2002 — Object-Oriented Design & Programming**  
**GitHub:** https://github.com/Qunming/SC2002-Restaurant-Rush

Restaurant Rush is an offline, turn-based Java restaurant management game. The manager prioritises seating, orders, food preparation and service under limited staff capacity, aiming to meet revenue, paid-customer and satisfaction targets within 18 turns.

The project comprises **three compulsory, independently runnable stages** and one **optional enhanced Stage 3 release**. The fourth tag must not be confused with the compulsory `stage-3` submission.

## 1. Releases and Git tags

| Tag | Scope | Submission status |
|---|---|---|
| [`stage-1`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-1) | Regular customers, three menu items, Waiter/Chef/Cashier, full 18-turn gameplay, victory and defeat | **Required** |
| [`stage-2`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-2) | Adds VIP and Critic customers with polymorphic behaviour | **Required** |
| [`stage-3`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-3) | Adds Host, ComboMeal and Happy Hour | **Required** |
| [`stage-3-with-additional-features`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-3-with-additional-features) | Adds cooking timing, food quality, reviews and tips | **Optional enhanced release** |

The optional fourth tag was verified to reference the same source commit as the existing `main` branch **before this README update**. This README can be updated on `main` without moving or rewriting any release tags.

## 2. Requirements and how to run

- **Java JDK 21 or later**, with `java` and `javac` available on PATH.
- Git for cloning and switching tags.
- No third-party Java libraries or network connection required during gameplay.
- Commands below are for **Windows PowerShell**.

```powershell
git clone https://github.com/Qunming/SC2002-Restaurant-Rush.git
cd SC2002-Restaurant-Rush
java -version
javac -version

# Choose ONE of the four tagged releases:
git switch --detach stage-1
# git switch --detach stage-2
# git switch --detach stage-3
# git switch --detach stage-3-with-additional-features

# Clean old compiled files before changing stages:
if (Test-Path build) { Remove-Item build -Recurse -Force }

# Compile and run:
javac -encoding UTF-8 -sourcepath src -d build src/app/Main.java
java -cp build app.Main
```

For each other version, switch to the relevant tag, remove `build`, compile and run again. A **detached HEAD** is expected when checking out a tag. To return to the active development branch, use `git switch main` after saving any uncommitted changes. Do **not** combine files from different tagged releases.

On Windows Command Prompt, replace the PowerShell clean command with `if exist build rmdir /s /q build`.

**Submission verification:** These build commands are documented from repository source and existing instructions. Perform a fresh local compile and playthrough for each tag before claiming that every release passes testing.

## 3. Base game configuration and rules

| Setting | Value |
|---|---|
| Duration | Exactly 18 turns; no early ending |
| Tables | 2 |
| Stage 1/2 staff | Waiter ×1, Chef ×1, Cashier ×1 |
| Stage 3 staff addition | Host ×1; Waiter retains seating ability |
| Arrivals | One each at start of turns **1, 4, 7, 10, 13, 16** |
| Initial satisfaction | 100 |
| Victory after turn 18 | **At least 4 paid customers**, **at least $45 food revenue**, and **at least 60 average satisfaction of paid customers** |
| Defeat | Any victory condition unmet after turn 18 |

At the start of a turn, scheduled arrivals join the queue. The manager chooses Waiter and Chef tasks (and Host actions in Stage 3). The Waiter acts before the Chef; a dish finished by the Chef this turn cannot be served until a later turn. Served customers require an eating turn before becoming READY_TO_PAY, and payment occurs on a subsequent eligible turn. The Cashier automatically processes at most one eligible payment per turn, oldest first. Customers waiting for food or a table lose satisfaction at turn end; customers reaching zero abandon without payment. A table remains occupied until payment or abandonment.

Invalid staff choices should be explained and retried without consuming a staff task. Revenue includes paid orders only. In the enhanced release, **tips are tracked separately and do not count towards the $45 food-revenue target**.

### 3.1 Menu

| Menu item | Price | Preparation units | Available from |
|---|---:|---:|---|
| Salad | $9.00 | 1 | Stage 1 |
| Burger | $15.00 | 1 | Stage 1 |
| Pasta | $18.00 | 2 | Stage 1 |
| Salad + Pasta Combo | $24.30 | 3 | Stage 3 |

Customers, not the manager, choose food. Regular and VIP customers use a deterministic menu selection based on `(customerId - 1) % menu.getItems().size()`; the Critic chooses the most expensive menu item. The fixed Stage 2/3 arrival mix is Regular (turn 1), VIP (4), Regular (7), Critic (10), Regular (13), Regular (16).

### 3.2 Customer behaviour from Stage 2 onwards

| Customer | Unserved satisfaction loss/turn | Discount | Food preference |
|---|---:|---:|---|
| Regular | 8 | None | Deterministic menu position |
| VIP | 5 | 10% | Deterministic menu position |
| Critic | 12 | None | Most expensive available item |

All customers share the same service and payment lifecycle. Their differing behaviours are dispatched via customer objects, rather than a chain of type checks in the controller.

## 4. Required Stage 3 extensions

**Host:** An additional staff member can seat at most one queued customer per turn independently of the Waiter. Both use the existing table-management rules; the Waiter retains the ability to seat a customer.

**ComboMeal:** `ComboMeal extends MenuItem`. It combines two existing menu items: price is **90% of the sum** and preparation units are added. Salad + Pasta costs `($9 + $18) × 90% = $24.30` and needs `1 + 2 = 3` preparation units. The combo follows the normal order workflow.

**Happy Hour:** The manager may activate this once per game, at turn start. It remains active during the activation turn and the next turn. During each active turn, customers still waiting for a table or food recover 15 satisfaction (maximum 100); newly created orders receive 20% off. Earlier orders are not repriced. VIP discount is applied **before** Happy Hour: a $15 VIP Burger during Happy Hour is `$15 × 0.90 × 0.80 = $10.80`. The final bill is locked at order placement. Money is stored as integer cents.

## 5. Optional fourth tag: additional gameplay features

The following features exist in **`stage-3-with-additional-features`**, not in the compulsory `stage-3` release.

| Feature | Files | Effect |
|---|---|---|
| Cooking timing challenge | `src/challenge/CookingChallenge.java` | Player presses ENTER to start/stop a real-time timer for the selected dish |
| Cooking quality | `src/order/CookingQuality.java`, `Order.java` | `PERFECT` within ±0.5 s of target; otherwise `NORMAL` |
| Customer feedback | `src/feedback/FeedbackType.java`, `Review.java`, `ReviewManager.java` | Records reviews after payment or abandonment; VIP and Critic have distinct quality expectations |
| Tipping | `src/policy/TipCalculator.java`, `GameState.java`, `Cashier.java` | Paid PERFECT dish: Regular $2, VIP $3, Critic $5 tip; NORMAL: $0 |

Cooking target times in code: Salad **2 s**, Burger **3 s**, Pasta **5 s**, Salad + Pasta Combo **6 s**. Abandoned customers give BAD feedback. The extended release uses actual keyboard timing, so quality and tips depend on player performance. Tips remain separate from food revenue and do not change the compulsory victory thresholds.

## 6. Object-oriented architecture

| Responsibility | Principal classes |
|---|---|
| Application setup | `app.Main` |
| Turn control and shared game state | `GameController`, `GameState`, `GameConfig` |
| Customers and lifecycle | `Customer`, `RegularCustomer`, `VIPCustomer`, `CriticCustomer`, `CustomerManager` |
| Seating and tables | `Table`, `TableManager` |
| Menu, orders and preparation progress | `Menu`, `MenuItem`, `ComboMeal`, `Order`, `OrderManager` |
| Staff actions | `Waiter`, `Chef`, `Cashier`, `Host` |
| Console boundary | `ConsoleUI`, `InputHandler` |
| Optional extension logic | `CookingChallenge`, `ReviewManager`, `TipCalculator` |

The design demonstrates encapsulated object state, polymorphic customer behaviour, division of boundary/control/domain responsibilities, reuse of table management for the Host and reuse of the `MenuItem`/`Order` workflow for ComboMeal. The PDF report contains the detailed UML diagrams, design discussion, trade-offs and functional evidence.

## 7. GitHub evidence — changes between tagged versions

The changes below were verified by comparing **source-file snapshots of the actual tag commits**. This avoids assuming strictly linear history: the `stage-1...stage-2` and `stage-2...stage-3` GitHub comparisons report **diverged histories**. Each table lists all new and changed source files and explains why. **No files were deleted** in any of the three transitions. `README.md` also changed in both compulsory transitions to document the corresponding release.

### 7.1 Stage 1 → Stage 2

**Comparison:** [stage-1...stage-2](https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-1...stage-2)

**New files**

| File | Why added |
|---|---|
| `src/customer/RegularCustomer.java` | Makes the existing Regular behaviour explicit in the new customer hierarchy |
| `src/customer/VIPCustomer.java` | Overrides waiting penalty and discount for VIP behaviour |
| `src/customer/CriticCustomer.java` | Overrides waiting penalty and food selection for Critic behaviour |

**Modified files**

| File | Why changed |
|---|---|
| `src/controller/GameController.java` | Creates the required mix of customer subclasses and retains the common turn sequence |
| `src/controller/GameState.java` | Updates shared game-state and statistics implementation for the evolved version |
| `src/customer/Customer.java` | Adds overridable customer type, satisfaction loss and discount behaviour |
| `src/customer/CustomerManager.java` | Manages different customer types and their specific waiting penalties |
| `src/menu/Menu.java` | Refactors menu storage and access while retaining base menu items |
| `src/menu/MenuItem.java` | Refines item representation and input validation |
| `src/order/Order.java` | Calculates and locks the appropriate customer-discounted bill |
| `src/order/OrderManager.java` | Validates selected items and maintains order creation |
| `src/staff/Cashier.java` | Updates payment/state validation within the evolved implementation |
| `src/ui/ConsoleUI.java` | Displays customer types, discounted bills and updated game information |
| `README.md` | Describes Stage 2 features, usage and rules |

**Deleted:** None.

### 7.2 Stage 2 → Stage 3

**Comparison:** [stage-2...stage-3](https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-2...stage-3)

**New files**

| File | Why added |
|---|---|
| `src/staff/Host.java` | Introduces independent Host seating within one task per turn |
| `src/menu/ComboMeal.java` | Defines a combined menu item using existing component items |

**Modified files**

| File | Why changed |
|---|---|
| `src/app/Main.java` | Constructs and wires the Host |
| `src/controller/GameConfig.java` | Defines two-turn Happy Hour duration |
| `src/controller/GameController.java` | Coordinates Host actions and Happy Hour activation |
| `src/controller/GameState.java` | Tracks activation and remaining Happy Hour duration |
| `src/customer/Customer.java` | Supports capped satisfaction recovery |
| `src/customer/CustomerManager.java` | Applies recovery to eligible customers |
| `src/menu/Menu.java` | Registers ComboMeal alongside existing items |
| `src/order/Order.java` | Applies VIP then Happy Hour discount at creation and locks price |
| `src/order/OrderManager.java` | Supplies Happy Hour state for new order creation |
| `src/staff/Waiter.java` | Passes Happy Hour status when taking an order |
| `src/ui/ConsoleUI.java` | Displays Host and Happy Hour information |
| `src/ui/InputHandler.java` | Adds inputs for Host and promotion decisions |
| `README.md` | Describes Stage 3 features, usage and rules |

**Deleted:** None.

### 7.3 Stage 3 → Stage 3 with Additional Features (optional)

**Comparison:** [stage-3...stage-3-with-additional-features](https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-3...stage-3-with-additional-features)

**New files**

| File | Why added |
|---|---|
| `src/challenge/CookingChallenge.java` | Interactive cooking-timing mini-game |
| `src/feedback/FeedbackType.java` | Enum for feedback categories |
| `src/feedback/Review.java` | Customer review data |
| `src/feedback/ReviewManager.java` | Review storage and duplicate prevention |
| `src/order/CookingQuality.java` | Enum for cooking-quality result |
| `src/policy/TipCalculator.java` | Tip calculations separate from compulsory food revenue |

**Modified files**

| File | Why changed |
|---|---|
| `src/app/Main.java` | Instantiates and injects cooking challenge with a shared Scanner |
| `src/controller/GameController.java` | Runs cooking challenges and creates departure feedback |
| `src/controller/GameState.java` | Holds review manager and tip accounting |
| `src/customer/Customer.java` | Adds baseline quality-related feedback |
| `src/customer/VIPCustomer.java` | Adds distinct VIP expectations and comments |
| `src/customer/CriticCustomer.java` | Adds distinct Critic expectations and comments |
| `src/order/Order.java` | Stores resulting cooking quality |
| `src/staff/Cashier.java` | Records tips after payment |
| `src/ui/ConsoleUI.java` | Displays additional quality, feedback and tip information |

**Deleted:** None. `README.md` was unchanged in the tagged source snapshot during this optional transition; this submission README documents the fourth release on `main`.

## 8. Reproducible Gameplay Test Evidence

The three compulsory stages were independently compiled
and tested against their Git release tags.

Full 18-turn victory and defeat scenarios, including
action tables and verified final scoreboards, are available
in [Reproducible Gameplay Tests](docs/test-evidence/ReproducibleGameplayTests.md).

Additional queue- and table-abandonment test logs
are available in `docs/test-evidence/`.
