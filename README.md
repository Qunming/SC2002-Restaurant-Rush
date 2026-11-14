
# SC2002 Restaurant Rush

**SC2002 — Object-Oriented Design & Programming**

**GitHub Repository:** https://github.com/Qunming/SC2002-Restaurant-Rush

Restaurant Rush is an offline, turn-based Java restaurant management game. The player acts as a restaurant manager, making decisions about seating, orders, food preparation, service and customer satisfaction while managing limited staff capacity.

The objective is to meet the required revenue, paid-customer and average satisfaction targets within 18 turns.

The project consists of **three compulsory, independently runnable stages** and one **optional enhanced release**, each preserved using its own Git tag.

The system was developed using object-oriented design and programming principles, with an emphasis on **encapsulation, inheritance, polymorphism, separation of responsibilities and extensibility**.

---

## 1. Releases and Git Tags

| Git Tag | Features | Submission Status |
|---|---|---|
| [`stage-1`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-1) | Basic restaurant operation with Regular customers, Waiter, Chef and Cashier | **Required** |
| [`stage-2`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-2) | Adds VIP and Critic customers using inheritance and polymorphism | **Required** |
| [`stage-3`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-3) | Adds Host, ComboMeal and Happy Hour | **Required** |
| [`stage-3-with-additional-features`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-3-with-additional-features) | Adds cooking timing challenges, cooking quality, customer feedback and tipping | **Optional enhanced release** |

Each release is independently runnable.

The compulsory `stage-3` release remains separate from the optional enhanced release. Additional features do not replace the compulsory Stage 3 implementation.

Original release snapshots are preserved under `archive/` tags following documentation-only tag updates.

---

## 2. System Requirements and Execution

### 2.1 Requirements

- Java Development Kit (JDK) 21 or later
- Git for cloning and switching between releases
- Windows PowerShell or Command Prompt
- No external Java libraries
- No internet connection required during gameplay

### 2.2 Clone the Repository

Open PowerShell:

```powershell
git clone https://github.com/Qunming/SC2002-Restaurant-Rush.git
cd SC2002-Restaurant-Rush
```

Verify Java installation:

```powershell
java -version
javac -version
```

### 2.3 Select a Release

Choose **one** of the following Git tags:

| Release | Command |
|---|---|
| Stage 1 | `git switch --detach stage-1` |
| Stage 2 | `git switch --detach stage-2` |
| Stage 3 | `git switch --detach stage-3` |
| Optional enhanced release | `git switch --detach stage-3-with-additional-features` |

For example, to run compulsory Stage 3:

```powershell
git switch --detach stage-3
```

A detached HEAD is expected when checking out a Git tag.

### 2.4 Compile and Run

Clear previously compiled classes before running a different release:

```powershell
if (Test-Path build) {
    Remove-Item build -Recurse -Force
}
```

Compile:

```powershell
javac --release 21 -encoding UTF-8 -sourcepath src -d build src/app/Main.java
```

Run:

```powershell
java -cp build app.Main
```

The game accepts numerical menu selections through the terminal.

The optional enhanced release also uses interactive ENTER key presses for cooking timing challenges.

### 2.5 Switching Between Releases

To run another stage:

1. Finish or exit the current game.
2. Switch to the required Git tag.
3. Delete the existing `build` directory.
4. Compile and run the selected release.

Return to the main branch using:

```powershell
git switch main
```

Save uncommitted changes before switching branches or tags.

Do not combine source files from different stage releases.

---

## 3. Base Game Configuration and Rules

### 3.1 Game Configuration

| Setting | Specification |
|---|---|
| Duration | 18 turns |
| Tables | 2 |
| Customer arrivals | Turns 1, 4, 7, 10, 13 and 16 |
| Initial satisfaction | 100 |
| Stage 1 and 2 staff | Waiter, Chef and Cashier |
| Stage 3 additional staff | Host |
| Required paid customers | At least 4 |
| Required food revenue | At least $45.00 |
| Required average paid satisfaction | At least 60 |

The game always completes 18 turns.

**Victory** occurs when all three conditions are satisfied after Turn 18:

1. At least four customers have paid.
2. Total food revenue reaches at least $45.00.
3. Average satisfaction among paid customers is at least 60.

**Defeat** occurs when any condition is not satisfied.

### 3.2 Customer Arrivals

One customer arrives at the beginning of turns:

