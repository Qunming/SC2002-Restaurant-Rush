# Member 2 implementation plan (Stage 1 ONLY)

Files you own:
- `src/customer/Customer.java`
- `src/customer/CustomerManager.java`
- `src/customer/CustomerStatus.java` (enum already complete)
- `src/restaurant/Table.java`
- `src/restaurant/TableManager.java`

## Recommended order
1. Complete `Customer` state transitions and food choice.
2. Complete `Table` (occupant and availability).
3. Complete `TableManager` (seating and release).
4. Complete `CustomerManager` (register, filtered lists, eating, abandonment).
5. Test the entire customer lifecycle, separately from the game loop.

## Customer invariants
- New customer: satisfaction 100, WAITING, table/order null, turn markers -1.
- Valid transitions: WAITING -> SEATED -> ORDERED -> SERVED ->
  READY_TO_PAY -> PAID. WAITING, SEATED, or ORDERED can become ABANDONED
  only at satisfaction 0.
- `markReadyToPay(turn)` requires `turn > servedTurn`.
- Satisfaction falls by 8 only during WAITING, SEATED, ORDERED; never below 0.
- `markPaid()` does **not** charge money or release a table.
- `abandon()` does **not** cancel orders or release a table. Controller cleans up.
- `chooseMenuItem()` uses `(customerId - 1) % menu.getItems().size()` and
  the fixed item order SALAD, BURGER, PASTA.

## CustomerManager contracts
- `addCustomer`: reject null/duplicate customer IDs.
- `getCustomerById`: return matching customer or **null** if absent.
- `getWaitingCustomers`: queue FIFO by arrival order, return independent list.
- `getActiveCustomers`: all except PAID or ABANDONED.
- `getPaidCustomers`: PAID only.
- `getOldestReadyToPayCustomer(turn)`: earliest eligible readyToPayTurn strictly
  **less than** turn; tie breaks by arrival order; null if none.
- `advanceEating(turn)`: SERVED customer becomes READY_TO_PAY no earlier
  than the following turn.
- `applyEndTurnSatisfactionLoss()`: change statuses, return each **newly**
  abandoned customer exactly once; does not release tables or cancel orders.

## Table contracts
- `Table` owns one occupant and prevents double booking.
- `TableManager` owns the list of numbered tables and seating/release workflow.
- `TableManager.seatCustomer` returns false when customer isn't WAITING
  or no table is free. Table/customer occupancy must stay consistent.
- A table stays occupied until customer PAID or ABANDONED.
- Expose `getTables()` read-only.

## Simple manual test scenarios
1. Customer #1 starts at WAITING, 100 satisfaction, no table/order.
2. Seating #1 at table 1 works. Seating #1 twice must fail.
3. Two seats can be occupied; third seating request returns false.
4. End-of-turn penalty goes 100 -> 92 -> 84; never below 0.
5. SERVED on turn 4 cannot be READY_TO_PAY on turn 4; can on turn 5.
6. READY_TO_PAY on turn 5 is NOT eligible for payment on turn 5;
   becomes eligible on turn 6.
7. SERVED, READY_TO_PAY and PAID never lose satisfaction.
8. At satisfaction 0, newly abandoned appears in cleanup list once.
9. Table is NOT released when served; only PAID or ABANDONED.

## Design rationale
Only one customer type is specified for Stage 1. A concrete Customer class avoids
unnecessary inheritance and keeps state/behaviour together (high cohesion).
CustomerManager coordinates customers, while TableManager coordinates tables.
Neither calculates prices or handles the UI (lower coupling).
