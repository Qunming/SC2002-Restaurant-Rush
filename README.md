
# SC2002 Restaurant Rush — Stage 1

An offline, console-based Java restaurant management game built using object-oriented programming.

## Requirements

- Java 21 or later, available on your PATH (`java -version` and `javac -version`).
- Windows Command Prompt or PowerShell for the provided build script.
- No external libraries or internet connection are needed to play.

## Compile and run

From the repository root in Command Prompt or PowerShell:

```cmd
.\compile_stage1.cmd
java -cp build app.Main
```

Or compile manually:

```cmd
javac -encoding UTF-8 -sourcepath src -d build src\app\Main.java
java -cp build app.Main
```

The compilation script creates a `build` folder. Compiled `.class` files are excluded from Git.

## Stage 1 rules

- The game always runs for **18 turns** with **2 tables**.
- One customer arrives at the start of turns **1, 4, 7, 10, 13, 16**.
- Each customer starts with **100 satisfaction** and loses **8** at the end of every unserved turn (minimum 0). An unserved customer who reaches zero abandons the restaurant.
- The **Waiter** may seat one customer, take one order, serve one READY order, or wait once per turn.
- The **Chef** works after the Waiter and prepares at most one food unit per turn, or waits.
- The **Cashier** automatically processes at most one eligible payment per turn, oldest eligible customer first.
- A dish completed this turn cannot be served until a later turn. A customer served in turn `t` becomes READY_TO_PAY in turn `t+1` and can pay no earlier than turn `t+2`.
- A table is freed only after payment or abandonment. Revenue counts only completed payments.

| Dish | Price | Preparation units |
| --- | ---: | ---: |
| Salad | $9.00 | 1 |
| Burger | $15.00 | 1 |
| Pasta | $18.00 | 2 |

Customer food selection is deterministic: customer ID 1 chooses Salad, ID 2 Burger, ID 3 Pasta; the cycle repeats. The player never chooses the dish.

### Victory conditions

All three conditions must hold **at the end of turn 18**:

1. At least **4 paid customers**.
2. Revenue of at least **$45.00**.
3. Average satisfaction of paid customers at least **60**.

The average uses satisfaction when payment is collected; when nobody has paid, the screen shows `N/A`.

## Class responsibilities

| Member | Packages | Responsibility |
| --- | --- | --- |
| M1 | `app`, `controller`, `ui` | Set up the game, coordinate turns, display status, collect input, record statistics |
| M2 | `customer`, `restaurant` | Customer lifecycle, satisfaction, queues, seating and table management |
| M3 | `menu`, `order` | Menu data, order creation, preparation and order state |
| M4 | `staff` | Waiter actions, Chef actions and automatic Cashier payments |

No Stage 2 or Stage 3 features are included in this release.

## Testing and release

Before creating the `stage-1` Git tag:

1. Run the game for all 18 turns and record one victory run and one defeat run with exact input choices.
2. Verify invalid choices can be retried without consuming the staff action.
3. Verify cooking, serving and payment timing; ensure that customer and order states remain consistent.
4. Compile and run from a clean checkout of the tagged commit.

The `stage-1` tag should point to the verified, independently runnable Stage 1 release. Do not add VIP, Critic, Host, ComboMeal or Happy Hour features to that tag.