`1, 4, 7, 10, 13, 16`

Customers enter the waiting queue with 100 satisfaction.

The player decides how to allocate limited staff actions to seating, order taking, preparation and service.

### 3.3 Staff Responsibilities

**Waiter**

The Waiter can perform one task per turn:

- Seat one waiting customer
- Take an order from a seated customer
- Serve an eligible READY order
- Wait

Viewing order history does not consume a staff action.

**Chef**

The Chef can perform one task per turn:

- Prepare one unit of an eligible order
- Wait

Multi-unit dishes require multiple preparation actions.

**Cashier**

The Cashier operates automatically:

- Processes at most one eligible payment per turn
- Selects the oldest eligible customer
- Records food revenue and paid-customer satisfaction
- Releases the customer's table after payment

The Cashier does not require player input.

### 3.4 Turn Sequence

**Stage 1 and Stage 2**

1. Scheduled customer arrival
2. Restaurant status display
3. Waiter action
4. Chef action
5. Eating progress and automatic payment
6. Satisfaction reduction and abandonment
7. End-of-turn summary

**Stage 3 and Optional Enhanced Release**

1. Scheduled customer arrival
2. Happy Hour activation decision and satisfaction recovery
3. Restaurant status display
4. Host action
5. Waiter action
6. Chef action
7. Eating progress and automatic payment
8. Satisfaction reduction and abandonment
9. End-of-turn summary

### 3.5 Order and Customer Lifecycle

Normal order lifecycle:

`PLACED → PREPARING → READY → SERVED → PAID`

For one-unit dishes, preparation may move an order directly from PLACED to READY.

Important timing rules:

- The Chef may begin preparing an order during the turn it is placed.
- An order cannot be served during the turn it first becomes READY.
- A served customer finishes eating on a later turn.
- Automatic payment occurs on a subsequent eligible turn.
- The Cashier processes at most one payment per turn.
- Tables remain occupied until payment or abandonment.

Customers awaiting service lose satisfaction at the end of each turn.

When satisfaction reaches zero, the customer abandons without paying.

Any unpaid, unserved order is cancelled, and an occupied table is released.

Invalid staff actions are rejected and may be retried without consuming the staff member's task.

---

## 4. Menu and Customer Behaviour

### 4.1 Menu Items

| Menu Item | Price | Preparation Units | Available From |
|---|---:|---:|---|
| Salad | $9.00 | 1 | Stage 1 |
| Burger | $15.00 | 1 | Stage 1 |
| Pasta | $18.00 | 2 | Stage 1 |
| Salad + Pasta Combo | $24.30 | 3 | Stage 3 |

Customers select their own food automatically.

Regular and VIP customers use deterministic menu selection:

```java
(customerId - 1) % menu.getItems().size()
```

Critic customers choose the most expensive available menu item.

### 4.2 Stage 1: Regular Customers

Regular customers:

- Start with 100 satisfaction
- Lose 8 satisfaction per unserved turn
- Receive no discount
- Use deterministic food selection

### 4.3 Stage 2: VIP and Critic Customers

Stage 2 introduces additional customer subclasses.

| Customer | Satisfaction Loss per Unserved Turn | Discount | Food Selection |
|---|---:|---:|---|
| Regular | 8 | 0% | Deterministic |
| VIP | 5 | 10% | Deterministic |
| Critic | 12 | 0% | Most expensive item |

The fixed customer arrival sequence from Stage 2 onwards is:

| Arrival Turn | Customer Type |
|---|---|
| 1 | Regular |
| 4 | VIP |
| 7 | Regular |
| 10 | Critic |
| 13 | Regular |
| 16 | Regular |

All customer types share the same customer lifecycle.

Different behaviours are implemented through overridden methods:

- `getCustomerType()`
- `getWaitingSatisfactionLoss()`
- `getDiscountPercent()`
- `chooseMenuItem()`

This enables the existing staff and order-management workflows to operate on different customer types through the common `Customer` superclass.

---

## 5. Required Stage 3 Features

Stage 3 introduces three compulsory features.

### 5.1 Host

The Host is an additional staff member who can seat one waiting customer per turn.

- Host and Waiter have independent action limits.
- The Waiter retains the ability to seat customers.
- Both use the existing `TableManager`.
- The restaurant still has two tables.

The Host reuses existing seating operations rather than introducing a separate table-management implementation.

### 5.2 ComboMeal

