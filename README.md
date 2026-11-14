
# SC2002 Restaurant Rush — Stage 2

A turn-based restaurant management game developed in Java using object-oriented programming (OOP).

The player acts as a restaurant manager, deciding which customers to serve and which orders to prepare while managing limited staff resources.

Stage 2 extends the original Stage 1 implementation by introducing different customer types with unique behaviours through inheritance and polymorphism.

## 1. Repository Information

Repository:
https://github.com/Qunming/SC2002-Restaurant-Rush

Release reference:
`stage-2`

Completed releases:
- Stage 1: Basic Restaurant Operation (`stage-1`)
- Stage 2: Customer Behaviour Extension (`stage-2`, released)
- Stage 3: New Role and Menu Policy (separate release, `stage-3`; not present in this tag)

Each stage builds upon the previous implementation while preserving existing functionality.

---

## 2. System Requirements

- Java 21 or later
- Windows Command Prompt or PowerShell
- No external Java libraries required
- No internet connection required during gameplay

### Compile and Run

Clone the repository:

```bash
git clone https://github.com/Qunming/SC2002-Restaurant-Rush.git
```

Navigate into the project:

```bash
cd SC2002-Restaurant-Rush
```

Switch to Stage 2:

```bash
git switch --detach stage-2
```

### Option 1: Compile Using the Existing Script

```cmd
.\compile_stage1.cmd
java -cp build app.Main
```

The existing compilation script also works for Stage 2.

### Option 2: Compile Manually

```cmd
javac -encoding UTF-8 -sourcepath src -d build src\app\Main.java
java -cp build app.Main
```

The compiled Java files are stored in the `build` directory and excluded from Git.

To disable terminal colours:

```cmd
java -DnoColor=true -cp build app.Main
```

---

## 3. Game Overview

The game consists of 18 turns.

The player must manage customer satisfaction, restaurant revenue, seating arrangements, and staff workload.

### Base Game Configuration

| Setting | Value |
|---|---|
| Maximum turns | 18 |
| Tables | 2 |
| Waiters | 1 |
| Chefs | 1 |
| Cashiers | 1 |
| Total customer arrivals | 6 |
| Arrival turns | 1, 4, 7, 10, 13, 16 |

Each customer begins with 100 satisfaction points.

Customers who have not been served lose satisfaction at the end of each turn.

If satisfaction reaches zero, the customer abandons the restaurant without paying.

### Victory Conditions

The player wins only if all three conditions are satisfied at the end of Turn 18:

1. At least 4 customers have paid.
2. Total revenue is at least $45.00.
3. Average satisfaction of paid customers is at least 60.

Otherwise, the game ends in defeat.

The game always continues through all 18 turns.

---

## 4. Menu

The restaurant offers three food items.

| Menu Item | Price | Preparation Units |
|---|---|---|
| Salad | $9.00 | 1 |
| Burger | $15.00 | 1 |
| Pasta | $18.00 | 2 |

Customers choose their own food automatically.

The restaurant manager does not select the food.

Regular and VIP customers use a deterministic selection rule based on their customer ID.

The menu order is Salad, Burger, Pasta, repeating according to the customer ID.

Critic customers use a different ordering preference, described below.

---

## 5. Stage 2: Customer Behaviour Extension

Stage 2 introduces inheritance and polymorphism to support multiple customer types without rewriting the existing game logic.

Three customer types are available:

- RegularCustomer
- VIPCustomer
- CriticCustomer

All customer types inherit from the existing `Customer` class.

### Customer Behaviour Comparison

| Customer Type | Satisfaction Loss | Discount | Special Behaviour |
|---|---|---|---|
| Regular | 8 per turn | None | Standard food selection |
| VIP | 5 per turn | 10% | Standard food selection |
| Critic | 12 per turn | None | Selects the most expensive dish |

### Regular Customer

Regular customers retain the original Stage 1 behaviour.

- Start with 100 satisfaction.
- Lose 8 satisfaction per unserved turn.
- Pay the full menu price.
- Choose food according to the deterministic ID-based rule.

### VIP Customer

VIP customers are more patient and receive discounted meals.

- Start with 100 satisfaction.
- Lose 5 satisfaction per unserved turn.
- Receive a 10% discount on their order.
- Follow the same seating, ordering, cooking, serving and payment process.

Example:

A VIP customer ordering a Burger pays:

```text
Original price: $15.00
VIP discount:   10%
Final bill:     $13.50
```

The discounted price is calculated and locked when the order is created.

### Critic Customer

Critic customers are less patient and have a distinct food preference.

- Start with 100 satisfaction.
- Lose 12 satisfaction per unserved turn.
- Pay the full menu price.
- Automatically select the most expensive available menu item.

With the current menu, Critic customers choose Pasta ($18.00).

This preference is implemented by overriding `chooseMenuItem()` in `CriticCustomer`.

### Stage 2 Customer Arrivals

The game uses a fixed customer-type sequence to make testing reproducible.

| Turn | Customer ID | Customer Type |
|---|---|---|
| 1 | 1 | Regular |
| 4 | 2 | VIP |
| 7 | 3 | Regular |
| 10 | 4 | Critic |
| 13 | 5 | Regular |
| 16 | 6 | Regular |

The total number of arrivals remains six.

This ensures every game includes at least one VIP and one Critic customer.

---

## 6. Game Turn Sequence

Each turn follows the original Stage 1 sequence.

### Step 1: Customer Arrival

A new customer arrives on a scheduled turn.

The customer joins the waiting queue with 100 satisfaction.

### Step 2: Waiter Action

The Waiter may perform at most one action:

1. Seat a waiting customer.
2. Take an order from a seated customer.
3. Serve an order that is ready.
4. Wait.

The player may also view order history without consuming a staff action.

### Step 3: Chef Action

The Chef may:

1. Prepare one unit of an eligible order.
2. Wait.

The Chef may prepare an order taken by the Waiter during the same turn.

An order becomes READY once all required preparation units are completed.

An order that becomes READY during the current turn cannot be served until a later turn.

### Step 4: Eating and Payment

A customer served on an earlier turn becomes READY_TO_PAY after completing an eating turn.

The Cashier automatically processes at most one eligible payment per turn.

Payment is collected no earlier than the turn after the customer becomes READY_TO_PAY.

Once payment is completed:

- The customer is marked PAID.
- The order is marked PAID.
- The final order price is added to revenue.
- Customer satisfaction is recorded.
- The occupied table becomes available.

### Step 5: Satisfaction and Abandonment

Unserved customers lose satisfaction according to their customer type.

```text
Regular: -8
VIP:     -5
Critic:  -12
```

Satisfaction cannot fall below zero.

Customers reaching zero satisfaction abandon the restaurant.

Any unfinished order is cancelled and the table is released.

Served customers do not lose further satisfaction.

### Step 6: Turn Results

The game displays turn results and progresses to the next turn.

After Turn 18, the system evaluates the victory conditions.

---

## 7. Object-Oriented Design

Stage 2 demonstrates inheritance, polymorphism, encapsulation and separation of responsibilities.

### Inheritance

The three customer types share common attributes and behaviours through the `Customer` superclass.

```text
              Customer
                 |
       ---------------------
       |         |         |
    Regular     VIP      Critic
    Customer  Customer   Customer
```

Common attributes include:

- Customer ID
- Satisfaction
- Customer status
- Assigned table
- Customer order
- Serving and payment turn information

This avoids duplicating customer lifecycle logic.

### Polymorphism

Different customer behaviours are implemented through method overriding.

Important methods include:

```java
getCustomerType()
getWaitingSatisfactionLoss()
getDiscountPercent()
chooseMenuItem(Menu menu)
```

For example, `CustomerManager` calls:

```java
customer.decreaseWaitingSatisfaction();
```

The customer object determines the appropriate satisfaction reduction.

Similarly, `Order` calls:

```java
customer.getDiscountPercent();
```

The correct discount is determined by the actual customer type.

This allows the existing managers and staff classes to work with different customer types through the shared `Customer` interface provided by the superclass.

### Encapsulation

Customer attributes are private and accessed through defined methods.

Customer state changes are controlled through operations such as:

```java
seatAt()
assignOrder()
markServed()
markReadyToPay()
markPaid()
abandon()
```

These methods validate the customer's current state before allowing a transition.

### Separation of Responsibilities

- Customer classes manage customer behaviour and state.
- CustomerManager manages customer collections and lifecycle updates.
- TableManager manages seating and table releases.
- OrderManager creates and tracks orders.
- Staff classes execute restaurant operations.
- GameController coordinates turns.
- ConsoleUI handles display.
- InputHandler collects user input.

