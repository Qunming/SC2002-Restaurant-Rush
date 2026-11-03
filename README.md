# SC2002 Restaurant Rush — Stage 3

A command-line, turn-based restaurant management game developed in Java for the SC2002 Object-Oriented Design & Programming group assignment.

The player acts as a restaurant manager, making decisions about seating, order handling, food preparation, customer satisfaction and revenue.

Stage 3 extends the existing Stage 2 implementation by introducing three features: **Host**, **ComboMeal**, and **Happy Hour**. All core Stage 1 and Stage 2 gameplay rules are retained.

## 1. Repository Information

**GitHub Repository:**  
https://github.com/Qunming/SC2002-Restaurant-Rush

**Development Branch:** `main`

### Stage Releases

| Stage | Features | Git Reference | Status |
|---|---|---|---|
| Stage 1 | Basic Restaurant Operation | `stage-1` | Tagged |
| Stage 2 | Customer Behaviour Extension | `stage-2` | Tagged |
| Stage 3 | Host, ComboMeal and Happy Hour | `stage-3` | Tagged |

Each stage builds upon the previous implementation while preserving its existing functionality.

Each tagged stage is intended to compile and run independently.

## 2. System Requirements

- Java Development Kit (JDK) 21 or later
- Windows PowerShell or Command Prompt
- No external Java libraries required
- No internet connection required during gameplay

### Clone the Repository

```powershell
git clone https://github.com/Qunming/SC2002-Restaurant-Rush.git
cd SC2002-Restaurant-Rush
```

If the repository is already cloned, open the existing project folder.

### Compile and Run Stage 3

Switch to the main branch:

```powershell
git switch main
```

Compile and run:

```powershell
javac -encoding UTF-8 -sourcepath src -d build src\app\Main.java
java -cp build app.Main
```

Compiled `.class` files are generated inside the `build` directory.

### Run an Earlier Stage

Stage 1 and Stage 2 can be accessed using their respective Git tags.

For example, to run Stage 2:

```powershell
javac -encoding UTF-8 -sourcepath src -d build src\app\Main.java
java -cp build app.Main
```

Return to the main branch:

```powershell
git switch main
```

Local changes should be committed or saved before switching Git references.

## 3. Game Overview

Restaurant Rush is a turn-based restaurant management simulation consisting of **18 turns**.

The player must manage customer satisfaction, restaurant revenue, seating arrangements and staff workload.

### Base Game Configuration

| Setting | Value |
|---|---|
| Maximum turns | 18 |
| Tables | 2 |
| Waiters | 1 |
| Hosts | 1 (Stage 3) |
| Chefs | 1 |
| Cashiers | 1 |
| Total customer arrivals | 6 |
| Arrival turns | 1, 4, 7, 10, 13, 16 |
| Starting satisfaction | 100 |

Customers who are still waiting for a table or food lose satisfaction at the end of each turn.

When satisfaction reaches zero, the customer abandons the restaurant without paying.

### Victory Conditions

The player wins only if all three conditions are satisfied at the end of Turn 18:

1. At least **4 customers** have paid.
2. Total revenue is at least **$45.00**.
3. Average satisfaction of paid customers is at least **60**.

Otherwise, the game ends in defeat.

The game continues through all 18 turns.

## 4. Menu

Stage 3 expands the original menu by introducing a ComboMeal.

| Menu Item | Price | Preparation Units |
|---|---:|---:|
| Salad | $9.00 | 1 |
| Burger | $15.00 | 1 |
| Pasta | $18.00 | 2 |
| Salad + Pasta Combo | $24.30 | 3 |

### ComboMeal

The `ComboMeal` class combines two existing menu items.

Its rules are:

- The combined preparation units equal the sum of both items.
- The combo price equals 90% of the combined prices, rounded to the nearest cent.

Example:

```text
Salad:                $9.00
Pasta:               $18.00
Combined price:      $27.00
Combo discount:         10%
Final combo price:   $24.30

Salad preparation:       1
Pasta preparation:       2
Total preparation:       3
```

`ComboMeal` extends `MenuItem`, allowing it to reuse the existing order, preparation, serving and payment workflow.

### Customer Food Selection

Customers choose their food automatically. The restaurant manager does not select the dishes.

Regular and VIP customers use the deterministic selection rule:

```java
(customerId - 1) % menu.getItems().size()
```

The Stage 3 menu order is:

1. Salad
2. Burger
3. Pasta
4. Salad + Pasta Combo

Critic customers select the most expensive available menu item.

In Stage 3, the Critic selects the Salad + Pasta Combo ($24.30).

This deterministic selection behaviour is a documented team design decision.

## 5. Customer Behaviour

Stage 3 preserves the customer types introduced in Stage 2.

The three customer types are:

- `RegularCustomer`
- `VIPCustomer`
- `CriticCustomer`

All three inherit from the `Customer` superclass.

### Customer Behaviour Comparison