`ComboMeal` extends `MenuItem`.

A ComboMeal combines two menu items.

Its price equals 90% of the sum of the component prices, and its preparation units equal the sum of their preparation units.

For Salad + Pasta:

```text
Original price = $9.00 + $18.00 = $27.00
Combo price    = $27.00 × 0.90 = $24.30

Preparation units = 1 + 2 = 3
```

ComboMeal uses the existing order and food-preparation workflow.

The Chef does not require modification to prepare the new item.

### 5.3 Happy Hour

Happy Hour is a once-per-game promotion activated at the start of a turn.

It lasts for two consecutive turns:

- Activation turn
- Following turn

During each active turn:

1. Customers awaiting service recover 15 satisfaction, capped at 100.
2. Newly created orders receive a 20% Happy Hour discount.

Previously created orders retain their original locked prices.

VIP discounts are applied before Happy Hour discounts.

Example: VIP Burger during Happy Hour.

```text
Original price     = $15.00
After VIP discount = $15.00 × 0.90 = $13.50
After Happy Hour   = $13.50 × 0.80 = $10.80
```

Money is stored in integer cents to avoid floating-point rounding errors.

---

## 6. Optional Enhanced Release

The additional features are available in:

[`stage-3-with-additional-features`](https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-3-with-additional-features)

This version extends Stage 3 without changing the compulsory duration, win/lose conditions, satisfaction penalties, menu prices or food-revenue calculations.

### 6.1 Cooking Timing Challenge

The player completes a timing challenge when an order finishes its final preparation unit.

Press ENTER to start the timer and ENTER again to stop it.

| Dish | Target | PERFECT Range |
|---|---:|---|
| Salad | 2 s | 1.5–2.5 s |
| Burger | 3 s | 2.5–3.5 s |
| Pasta | 5 s | 4.5–5.5 s |
| Salad + Pasta Combo | 6 s | 5.5–6.5 s |

Timing within ±0.5 seconds gives `PERFECT` quality; otherwise, the result is `NORMAL`.

The challenge does not consume another Chef action or game turn.

### 6.2 Customer Feedback

Feedback is generated immediately when a customer pays or abandons the restaurant.

| Customer | PERFECT | NORMAL |
|---|---|---|
| Regular | GOOD | MEDIOCRE |
| VIP | GOOD | BAD |
| Critic | GOOD | BAD |

Abandoned customers always give BAD feedback.

Customer-specific comments use polymorphic methods in the corresponding customer classes.

Reviews are stored by `ReviewManager`, with duplicate reviews prevented.

The enhanced release also supports viewing review history.

### 6.3 Tipping

Paid PERFECT-quality orders earn tips based on customer type.

| Customer | PERFECT Tip | NORMAL Tip |
|---|---:|---:|
| Regular | $2.00 | $0.00 |
| VIP | $3.00 | $0.00 |
| Critic | $5.00 | $0.00 |

Tips are stored separately from food revenue.

They do not affect the mandatory victory conditions, customer satisfaction or menu prices.

Duplicate tip recording is prevented.

### 6.4 Additional Classes

| Feature | Principal Classes |
|---|---|
| Timing challenge | `CookingChallenge` |
| Cooking quality | `CookingQuality`, `Order` |
| Feedback | `FeedbackType`, `Review`, `ReviewManager` |
| Customer-specific reviews | `Customer`, `VIPCustomer`, `CriticCustomer` |
| Tips | `TipCalculator`, `GameState`, `Cashier` |
| Gameplay integration | `Main`, `GameController`, `ConsoleUI` |

---

## 7. Object-Oriented Design and SOLID Principles

The design separates game orchestration, customer behaviour, restaurant operations, order management and user interaction.

### 7.1 Class Responsibilities

| Responsibility | Principal Classes |
|---|---|
| Application initialization | `Main` |
| Turn progression and shared state | `GameController`, `GameState`, `GameConfig` |
| Customer behaviour and lifecycle | `Customer`, customer subclasses, `CustomerManager` |
| Seating and tables | `Table`, `TableManager` |
| Menu and dishes | `Menu`, `MenuItem`, `ComboMeal` |
| Orders and preparation | `Order`, `OrderManager` |
| Staff operations | `Waiter`, `Chef`, `Cashier`, `Host` |
| Console interface | `ConsoleUI`, `InputHandler` |
| Optional enhancements | `CookingChallenge`, `ReviewManager`, `TipCalculator` |

