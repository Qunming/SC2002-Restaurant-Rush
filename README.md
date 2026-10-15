# Restaurant Rush - Stage 1 Revised Skeleton

**This repository contains the revised Stage 1-only source skeleton.**

This design intentionally uses only the Stage 1 requirements of the SC2002
Restaurant Rush brief. It does **not** anticipate requirements from later stages.
It is a **compilable TODO skeleton**, not a playable game. Business-logic TODOs
throw `UnsupportedOperationException` until their owners implement them.

## Key design decisions
- `Customer` is a **concrete class**. There is no `RegularCustomer` subclass.
- All Stage 1 customers lose **8 satisfaction** at the end of each unserved turn,
  minimum 0; starting satisfaction is **100**.
- No customer-type discount method, `happyHourActive` argument, Host or ComboMeal.
- No `PricingPolicy` class: the Stage 1 bill is the menu item's fixed price.
  `Order` locks that price at creation. This avoids a trivial extra class.
- Managers coordinate; Customer/Order/Table own their respective state changes;
  console UI only gathers input and displays output.
- Customer food choice is deterministic: customer IDs 1, 2, 3 select Salad,
  Burger, Pasta respectively, repeating each three IDs in the fixed menu order.
- Keep all money in **integer cents**.

## Required Stage 1 configuration
- 18 turns, always complete all 18.
- Two tables.
- One regular customer arrives at start of turns 1, 4, 7, 10, 13, 16.
- Waiter: one Seat, Take Order, Serve, or Wait action per turn.
- Chef: one preparation unit or Wait after the Waiter's action.
- Cashier: one automatic eligible payment per turn, oldest first.
- Menu: Salad 900 cents / 1 unit; Burger 1500 / 1; Pasta 1800 / 2.
- Victory after turn 18 iff at least 4 PAID customers AND at least 4500 cents
  revenue AND average satisfaction of paid customers at least 60.
- Payment recorded ONLY when actually collected; no unpaid revenue.

## Responsibilities
| Member | Packages | Responsibilities |
| --- | --- | --- |
| 1 | app/, controller/, ui/ | Turn loop, game state, dashboard, input, win/loss |
| 2 | customer/, restaurant/ | Queue, customer status, waiting loss, eating, table assignment |
| 3 | menu/, order/ | Foods, orders, prices, cooking-progress state |
| 4 | staff/ | Waiter, Chef, automatic Cashier |

## Compile on Windows (Java 21)
From this folder in Command Prompt:

```cmd
compile_stage1.cmd
```

Or manually:

```cmd
if not exist build mkdir build
dir /s /b src\*.java > sources.txt
javac -d build @sources.txt
java -cp build app.Main
```

The final command currently reaches an unfinished TODO and throws an exception.
That is expected for a coding skeleton, NOT for final Stage 1 submission.

## Integration compatibility
This is a revised architecture, not a drop-in replacement for the previous
skeleton. Members 1, 3 and 4 must use the revised constructor and method
signatures detailed in `docs/ChangesFromGitHub.md`. Collaborators using the
previous repository history should clone the new main branch or reset their
local main after coordinating any work in progress.

Create the `stage-1` tag only AFTER the game is fully playable and the team
records one reproducible victory and one defeat run.