| Customer Type | Satisfaction Loss Per Unserved Turn | Discount | Food Preference |
|---|---:|---:|---|
| Regular | 8 | None | Deterministic selection |
| VIP | 5 | 10% | Deterministic selection |
| Critic | 12 | None | Most expensive menu item |

### Regular Customer

Regular customers retain the original Stage 1 behaviour.

They begin with 100 satisfaction, lose 8 satisfaction per unserved turn and pay the normal menu price unless Happy Hour is active when the order is placed.

### VIP Customer

VIP customers lose 5 satisfaction per unserved turn and receive a 10% discount.

Example:

```text
Burger price:       $15.00
VIP discount:          10%
Final bill:         $13.50
```

During Happy Hour, an additional 20% discount is applied after the VIP discount.

### Critic Customer

Critic customers lose 12 satisfaction per unserved turn.

They automatically select the most expensive menu item.

With Stage 3's menu, Critic customers choose the Salad + Pasta Combo.

### Customer Arrivals

The game uses a fixed customer sequence to ensure reproducible gameplay.

| Turn | Customer ID | Customer Type |
|---|---:|---|
| 1 | 1 | Regular |
| 4 | 2 | VIP |
| 7 | 3 | Regular |
| 10 | 4 | Critic |
| 13 | 5 | Regular |
| 16 | 6 | Regular |

All customer types share the same seating, ordering, preparation, serving and payment workflow.

## 6. Stage 3 Features

### 6.1 Host

Stage 3 introduces a new staff member called the Host.

The Host may seat at most one waiting customer per turn, independently of the Waiter.

The Waiter retains all existing actions.

Example of actions during the same turn:

1. The Host seats Customer #1.
2. The Waiter takes Customer #1's order.
3. The Chef prepares one unit of the order.

The Host uses the existing `TableManager` to assign tables.

This prevents multiple customers from occupying the same table.

The Host cannot seat a customer when all tables are occupied or when the Host has already acted during the turn.

### 6.2 ComboMeal

`ComboMeal` extends the existing `MenuItem` class.

It stores references to two menu items and calculates their combined price and preparation units.

The implemented combo contains:

- Salad
- Pasta

The Chef still prepares one unit per turn.

The Salad + Pasta Combo requires three preparation actions.

A ComboMeal uses the same order statuses and payment workflow as other menu items.

### 6.3 Happy Hour

Happy Hour is a temporary promotional policy controlled by the player.

The player may activate Happy Hour once per game at the start of a turn.

After activation, Happy Hour remains active during:

- The activation turn
- The following turn

It cannot be activated again later.

#### Happy Hour Benefits

While Happy Hour is active:

- Newly placed orders receive an additional 20% discount.
- Eligible waiting customers recover 15 satisfaction at the beginning of each active turn.
- Satisfaction cannot exceed 100.
- The remaining Happy Hour duration is displayed.

Satisfaction recovery applies only to customers with these statuses:

- `WAITING`
- `SEATED`
- `ORDERED`

Customers already served, ready to pay, paid or abandoned do not receive satisfaction recovery.

#### Discount Calculation

When multiple discounts apply, the calculation follows this order:

1. ComboMeal discount is already included in the combo price.
2. VIP discount is applied.
3. Happy Hour discount is applied.

Example: VIP ordering Burger during Happy Hour.

```text
Burger price:           $15.00
VIP discount (10%):     $13.50
Happy Hour (20%):       $10.80

Final bill:             $10.80
```

Example: VIP ordering ComboMeal during Happy Hour.

```text
ComboMeal price:        $24.30
VIP discount (10%):     $21.87
Happy Hour (20%):       $17.50

Final bill:             $17.50
```

Prices are stored as integer cents and rounded after each discount calculation.

The final price is fixed when an order is created.

Orders placed before Happy Hour do not receive a retroactive discount.

## 7. Game Turn Sequence

Stage 3 preserves the original game workflow while introducing Happy Hour and Host actions.

### Step 1: Customer Arrival

A new customer arrives on a scheduled turn.

The customer joins the waiting queue with 100 satisfaction.

### Step 2: Happy Hour Activation

If Happy Hour has not been used, the player may activate it or save it for a later turn.

If Happy Hour is active, eligible customers recover 15 satisfaction.

### Step 3: Restaurant Status

The game displays the current restaurant statistics, available tables, waiting customers, orders and Happy Hour status.

### Step 4: Host Action

The Host may:

1. Seat one waiting customer.
2. Wait.

The Host performs at most one task per turn.

### Step 5: Waiter Action

The Waiter may:

1. Seat a waiting customer.
2. Take an order from a seated customer.
3. Serve a ready order.
4. Wait.

The Waiter performs at most one task per turn.

Viewing order history does not consume the Waiter's action.

### Step 6: Chef Action

The Chef may:

1. Prepare one unit of an eligible order.
2. Wait.

The Chef may prepare an order placed earlier during the same turn.

An order becomes `READY` after all required preparation units are completed.

An order cannot be served on the same turn that it becomes `READY`.

### Step 7: Eating and Payment

Customers served on an earlier turn may become `READY_TO_PAY`.