### 7.2 OOP Principles Applied

**Encapsulation**

Classes maintain their own state and provide methods that enforce valid changes.

Examples:

- `Customer` controls satisfaction, status and table assignment.
- `Order` controls preparation progress, status transitions and locked prices.
- `GameState` maintains game statistics and Happy Hour state.
- `TableManager` controls table allocation and release.

Other classes interact with these objects through public methods rather than directly modifying their private fields.

**Inheritance**

Stage 2 adds:

- `RegularCustomer extends Customer`
- `VIPCustomer extends Customer`
- `CriticCustomer extends Customer`

Stage 3 adds:

- `ComboMeal extends MenuItem`

The subclasses reuse common behaviour while introducing stage-specific requirements.

**Polymorphism**

The system uses overridden customer methods to implement different satisfaction penalties, discounts and food preferences.

For example, `CustomerManager` can call `decreaseWaitingSatisfaction()` on a `Customer` reference without checking whether that object is Regular, VIP or Critic.

The actual satisfaction penalty is determined by the object's overridden `getWaitingSatisfactionLoss()` method.

Similarly, `OrderManager` calls `chooseMenuItem()` through a `Customer` reference.

Regular and VIP customers follow deterministic selection, while Critic customers choose the most expensive item.

**Abstraction**

Common customer behaviour is represented by `Customer`, while common menu-item properties are represented by `MenuItem`.

These shared types allow staff and order-management classes to process different customer and dish types without implementing separate workflows for each subtype.

### 7.3 SOLID Principles Applied

| Principle | Application in Restaurant Rush | Assessment |
|---|---|---|
| **S — Single Responsibility** | `TableManager` manages seating; `OrderManager` manages orders; `Cashier` processes payment; `ConsoleUI` handles display | Applied through separation of responsibilities |
| **O — Open/Closed** | New customer subclasses extend `Customer`; `ComboMeal` extends `MenuItem`; existing Chef and seating behaviour is reused | Partially applied; integration changes were still required |
| **L — Liskov Substitution** | VIP and Critic customers can be used wherever the game expects a `Customer` | Supported by the shared customer lifecycle |
| **I — Interface Segregation** | The design uses focused class responsibilities but does not introduce separate role-specific interfaces | Not a major demonstrated feature |
| **D — Dependency Inversion** | `GameController` receives collaborators through constructor injection, but depends on concrete types rather than interfaces | Partially applied |

#### Single Responsibility Principle

The game is divided into classes with identifiable responsibilities.

For example, `Cashier` handles payments while `TableManager` handles seating.

A payment does not require the Cashier to implement table-allocation logic; it calls `TableManager` to release a table.

This reduces duplicated responsibilities across staff classes.

#### Open/Closed Principle

Stage 2 introduces new customer behaviour primarily through subclasses.

The existing Waiter and Chef can continue processing customer orders without knowing the concrete customer subclass.

Stage 3 demonstrates another extension point: `ComboMeal` is a subtype of `MenuItem`, so the existing Chef can prepare it using the same preparation-unit mechanism.

However, new stage features also required modifications to orchestration, state, pricing and interface classes. The design therefore demonstrates extensibility but is not completely closed to modification.

#### Liskov Substitution Principle

`RegularCustomer`, `VIPCustomer` and `CriticCustomer` preserve the shared customer lifecycle.

They can be supplied wherever a `Customer` object is expected, while overriding selected behaviour.

For example, the Waiter can seat or serve a VIP customer using the same methods used for a Regular customer.

#### Interface Segregation Principle

The application does not depend heavily on Java interfaces.

Instead, responsibilities are separated among focused classes such as `Chef`, `Cashier`, `TableManager` and `OrderManager`.

Interface segregation is therefore an area that could be improved in a larger future design, rather than a principle extensively demonstrated by the current implementation.

#### Dependency Inversion Principle

`GameController` receives staff members, game state and UI collaborators through its constructor instead of constructing every object internally.

This separates application setup in `Main` from turn orchestration.

However, the constructor still uses concrete class types. A future version could introduce interfaces for staff actions or boundary services to reduce concrete dependencies.

### 7.4 Design Trade-offs

The implementation prioritises a clear, independently runnable game for each compulsory stage.

Not every SOLID principle is applied fully.

In particular, `GameController` coordinates multiple gameplay responsibilities and requires changes when new staff actions or policies are introduced.

