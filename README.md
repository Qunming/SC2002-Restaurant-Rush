
# SC2002 Restaurant Rush

**SC2002 — Object-Oriented Design & Programming**  
**GitHub Repository:** https://github.com/Qunming/SC2002-Restaurant-Rush

Restaurant Rush is an offline, turn-based Java restaurant management game. The player acts as a restaurant manager, making decisions about seating, orders, food preparation, service and customer satisfaction while managing limited staff capacity.

The objective is to meet the required revenue, paid-customer and average satisfaction targets within 18 turns.

The project comprises **three compulsory, independently runnable stages** and one **optional enhanced release**. Each stage is preserved using its own Git tag.

---

## 1. Releases and Git Tags

| Git Tag | Features | Submission Status |
|---|---|---|
| [`stage-1`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-1) | Basic restaurant operation with Regular customers, Waiter, Chef and Cashier | **Required** |
| [`stage-2`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-2) | Adds VIP and Critic customers using inheritance and polymorphism | **Required** |
| [`stage-3`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-3) | Adds Host, ComboMeal and Happy Hour | **Required** |
| [`stage-3-with-additional-features`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-3-with-additional-features) | Adds cooking timing challenges, cooking quality, customer feedback and tipping | **Optional enhanced release** |

Each release tag identifies an independently runnable version of the game.

The compulsory `stage-3` release is kept separate from the optional enhanced release. Additional features do not replace or modify the compulsory Stage 3 requirements.

The original release snapshots are also preserved under `archive/` tags following documentation-only updates.

---

## 2. System Requirements and Execution

### 2.1 Requirements

- Java Development Kit (JDK) 21 or later
- Git for cloning the repository and switching between releases
- Windows PowerShell or Command Prompt
- No external Java libraries required
- No internet connection required during gameplay

### 2.2 Clone the Repository

Open PowerShell and run:

```powershell
git clone https://github.com/Qunming/SC2002-Restaurant-Rush.git
cd SC2002-Restaurant-Rush
```

Verify that Java is installed:

```powershell
java -version
javac -version
```

### 2.3 Select a Release

Choose **one** of the following versions:

```powershell
# Stage 1
git switch --detach stage-1

# Stage 2
git switch --detach stage-2

# Stage 3 (compulsory)
git switch --detach stage-3

# Stage 3 with additional features (optional)
git switch --detach stage-3-with-additional-features
```

Only run the command for the version you want to play.

A detached HEAD is expected when checking out a Git tag.

### 2.4 Compile and Run

Remove previously compiled classes before switching between releases:

```powershell
if (Test-Path build) {
    Remove-Item build -Recurse -Force
}
```

Compile the selected release:

```powershell
javac --release 21 -encoding UTF-8 -sourcepath src -d build src/app/Main.java
```

Start the game:

```powershell
java -cp build app.Main
```

The application runs in the terminal and accepts numerical menu selections.

The optional enhanced release also requires interactive ENTER key presses for its cooking timing challenges.

### 2.5 Switching Between Versions

To play another stage:

1. Finish or exit the current game.
2. Switch to the relevant Git tag.
3. Delete the existing `build` directory.
4. Compile and run the newly selected version.

To return to the main branch:

```powershell
git switch main
```

Save any uncommitted changes before switching Git references.

Do not combine source files from different stage releases.

---

## 3. Base Game Configuration and Rules

### 3.1 Game Configuration

| Setting | Specification |
|---|---|
| Game duration | 18 turns |
| Number of tables | 2 |
| Customer arrivals | Turns 1, 4, 7, 10, 13 and 16 |
| Initial customer satisfaction | 100 |
| Stage 1 and 2 staff | Waiter, Chef and Cashier |
| Stage 3 additional staff | Host |
| Required paid customers | At least 4 |
| Required food revenue | At least $45.00 |
| Required average satisfaction | At least 60 among paid customers |

The game always completes all 18 turns.

**Victory** occurs when all three requirements are satisfied after Turn 18:

1. At least four customers have paid.
2. Total food revenue is at least $45.00.
3. Average satisfaction among paid customers is at least 60.

**Defeat** occurs if any one of these conditions is not met.

### 3.2 Customer Arrivals

One customer arrives at the beginning of each scheduled arrival turn:

`1, 4, 7, 10, 13, 16`

Customers initially join the waiting queue with 100 satisfaction.

The player must decide when to seat customers, take orders, prepare food and serve completed dishes.

### 3.3 Staff Responsibilities

**Waiter**

The Waiter can perform one task per turn:

- Seat one waiting customer
- Take an order from a seated customer
- Serve an eligible READY order
- Wait

