# Changes from the GitHub Stage 1 TODO skeleton

Source repository reviewed: https://github.com/Qunming/SC2002-Restaurant-Rush
Default branch: `main`, observed 15 Oct 2026.

## Deliberate Stage 1-only simplifications

1. `src/customer/Customer.java`: changed `abstract class Customer` to concrete
   `class Customer`. The ID-based constructor is public.
2. **Removed** `src/customer/RegularCustomer.java`: with only one customer type,
   inheritance adds no Stage 1 behaviour. `new Customer(id)` is used at arrival.
3. Removed `getWaitingLossPerTurn()` and `getDiscountPercent()` from Customer.
   The fixed 8-point loss belongs to Customer itself.
4. **Removed** `src/policy/PricingPolicy.java`: Stage 1 always charges menu price.
5. `OrderManager.createOrder(Customer, Menu)` replaces
   `createOrder(Customer, Menu, PricingPolicy, boolean)`.
6. `Waiter.takeOrder(int, GameState)` replaces
   `takeOrder(int, GameState, boolean)`.
7. `GameState` constructor no longer accepts or stores a `PricingPolicy`.
8. `Order(int, Customer, MenuItem)` locks `MenuItem.getPriceCents()` directly.
9. Fixed original `GameController.playTurn()` skeleton mismatch: it previously
   called missing UI/arrival methods and displayed before arrival. The revised
   controller creates the arrival first, then displays state, then Waiter/Chef.
10. Kept the existing package layout, enums, turn constants, and main method
    names wherever possible for easier review.

## Integration warning
**Not drop-in compatible with the previous `main`.** Members 1, 3 and 4 must use the
revised constructor and method signatures above. Do not mix revised source
files with old local copies.