Further separation into dedicated turn phases or policy components could reduce future modifications.

The current design nevertheless demonstrates reuse through stable domain components, customer polymorphism and menu-item inheritance.

---

## 8. Change Maps and Design Extensibility

The change maps compare actual Java source snapshots in the compulsory Git tags.

**Counting method:**

- One Java class declaration is counted per class source file.
- Enum declarations are excluded from the class counts.
- Added classes exist only in the newer stage.
- Modified classes exist in both stages but have different source contents.
- Unchanged classes have identical source contents across the two tags.
- README and other documentation files are excluded.

This avoids relying on Git commit history alone, because some release histories diverged during development.

### 8.1 Class Change Map Summary

| Category | Stage 1 → Stage 2 | Stage 2 → Stage 3 |
|---|---:|---:|
| Classes in previous stage | 18 | 21 |
| **Classes added** | **3** | **2** |
| **Existing classes modified** | **10** | **12** |
| **Existing classes unchanged** | **8** | **9** |
| Classes deleted | 0 | 0 |
| **Total classes in new stage** | **21** | **23** |

These figures count Java classes only. Each compulsory stage also contains two unchanged enum declarations: `CustomerStatus` and `OrderStatus`.

The figures are based on the published `stage-1`, `stage-2` and `stage-3` source snapshots.

### 8.2 Stage 1 → Stage 2: Customer Behaviour Extension

**GitHub comparison:**

[Compare Stage 1 and Stage 2](https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-1...stage-2)

#### Added Classes (3)

| Class | Purpose |
|---|---|
| `RegularCustomer` | Represents Regular customers explicitly within the new customer hierarchy |
| `VIPCustomer` | Adds reduced waiting penalties and a 10% discount |
| `CriticCustomer` | Adds increased waiting penalties and preference for the most expensive food |

#### Modified Existing Classes (10)

| Class | Reason for Modification |
|---|---|
| `GameController` | Creates the required mixture of customer subclasses |
| `GameState` | Refines game-state and payment statistics handling |
| `Customer` | Adds overridable methods for customer-specific behaviour |
| `CustomerManager` | Supports multiple customer subclasses |
| `Menu` | Refactors menu storage and access |
| `MenuItem` | Refines item representation and validation |
| `Order` | Supports customer-specific discounted prices |
| `OrderManager` | Refines order creation and validation |
| `Cashier` | Strengthens payment and state validation |
| `ConsoleUI` | Displays customer types and discounted bills |

#### Existing Classes Remaining Unchanged (8)

| Class | Reuse Explanation |
|---|---|
| `Main` | Existing application setup remains usable |
| `GameConfig` | Base game configuration remains unchanged |
| `Table` | Table occupancy works for every customer subtype |
| `TableManager` | Seating logic accepts all customer subtypes |
| `Chef` | Preparation logic is independent of customer type |
| `Waiter` | Existing staff operations work with the common `Customer` type |
| `AnsiColors` | Terminal colour handling remains reusable |
| `InputHandler` | Existing input handling remains reusable |

**Classes deleted:** 0.

#### Design Justification

Stage 2 extends customer behaviour primarily through inheritance and polymorphism.

Instead of introducing separate Waiter, Chef and table-management implementations for VIP and Critic customers, the new subclasses reuse the existing customer lifecycle.

The Waiter and TableManager continue operating on `Customer` references.

Customer-specific behaviour is provided through overridden methods, avoiding separate service workflows for each customer type.

The transition required changes to 10 existing classes, including the core customer and order models. However, eight original classes remained unchanged.

This demonstrates reuse of existing restaurant operations while accommodating new customer requirements.

### 8.3 Stage 2 → Stage 3: Host, ComboMeal and Happy Hour

**GitHub comparison:**

[Compare Stage 2 and Stage 3](https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-2...stage-3)

#### Added Classes (2)

| Class | Purpose |
|---|---|
| `Host` | Adds an independent staff member for seating customers |
| `ComboMeal` | Extends `MenuItem` to combine existing dishes |

#### Modified Existing Classes (12)