The Waiter can also view order history without consuming a staff action.

**Chef**

The Chef can perform one task per turn:

- Prepare one unit of an eligible order
- Wait

An order requiring multiple preparation units needs multiple Chef actions.

**Cashier**

The Cashier operates automatically.

- Processes at most one eligible payment per turn
- Selects the oldest eligible customer
- Records the paid amount and customer satisfaction
- Releases the customer's table after payment

The Cashier does not require a player-controlled action.

### 3.4 Game Turn Sequence

**Stages 1 and 2**

1. Customer arrival, if scheduled
2. Restaurant status display
3. Waiter action
4. Chef action
5. Eating progress and automatic payment
6. Satisfaction reduction and abandonment
7. End-of-turn summary

**Stage 3 and the optional enhanced release**

1. Customer arrival, if scheduled
2. Happy Hour activation decision and eligible satisfaction recovery
3. Restaurant status display
4. Host action
5. Waiter action
6. Chef action
7. Eating progress and automatic payment
8. Satisfaction reduction and abandonment
9. End-of-turn summary

### 3.5 Order and Customer Lifecycle

The normal order lifecycle is:

`PLACED → PREPARING → READY → SERVED → PAID`

For a one-unit dish, the order may move directly from PLACED to READY after preparation.

Important timing rules:

- The Chef can begin preparing an order during the same turn in which it is placed.
- An order cannot be served during the turn it first becomes READY.
- A served customer finishes eating on a later turn.
- Payment is processed on a subsequent eligible turn.
- The Cashier handles at most one payment per turn.
- A table remains occupied until payment or abandonment.

Customers who have not been served lose satisfaction at the end of each turn.

If satisfaction reaches zero, the customer abandons the restaurant without paying.

Any unpaid, unserved order is cancelled, and an occupied table is released.

Invalid staff actions are rejected and can be retried without consuming the staff member's task.

---

## 4. Menu and Customer Behaviour

### 4.1 Menu Items

| Menu Item | Price | Preparation Units | Availability |
|---|---:|---:|---|
| Salad | $9.00 | 1 | Stage 1 onwards |
| Burger | $15.00 | 1 | Stage 1 onwards |
| Pasta | $18.00 | 2 | Stage 1 onwards |
| Salad + Pasta Combo | $24.30 | 3 | Stage 3 onwards |

Customers choose their own food automatically; the player does not select the menu item.

For Regular and VIP customers, menu selection follows:

```java
(customerId - 1) % menu.getItems().size()
```

Critic customers instead select the most expensive available item.

### 4.2 Stage 1: Regular Customers

Stage 1 contains only Regular customers.

Regular customers:

- Start with 100 satisfaction
- Lose 8 satisfaction per unserved turn
- Receive no discount
- Select food using the deterministic menu-selection rule

### 4.3 Stage 2: VIP and Critic Customers

Stage 2 introduces customer subclasses using inheritance and polymorphism.

| Customer Type | Satisfaction Loss per Unserved Turn | Discount | Food Selection |
|---|---:|---:|---|
| Regular | 8 | 0% | Deterministic selection |
| VIP | 5 | 10% | Deterministic selection |
| Critic | 12 | 0% | Most expensive menu item |

The fixed Stage 2 and Stage 3 customer arrival sequence is:

| Arrival Turn | Customer Type |
|---|---|
| 1 | Regular |
| 4 | VIP |
| 7 | Regular |
| 10 | Critic |
| 13 | Regular |
| 16 | Regular |

All three types share the same customer lifecycle.

The differences are implemented using overridden customer methods, such as:

- `getCustomerType()`
- `getWaitingSatisfactionLoss()`
- `getDiscountPercent()`
- `chooseMenuItem()`

This demonstrates polymorphism without requiring the controller to implement separate service workflows for each customer type.

---

## 5. Required Stage 3 Features

Stage 3 extends Stage 2 with three compulsory features.

### 5.1 Host

The Host is an additional staff member who can seat one waiting customer per turn.

The Host acts independently of the Waiter.

- Host: Seat customer or wait
- Waiter: Retains all existing actions, including seating
- Both share the same `TableManager`
- The restaurant still has only two tables

This provides an additional seating opportunity without removing the Waiter's existing responsibilities.

### 5.2 ComboMeal

`ComboMeal` extends `MenuItem`.

A ComboMeal combines two existing menu items.

Its price is 90% of the sum of the component prices:

```text
Combo price = (Item A price + Item B price) × 0.90
```

Its preparation requirement is the sum of the component preparation units.

For the Salad + Pasta Combo:

```text
Original price = $9.00 + $18.00 = $27.00
Combo price    = $27.00 × 0.90 = $24.30

Preparation units = 1 + 2 = 3
```

The ComboMeal uses the existing order and preparation workflow.

### 5.3 Happy Hour

Happy Hour is a once-per-game promotion activated by the player at the start of a turn.

It lasts for two consecutive turns:

- The activation turn
- The following turn

During each active turn:

1. Customers awaiting service recover 15 satisfaction, capped at 100.
2. Newly placed orders receive a 20% Happy Hour discount.

Orders created before Happy Hour retain their original prices.

VIP discounts are applied before the Happy Hour discount.

For example, a $15.00 Burger ordered by a VIP customer during Happy Hour:

```text
Original price     = $15.00
After VIP discount = $15.00 × 0.90 = $13.50
After Happy Hour   = $13.50 × 0.80 = $10.80
```

The final price is calculated and locked when the order is created.

The program stores monetary values as integer cents to avoid floating-point rounding errors in financial calculations.

---

## 6. Optional Enhanced Release

The optional release is available under:

[`stage-3-with-additional-features`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-3-with-additional-features)

It builds on the compulsory Stage 3 features without changing the required game duration, victory conditions, customer satisfaction penalties, menu prices or food revenue calculations.

### 6.1 Cooking Timing Challenge

The enhanced release introduces a timing challenge when an order completes its final preparation unit.

The player presses ENTER once to start the timer and again to stop it.

The target time depends on the dish.

| Dish | Target Time | PERFECT Range |
|---|---:|---|
| Salad | 2 seconds | 1.5–2.5 seconds |
| Burger | 3 seconds | 2.5–3.5 seconds |
| Pasta | 5 seconds | 4.5–5.5 seconds |
| Salad + Pasta Combo | 6 seconds | 5.5–6.5 seconds |

A result within ±0.5 seconds of the target earns `PERFECT` quality.

Other results earn `NORMAL` quality.

The cooking challenge does not consume an additional staff task or game turn.

Multi-unit orders still require their normal number of Chef preparation actions.

### 6.2 Customer Feedback

After a customer pays or abandons the restaurant, feedback is generated and displayed during gameplay.

Feedback uses the customer's type and cooking quality.

| Customer Type | PERFECT Food | NORMAL Food |
|---|---|---|
| Regular | GOOD | MEDIOCRE |
| VIP | GOOD | BAD |
| Critic | GOOD | BAD |

An abandoned customer always gives BAD feedback.

Customer-specific feedback is implemented through polymorphism.

Reviews are stored using `Review` and `ReviewManager`, and duplicate reviews are prevented.

The enhanced release also provides review history for inspection.

### 6.3 Tipping System

Tips are awarded when customers pay for PERFECT-quality food.

| Customer Type | PERFECT Tip | NORMAL Tip |
|---|---:|---:|
| Regular | $2.00 | $0.00 |
| VIP | $3.00 | $0.00 |
| Critic | $5.00 | $0.00 |

Tips are recorded separately from food revenue.

They do not affect:

- The $45.00 revenue requirement
- The paid-customer requirement
- Average customer satisfaction
- The mandatory victory and defeat conditions

The program prevents duplicate tip recording for the same order.

### 6.4 Implementation Files

| Feature | Principal Classes |
|---|---|
| Cooking challenge | `CookingChallenge` |
| Cooking quality | `CookingQuality`, `Order` |
| Feedback | `FeedbackType`, `Review`, `ReviewManager` |
| Customer-specific comments | `Customer`, `VIPCustomer`, `CriticCustomer` |
| Tips | `TipCalculator`, `GameState`, `Cashier` |
| Gameplay integration | `GameController`, `Main`, `ConsoleUI` |

---

## 7. Object-Oriented Architecture

The game separates responsibilities across application, controller, domain, staff and user-interface classes.

| Responsibility | Principal Classes |
|---|---|
| Application setup | `Main` |
| Game progression and shared state | `GameController`, `GameState`, `GameConfig` |
| Customer management | `Customer`, `CustomerManager`, customer subclasses |
| Seating management | `Table`, `TableManager` |
| Menu and food definitions | `Menu`, `MenuItem`, `ComboMeal` |
| Order management | `Order`, `OrderManager` |
| Staff actions | `Waiter`, `Chef`, `Cashier`, `Host` |
| User interface | `ConsoleUI`, `InputHandler` |
| Optional enhancement logic | `CookingChallenge`, `ReviewManager`, `TipCalculator` |

The design demonstrates:

- **Encapsulation:** Customer, Order and GameState manage their own internal data.
- **Inheritance:** RegularCustomer, VIPCustomer and CriticCustomer extend Customer.
- **Polymorphism:** Customer-specific satisfaction loss, discounts, food selection and optional feedback use overridden methods.
- **Composition and object collaboration:** GameState coordinates managers and shared restaurant data.
- **Separation of responsibilities:** The controller manages gameplay flow while domain and staff classes enforce business rules.
- **Extensibility:** New customer behaviour and additional features reuse existing workflows.

Detailed UML class diagrams, sequence diagrams, design principles and trade-offs are provided in the accompanying PDF report.

---

## 8. GitHub Evidence and Stage Comparisons

Each compulsory stage is preserved in an independently runnable Git tag.

The following comparisons document the actual changes between the released source snapshots.

Because some release histories diverged during development, the tables below are based on the files present in each tagged snapshot rather than assuming a strictly linear Git history.

All three stage transitions are additive in functionality. No Java source files were deleted between the listed releases.

### 8.1 Stage 1 to Stage 2

**GitHub comparison:**  
[Compare Stage 1 and Stage 2](https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-1...stage-2)

**New files**

| File | Reason |
|---|---|
| `src/customer/RegularCustomer.java` | Makes Regular customer behaviour explicit in the subclass hierarchy |
| `src/customer/VIPCustomer.java` | Implements VIP satisfaction penalties and discounts |
| `src/customer/CriticCustomer.java` | Implements Critic satisfaction penalties and food preferences |

**Modified files**

| File | Reason |
|---|---|
| `src/controller/GameController.java` | Introduces the fixed customer-type arrival sequence |
| `src/controller/GameState.java` | Refines game statistics and shared state |
| `src/customer/Customer.java` | Adds overridable customer behaviour methods |
| `src/customer/CustomerManager.java` | Supports multiple customer subclasses |
| `src/menu/Menu.java` | Refactors menu storage and access |
| `src/menu/MenuItem.java` | Refines item representation and validation |
| `src/order/Order.java` | Applies customer discounts when orders are created |
| `src/order/OrderManager.java` | Refines order creation and validation |
| `src/staff/Cashier.java` | Strengthens payment and state validation |
| `src/ui/ConsoleUI.java` | Displays customer types and discounted bills |
| `README.md` | Documents Stage 2 features, rules and execution |

**Deleted Java files:** None.

Stage 2 preserves the original Stage 1 gameplay while adding polymorphic customer behaviour.

### 8.2 Stage 2 to Stage 3

**GitHub comparison:**  
[Compare Stage 2 and Stage 3](https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-2...stage-3)

**New files**

| File | Reason |
|---|---|
| `src/staff/Host.java` | Introduces independent Host seating actions |
| `src/menu/ComboMeal.java` | Introduces combined menu items and preparation requirements |

**Modified files**

| File | Reason |
|---|---|
| `src/app/Main.java` | Instantiates and connects the Host |
| `src/controller/GameConfig.java` | Defines Happy Hour duration |
| `src/controller/GameController.java` | Coordinates Host and Happy Hour gameplay |
| `src/controller/GameState.java` | Tracks Happy Hour activation and duration |
| `src/customer/Customer.java` | Supports satisfaction recovery |
| `src/customer/CustomerManager.java` | Applies Happy Hour recovery to eligible customers |
| `src/menu/Menu.java` | Registers ComboMeal |
| `src/order/Order.java` | Applies and locks Happy Hour pricing |
| `src/order/OrderManager.java` | Passes promotion state during order creation |
| `src/staff/Waiter.java` | Uses Happy Hour status when taking orders |
| `src/ui/ConsoleUI.java` | Displays Host and Happy Hour information |
| `src/ui/InputHandler.java` | Adds Host and Happy Hour selections |
| `README.md` | Documents Stage 3 features, rules and execution |

**Deleted Java files:** None.

Stage 3 maintains the customer behaviour introduced in Stage 2 while incorporating Host, ComboMeal and Happy Hour.

### 8.3 Stage 3 to Optional Enhanced Release

**GitHub comparison:**  
[Compare Stage 3 and Additional Features](https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-3...stage-3-with-additional-features)

**New files**

| File | Reason |
|---|---|
| `src/challenge/CookingChallenge.java` | Introduces interactive cooking timing |
| `src/feedback/FeedbackType.java` | Defines feedback categories |
| `src/feedback/Review.java` | Represents customer reviews |
| `src/feedback/ReviewManager.java` | Stores reviews and prevents duplicates |
| `src/order/CookingQuality.java` | Defines cooking quality categories |
| `src/policy/TipCalculator.java` | Calculates tips separately from food revenue |