Game rules are kept separate from the console interface.

---

## 8. Stage 1 to Stage 2 Changes

Stage 2 extends the existing Stage 1 implementation without replacing the original restaurant workflow.

### Newly Added Files

| File | Purpose |
|---|---|
| `src/customer/RegularCustomer.java` | Explicit Regular customer subclass |
| `src/customer/VIPCustomer.java` | VIP satisfaction and discount behaviour |
| `src/customer/CriticCustomer.java` | Critic satisfaction and order preference |

### Modified Files

| File | Changes |
|---|---|
| `Customer.java` | Added overridable customer behaviour methods and polymorphic satisfaction reduction |
| `CustomerManager.java` | Supports different customer subclasses through the common Customer type |
| `GameController.java` | Creates fixed VIP and Critic arrivals and displays final bills |
| `ConsoleUI.java` | Displays customer types and order prices |
| `Order.java` | Applies customer-specific discounts and locks the final bill |
| `OrderManager.java` | Added order-selection validation and retained polymorphic food selection |
| `Menu.java` | Minor internal refactoring while preserving the original three items |
| `MenuItem.java` | Refined validation, formatting and string representation |
| `GameState.java` | Refined payment validation and code readability |
| `Cashier.java` | Strengthened payment validation while preserving the original payment workflow |
| `README.md` | Describes the Stage 2 customer behaviour extension and its release evidence |

### Unchanged Core Functionality

The following Stage 1 features remain:

- 18-turn game duration
- Two tables
- Six customer arrivals
- Waiter and Chef action limitations
- Automatic Cashier payment
- Order preparation timing
- Customer eating and payment timing
- Satisfaction and abandonment
- Victory and defeat conditions

No existing source files were deleted as part of the Stage 2 extension.

### Design Rationale

The main design decision was to introduce customer subclasses instead of placing customer-type conditions throughout the game controller.

This allows customer-specific behaviour to remain inside the relevant customer objects.

The original customer lifecycle and staff workflow can therefore be reused.

### GitHub Evidence

Stage 1 release:
https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-1

Stage 2 release:
https://github.com/Qunming/SC2002-Restaurant-Rush/tree/stage-2

Compare Stage 1 and Stage 2:
https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-1...stage-2

The `stage-2` release is tagged and has been independently compiled and tested. The comparison is between the published stage tags.


## 9. Known Limitations

- The game uses a command-line interface rather than a graphical interface.
- Customer arrivals follow a fixed schedule.
- Customer types are assigned deterministically.
- Customer food choices are automated.
- Only one Waiter, Chef and Cashier are available.
- The base game contains two tables and three menu items.
- Stage 3 features are not included in this release.

These constraints preserve the assignment's reproducible base setting.


The final submission will also require updated UML diagrams, a design report, gameplay evidence, video timestamps and declaration forms.

The final submission README on `main` will receive video timestamps after the demonstration is recorded.

Stage 2 has been independently compiled and tested against the published release tag.

## 10. Release verification and reproducibility

**Released tag:** `stage-2` (this is not the historical `stage-2-dev` branch). The compiled release contains Stage 1 functionality plus polymorphic `RegularCustomer`, `VIPCustomer` and `CriticCustomer` implementations; it intentionally excludes Stage 3 features.

| Scenario | Paid | Food revenue | Average paid satisfaction | Outcome |
|---|---:|---:|---:|---|
| Full 18-turn victory | 4 | $58.50 | 75.5 | VICTORY |
| Full 18-turn no-service queue abandonment | 0 | $0.00 | N/A | DEFEAT |
| Seated abandonment regression | 0 | $0.00 | N/A | DEFEAT |

**18-turn action tables and recorded results:** https://github.com/Qunming/SC2002-Restaurant-Rush/blob/main/docs/test-evidence/ReproducibleGameplayTests.md

### Clean rebuild in Windows PowerShell

```powershell
git switch --detach stage-2
if (Test-Path build) { Remove-Item build -Recurse -Force }
javac --release 21 -encoding UTF-8 -sourcepath src -d build src/app/Main.java
java -cp build app.Main
```

**Stage 1 -> Stage 2 compare:** https://github.com/Qunming/SC2002-Restaurant-Rush/compare/stage-1...stage-2

The file table in Section 8 describes new and modified classes. No original Stage 1 Java source files were deleted in this transition. The final submission README on `main` also provides the complete transition comparison, including README changes.