| Class | Reason for Modification |
|---|---|
| `Main` | Instantiates and connects the Host |
| `GameConfig` | Defines Happy Hour duration |
| `GameController` | Coordinates Host actions and Happy Hour |
| `GameState` | Tracks Happy Hour activation and duration |
| `Customer` | Supports satisfaction recovery |
| `CustomerManager` | Applies recovery to eligible customers |
| `Menu` | Registers the ComboMeal |
| `Order` | Calculates and locks Happy Hour-discounted prices |
| `OrderManager` | Accepts Happy Hour state during order creation |
| `Waiter` | Passes promotion state when taking orders |
| `ConsoleUI` | Displays Host and Happy Hour information |
| `InputHandler` | Handles new player selections |

#### Existing Classes Remaining Unchanged (9)

| Class | Reuse Explanation |
|---|---|
| `RegularCustomer` | Existing Regular behaviour works with the expanded menu |
| `VIPCustomer` | Existing VIP discount and behaviour remain reusable |
| `CriticCustomer` | Automatically selects the new most expensive menu item |
| `MenuItem` | Existing item representation supports ComboMeal inheritance |
| `Table` | Table occupancy rules are unchanged |
| `TableManager` | Existing seating logic is reused by the Host |
| `Cashier` | Existing automatic payment logic handles discounted orders |
| `Chef` | Existing preparation-unit logic handles three-unit ComboMeals |
| `AnsiColors` | Existing colour utility remains reusable |

**Classes deleted:** 0.

#### Design Justification

Stage 3 introduces new staff functionality, a new menu-item type and a promotion policy while reusing important existing components.

**Example 1 — Host reuses TableManager**

`Host` uses the same `TableManager.seatCustomer()` operation as the Waiter.

No separate seating system is needed, and `TableManager` remains unchanged.

**Example 2 — ComboMeal reuses Chef**

`ComboMeal` extends `MenuItem` and provides its own price and preparation-unit values.

The Chef already prepares orders one unit at a time.

Therefore, a three-unit ComboMeal can be prepared using the existing Chef implementation without modifying `Chef.java`.

**Example 3 — CriticCustomer automatically supports ComboMeal**

The Critic chooses the most expensive available item through `chooseMenuItem()`.

When ComboMeal is registered in `Menu`, the Critic can select it without changing `CriticCustomer.java`.

**Example 4 — Cashier reuses locked order prices**

The final discounted price is calculated and stored when an order is created.

The existing Cashier processes that locked price without needing separate payment logic for Happy Hour.

These examples demonstrate how common abstractions and clearly defined class responsibilities support extensions.

The transition still modifies 12 existing classes, mainly to integrate Host actions and Happy Hour into turn control, state management, pricing and the user interface.

The strongest evidence of extensibility is therefore the reuse of important unchanged domain classes, rather than a claim that all new requirements were implemented without modifying existing code.

### 8.4 Overall Extensibility Analysis

| Design Area | Stage 2 Extension | Stage 3 Extension |
|---|---|---|
| Customer behaviour | New subclasses reuse `Customer` | Existing customer subclasses remain usable |
| Staff workflow | Waiter and Chef unchanged | Host added; Chef and Cashier unchanged |
| Table management | Unchanged | Unchanged; reused by Host |
| Menu model | Existing menu model refined | ComboMeal extends unchanged `MenuItem` |
| Order handling | Discount support added | Promotion pricing integrated |
| Turn control | Updated for customer arrivals | Updated for Host and Happy Hour |

The system demonstrates extensibility in several areas:

- New customer types reuse the existing service workflow.
- New menu items reuse the existing preparation workflow.
- New staff members reuse shared restaurant-management components.
- Existing payment and seating logic support Stage 3 features without modification.

However, introducing new turn-level features still requires changes to `GameController`, `GameState` and other integration classes.

This identifies an opportunity for further modularisation in future development.

The design therefore demonstrates **practical reuse and partial adherence to Open/Closed Principle**, rather than complete elimination of modifications to existing code.

### 8.5 Stage 3 → Optional Enhanced Release

**GitHub comparison:**

[Compare Stage 3 and Additional Features](https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-3...stage-3-with-additional-features)

The additional release is separate from the compulsory stages.

**New Java source files:**

| File | Purpose |
|---|---|
| `CookingChallenge.java` | Interactive cooking timing |
| `FeedbackType.java` | Feedback enum |
| `Review.java` | Customer review data |
| `ReviewManager.java` | Review storage and duplicate prevention |
| `CookingQuality.java` | Cooking-quality enum |
| `TipCalculator.java` | Tip calculations |

**Modified Java source files:**