The Cashier automatically processes at most one eligible payment per turn.

The Cashier prioritizes the oldest eligible customer.

Customers cannot pay during the same turn they become `READY_TO_PAY`.

After successful payment:

- The customer becomes `PAID`.
- The order becomes `PAID`.
- The locked final price is added to revenue.
- Customer satisfaction is recorded.
- The occupied table becomes available.

### Step 8: Satisfaction and Abandonment

Customers still waiting for a table or food lose satisfaction according to their customer type.

```text
Regular: -8
VIP:     -5
Critic:  -12
```

Satisfaction cannot fall below zero.

A customer whose satisfaction reaches zero abandons the restaurant.

Unfinished orders are cancelled and occupied tables are released.

### Step 9: Turn Results

The game displays the updated statistics.

After Turn 18, the victory conditions are evaluated.

## 8. Object-Oriented Design

The system demonstrates inheritance, polymorphism, encapsulation and separation of responsibilities.

### Inheritance

The three customer subclasses inherit common attributes and behaviour from `Customer`.

```text
              Customer
                 |
       ---------------------
       |         |         |
    Regular     VIP      Critic
    Customer  Customer   Customer
```

`ComboMeal` also extends `MenuItem`.

This allows combo meals to use the existing ordering, cooking and payment workflow without introducing a separate order system.

### Polymorphism

Different customer behaviours are implemented through method overriding.

Important methods include:

```java
getCustomerType()
getWaitingSatisfactionLoss()
getDiscountPercent()
chooseMenuItem(Menu menu)
```

The same customer-management code can process Regular, VIP and Critic customers.

Each subclass determines its own satisfaction loss, discount and food-selection behaviour.

### Encapsulation

Customer attributes are private and modified through defined methods.

Examples include:

```java
seatAt()
assignOrder()
markServed()
markReadyToPay()
markPaid()
abandon()
recoverSatisfaction()
```

These methods validate state changes.

Happy Hour activation is controlled through `GameState` to prevent repeated activation.

### Separation of Responsibilities

| Class / Package | Responsibility |
|---|---|
| `Main` | Creates game objects |
| `GameConfig` | Stores game settings |
| `GameState` | Stores shared state, statistics and Happy Hour information |
| `GameController` | Coordinates the game turns |
| `Customer` and subclasses | Customer states and behaviours |
| `CustomerManager` | Customer collections, satisfaction and lifecycle |
| `TableManager` | Seating and table allocation |
| `MenuItem` and `ComboMeal` | Food item representation |
| `Menu` | Menu item registration |
| `Order` | Order progress and locked prices |
| `OrderManager` | Order creation and tracking |
| `Host` | Independent seating actions |
| `Waiter` | Seating, order taking and serving |
| `Chef` | Food preparation |
| `Cashier` | Automatic payment processing |
| `InputHandler` | User input |
| `ConsoleUI` | Console display |

### Reuse of Existing Components

The Host reuses `TableManager` for seating.

ComboMeal inherits from `MenuItem` and reuses the existing order-preparation workflow.

Happy Hour recovery reuses customer satisfaction methods.

The existing customer lifecycle and payment mechanisms remain unchanged.

## 9. Stage 2 to Stage 3 Changes

Stage 3 extends Stage 2 rather than replacing existing game.

### Newly Added Files

| File | Purpose |
|---|---|
| `src/staff/Host.java` | Independent Host actions |
| `src/menu/ComboMeal.java` | Combined meal pricing and preparation |

### Modified Files

| File | Changes |
|---|---|
| `src/controller/GameConfig.java` | Adds Happy Hour duration |
| `src/controller/GameState.java` | Tracks Happy Hour activation and remaining turns |
| `src/controller/GameController.java` | Adds Host and Happy Hour actions |
| `src/ui/InputHandler.java` | Adds Host and Happy Hour input options |
| `src/app/Main.java` | Instantiates the Host |
| `src/customer/Customer.java` | Adds satisfaction recovery |
| `src/customer/CustomerManager.java` | Applies Happy Hour recovery |
| `src/menu/Menu.java` | Registers ComboMeal |
| `src/order/Order.java` | Supports Happy Hour discount calculation |
| `src/order/OrderManager.java` | Accepts Happy Hour status during order creation |
| `src/staff/Waiter.java` | Passes Happy Hour status into order creation |

### Unchanged Core Functionality

The following features are retained from earlier stages:

- 18-turn game duration
- Two tables
- Six scheduled customer arrivals
- Regular, VIP and Critic customer behaviours
- Waiter and Chef action limits
- Automatic Cashier payment
- Food preparation timing
- Customer eating and payment timing
- Satisfaction loss and abandonment
- Victory and defeat conditions

`Chef.java` and `Cashier.java` require no Stage 3 modifications because their existing implementations already support multi-unit preparation and locked order prices.


These decisions preserve reproducible gameplay and the required base-game configuration.

The `stage-3` tag should identify the completed Stage 3 implementation together with its release documentation.

Each stage must remain independently runnable.