**Modified files**

| File | Reason |
|---|---|
| `src/app/Main.java` | Instantiates CookingChallenge using a shared Scanner |
| `src/controller/GameController.java` | Integrates cooking challenges and departure feedback |
| `src/controller/GameState.java` | Tracks reviews and tips |
| `src/customer/Customer.java` | Adds baseline customer feedback methods |
| `src/customer/VIPCustomer.java` | Adds VIP-specific feedback |
| `src/customer/CriticCustomer.java` | Adds Critic-specific feedback |
| `src/order/Order.java` | Records cooking quality |
| `src/staff/Cashier.java` | Records tips after payment |
| `src/ui/ConsoleUI.java` | Displays cooking, feedback and tip information |
| `README.md` | Documents the optional release and its gameplay |

**Deleted Java files:** None.

The optional enhanced release preserves the compulsory Stage 3 game rules and introduces additional gameplay systems independently of the mandatory victory conditions.

---

## 9. Reproducible Gameplay Tests

The three compulsory stages were independently compiled and tested against their published release implementations.

The tests cover complete 18-turn gameplay, including victory, defeat, customer abandonment, revenue calculations and customer satisfaction outcomes.

### 9.1 Verified Results

| Release | Test Scenario | Paid Customers | Food Revenue | Avg Satisfaction | Result |
|---|---|---:|---:|---:|---|
| Stage 1 | Victory | 4 | $51.00 | 78.0 | VICTORY |
| Stage 1 | Queue abandonment | 0 | $0.00 | N/A | DEFEAT |
| Stage 2 | Victory | 4 | $58.50 | 75.5 | VICTORY |
| Stage 2 | Queue abandonment | 0 | $0.00 | N/A | DEFEAT |
| Stage 3 | Victory with Happy Hour | 5 | $68.94 | 86.2 | VICTORY |
| Stage 3 | No-service scenario | 0 | $0.00 | N/A | DEFEAT |

Defeat is the expected outcome for the designated defeat scenarios.

Stages 1 and 2 were also tested for abandonment while occupying a table.

All recorded scenarios completed the required 18 turns.

### 9.2 Reproducible Action Tables

The complete gameplay test documentation is available here:

**[Reproducible Gameplay Tests — Full 18-Turn Tables](docs/test-evidence/ReproducibleGameplayTests.md)**

The document contains:

- One victory scenario for each compulsory stage
- One defeat scenario for each compulsory stage
- Turn-by-turn staff actions
- Expected final scoreboards
- Links to recorded console outputs
- Instructions for reproducing the scenarios

Additional verification results are available in:

**[Release Verification Results](docs/test-evidence/ReleaseVerification.md)**

Supporting gameplay logs are stored under:

`docs/test-evidence/`

### 9.3 Testing Scope

The recorded tests verify the principal gameplay outcomes and mandatory full-game progression.

They do not replace all possible unit, integration and edge-case tests.

The optional enhanced release adds real-time timing challenges, which require interactive player input and are separate from the compulsory reproducible gameplay scenarios.

---

## 10. Video Demonstration and Timestamps

The group demonstration video will cover the required gameplay and design evidence for the three compulsory stages.

**Video link:** Pending recording/upload.

**Video duration:** Approximately 15 minutes.

Timestamps will be completed after the final recording.

| No. | Required Demonstration Segment | Timestamp |
|---|---|---|
| 1 | Launching the game from the submitted project | Pending |
| 2 | Meaningful manager choices, including staff constraints | Pending |
| 3 | An order progressing from READY to SERVED to payment | Pending |
| 4 | VIP or Critic customer behaviour | Pending |
| 5 | A compulsory Stage 3 feature | Pending |
| 6 | One victory and one defeat demonstration | Pending |
| 7 | Git tag comparisons and UML diagrams | Pending |

The recorded video will complement the source code, README, reproducible gameplay evidence and PDF report.

---

## 11. Submission Deliverables

The SC2002 group submission consists of:

- Java source code and runnable Git stage tags
- This README with repository information, execution instructions, stage comparisons and video timestamps
- PDF report containing UML diagrams, design discussions and functional evidence
- Recorded group demonstration video or accessible download link
- Signed Declaration of Original Work
- Individual declarations concerning the use of generative AI assistance

The three compulsory releases are:

- `stage-1`
- `stage-2`
- `stage-3`

The optional `stage-3-with-additional-features` release is provided as a separate enhancement.

The compulsory Stage 3 release remains independently accessible and runnable.
