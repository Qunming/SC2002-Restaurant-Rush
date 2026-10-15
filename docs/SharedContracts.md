# Stage 1 cross-member contracts

| Owner | Method | Agreed behaviour |
| --- | --- | --- |
| M1 | `GameController.run()` | Exactly 18 full turns; no early exit |
| M1 | `GameController.playTurn()` | Arrival, dashboard, Waiter, Chef, end phases |
| M1 | `GameState.recordPayment(Customer, Order)` | Add paid bill once and satisfaction at payment |
| M2 | `CustomerManager.getCustomerById(int)` | Return null for missing ID |
| M2 | `CustomerManager.advanceEating(int)` | Only SERVED from earlier turn becomes READY_TO_PAY |
| M2 | `CustomerManager.getOldestReadyToPayCustomer(int)` | Oldest ready before current turn; null if none |
| M2 | `CustomerManager.applyEndTurnSatisfactionLoss()` | Return only newly abandoned customers |
| M2 | `TableManager.seatCustomer(Customer)` | Return false for expected seating failure |
| M3 | `OrderManager.createOrder(Customer, Menu)` | Customer chooses food, bill fixed at menu price |
| M3 | `Order.prepareOneUnit(int)` | One unit; READY after final required unit |
| M3 | `Order.markServed(int)` | READY since a PRIOR turn only |
| M3 | `OrderManager.cancelUnpaidOrderFor(Customer)` | Cancel unpaid abandoned order |
| M4 | `Waiter` / `Chef` | One valid task per turn; invalid action does not consume task |
| M4 | `Cashier.processOldestEligiblePayment(...)` | At most one payment per turn; free table |

## Turn phases
1. Create scheduled customer BEFORE printing dashboard.
2. Waiter action (one action or Wait).
3. Chef action (one preparation unit or Wait).
4. `CustomerManager.advanceEating(currentTurn)`.
5. `Cashier.processOldestEligiblePayment(gameState, currentTurn)`.
6. Apply satisfaction loss and clean up abandonments.
7. Show turn results; advance turn, except after turn 18.

## Validation convention
- Invalid staff action -> `IllegalArgumentException` with helpful message;
  Member 1 catches/retries it without spending a staff action.
- Missing lookup -> `null` for customer/order IDs.
- Unimplemented TODO -> `UnsupportedOperationException` only during development.
- Read-only lists -> `List.copyOf(...)` or an independent list.