| File | Purpose |
|---|---|
| `Main.java` | Creates CookingChallenge with a shared Scanner |
| `GameController.java` | Integrates cooking challenges and reviews |
| `GameState.java` | Tracks reviews and tips |
| `Customer.java` | Adds baseline feedback behaviour |
| `VIPCustomer.java` | Adds VIP-specific feedback |
| `CriticCustomer.java` | Adds Critic-specific feedback |
| `Order.java` | Stores cooking quality |
| `Cashier.java` | Records tips after payment |
| `ConsoleUI.java` | Displays additional gameplay information |

**Deleted Java source files:** None.

This additional release demonstrates further feature extension without changing the compulsory game duration or victory thresholds.

---

## 9. Reproducible Gameplay Tests

The three compulsory stages were independently compiled and tested against their released implementations.

The recorded test scenarios cover full 18-turn gameplay, including victory, defeat, satisfaction, abandonment, revenue and payments.

### 9.1 Verified Outcomes

| Release | Scenario | Paid Customers | Food Revenue | Avg Satisfaction | Final Result |
|---|---|---:|---:|---:|---|
| Stage 1 | Victory | 4 | $51.00 | 78.0 | VICTORY |
| Stage 1 | Queue abandonment | 0 | $0.00 | N/A | DEFEAT |
| Stage 2 | Victory | 4 | $58.50 | 75.5 | VICTORY |
| Stage 2 | Queue abandonment | 0 | $0.00 | N/A | DEFEAT |
| Stage 3 | Victory with Happy Hour | 5 | $68.94 | 86.2 | VICTORY |
| Stage 3 | No-service scenario | 0 | $0.00 | N/A | DEFEAT |

Defeat is the expected result in the designated defeat scenarios.

Stages 1 and 2 also passed separate table-abandonment scenarios.

All recorded gameplay scenarios completed 18 turns.

### 9.2 Reproducible Action Tables

**[Reproducible Gameplay Tests — Full 18-Turn Action Tables](docs/test-evidence/ReproducibleGameplayTests.md)**

The document includes:

- One victory scenario per compulsory stage
- One defeat scenario per compulsory stage
- Turn-by-turn staff choices
- Verified final results
- Links to recorded console output
- Reproduction instructions

Additional verification:

**[Release Verification Results](docs/test-evidence/ReleaseVerification.md)**

Supporting logs are stored in:

`docs/test-evidence/`

### 9.3 Test Scope and Limitations

These tests verify the main game lifecycle and recorded victory/defeat scenarios.

They do not constitute exhaustive testing of every possible input combination or edge case.

The optional enhanced release includes real-time cooking challenges requiring interactive player input and is tested separately from the compulsory gameplay scenarios.

---

## 10. Video Demonstration and Timestamps

The demonstration will focus on game operation, object-oriented design, SOLID principles and changes between the three compulsory stages.

**Video link:** Pending recording/upload.

**Video duration:** To be finalised.

Timestamps will be added after recording.

| No. | Demonstration Segment | Timestamp |
|---|---|---|
| 1 | Launching the game | Pending |
| 2 | Meaningful manager decisions and staff constraints | Pending |
| 3 | READY → SERVED → payment lifecycle | Pending |
| 4 | VIP or Critic customer behaviour | Pending |
| 5 | Compulsory Stage 3 features | Pending |
| 6 | Victory and defeat demonstrations | Pending |
| 7 | OOP/SOLID design, UML diagrams and stage change maps | Pending |

The design explanation will emphasise:

- Why particular classes were introduced or modified
- Which classes remained unchanged between stages
- How inheritance and polymorphism support customer extensions
- How existing classes such as `Chef`, `Cashier` and `TableManager` were reused
- How SOLID principles influenced design decisions and trade-offs

The video complements the source code, change maps, reproducible gameplay evidence and PDF report.

---

## 11. Submission Deliverables

The SC2002 submission includes:

- Java source code and independently runnable Git tags
- README with compilation instructions, stage comparisons, change maps and design explanations
- PDF report containing UML diagrams and design discussion
- Group demonstration video or accessible download link
- Signed Declaration of Original Work
- Individual declarations concerning generative AI assistance

**Compulsory releases:**

- `stage-1`
- `stage-2`
- `stage-3`

**Optional enhanced release:**

- `stage-3-with-additional-features`

The optional release is provided separately and does not replace compulsory Stage 3.

The tagged implementations and their documentation are available through the links in Section 1.
