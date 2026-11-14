# Reproducible Gameplay Tests — SC2002 Restaurant Rush

These complete turn-by-turn action tables provide all choices needed to reproduce the scenarios. Each table was verified against recorded console output from its actual compulsory Git tag. Each scenario completed all 18 turns. Tests used Java 26 with Java 21 compilation targeting (`javac --release 21`).

**How to read the tables:** Enter choices left to right as the respective staff menus appear. A dash (—) means that the menu does not appear. If only one customer/order is eligible, the game selects it automatically. Each table documents the recorded run; it is not a hypothetical strategy.

## Summary of verified outcomes

| Tag | Scenario | Revenue | Paid | Avg satisfaction | Abandoned | Final result |
|---|---|---:|---:|---:|---:|---|
| `stage-1` | Victory | $51.00 | 4 | 78.0 | 0 | **VICTORY** |
| `stage-1` | Defeat (queue abandonment) | $0.00 | 0 | N/A | 2 | **DEFEAT** |
| `stage-2` | Victory | $58.50 | 4 | 75.5 | 0 | **VICTORY** |
| `stage-2` | Defeat (queue abandonment) | $0.00 | 0 | N/A | 2 | **DEFEAT** |
| `stage-3` | Victory (Happy Hour on Turn 10) | $68.94 | 5 | 86.2 | 0 | **VICTORY** |
| `stage-3` | Defeat (no staff actions) | $0.00 | 0 | N/A | 2 | **DEFEAT** |

Defeat is the **expected and correct** outcome in the abandonment and no-action test cases. Stage 1 and Stage 2 also independently passed table-abandonment checks; their full action tables are omitted to keep one victory and one defeat table per stage.

## stage-1

### Victory

| Turn | Waiter | Chef |
|---|---|---|
| 1 | `1` Seat | `2` Wait |
| 2 | `2` Take order | `1` Prepare |
| 3 | `3` Serve | `2` Wait |
| 4 | `1` Seat | `2` Wait |
| 5 | `2` Take order | `1` Prepare |
| 6 | `3` Serve | `2` Wait |
| 7 | `1` Seat | `2` Wait |
| 8 | `2` Take order | `1` Prepare |
| 9 | `4` Wait | `1` Prepare |
| 10 | `3` Serve | `2` Wait |
| 11 | `1` Seat | `2` Wait |
| 12 | `2` Take order | `1` Prepare |
| 13 | `4` Wait | `2` Wait |
| 14 | `3` Serve | `2` Wait |
| 15 | `4` Wait | `2` Wait |
| 16 | `4` Wait | `2` Wait |
| 17 | `4` Wait | `2` Wait |
| 18 | `4` Wait | `2` Wait |

**Verified result: VICTORY.** Revenue **$51.00**; paid customers **4**; average paid satisfaction **78.0**; abandoned **0**; completed **18/18 turns**.

Verification: [Recorded console output](stage-1-tag-victory-output.txt)

### Defeat (queue abandonment)

| Turn | Waiter | Chef |
|---|---|---|
| 1 | `4` Wait | `2` Wait |
| 2 | `4` Wait | `2` Wait |
| 3 | `4` Wait | `2` Wait |
| 4 | `4` Wait | `2` Wait |
| 5 | `4` Wait | `2` Wait |
| 6 | `4` Wait | `2` Wait |
| 7 | `4` Wait | `2` Wait |
| 8 | `4` Wait | `2` Wait |
| 9 | `4` Wait | `2` Wait |
| 10 | `4` Wait | `2` Wait |
| 11 | `4` Wait | `2` Wait |
| 12 | `4` Wait | `2` Wait |
| 13 | `4` Wait | `2` Wait |
| 14 | `4` Wait | `2` Wait |
| 15 | `4` Wait | `2` Wait |
| 16 | `4` Wait | `2` Wait |
| 17 | `4` Wait | `2` Wait |
| 18 | `4` Wait | `2` Wait |

**Verified result: DEFEAT.** Revenue **$0.00**; paid customers **0**; average paid satisfaction **N/A**; abandoned **2**; completed **18/18 turns**.

Verification: [Recorded console output](stage-1-tag-queue-output.txt)

## stage-2

### Victory

| Turn | Waiter | Chef |
|---|---|---|
| 1 | `1` Seat | `2` Wait |
| 2 | `2` Take order | `1` Prepare |
| 3 | `3` Serve | `2` Wait |
| 4 | `1` Seat | `2` Wait |
| 5 | `2` Take order | `1` Prepare |
| 6 | `3` Serve | `2` Wait |
| 7 | `1` Seat | `2` Wait |
| 8 | `2` Take order | `1` Prepare |
| 9 | `4` Wait | `1` Prepare |
| 10 | `3` Serve | `2` Wait |
| 11 | `1` Seat | `2` Wait |
| 12 | `2` Take order | `1` Prepare |
| 13 | `4` Wait | `1` Prepare |
| 14 | `3` Serve | `2` Wait |
| 15 | `4` Wait | `2` Wait |
| 16 | `4` Wait | `2` Wait |
| 17 | `4` Wait | `2` Wait |
| 18 | `4` Wait | `2` Wait |

**Verified result: VICTORY.** Revenue **$58.50**; paid customers **4**; average paid satisfaction **75.5**; abandoned **0**; completed **18/18 turns**.

Verification: [Recorded console output](stage-2-tag-victory-output.txt)

### Defeat (queue abandonment)

| Turn | Waiter | Chef |
|---|---|---|
| 1 | `4` Wait | `2` Wait |
| 2 | `4` Wait | `2` Wait |
| 3 | `4` Wait | `2` Wait |
| 4 | `4` Wait | `2` Wait |
| 5 | `4` Wait | `2` Wait |
| 6 | `4` Wait | `2` Wait |
| 7 | `4` Wait | `2` Wait |
| 8 | `4` Wait | `2` Wait |
| 9 | `4` Wait | `2` Wait |
| 10 | `4` Wait | `2` Wait |
| 11 | `4` Wait | `2` Wait |
| 12 | `4` Wait | `2` Wait |
| 13 | `4` Wait | `2` Wait |
| 14 | `4` Wait | `2` Wait |
| 15 | `4` Wait | `2` Wait |
| 16 | `4` Wait | `2` Wait |
| 17 | `4` Wait | `2` Wait |
| 18 | `4` Wait | `2` Wait |

**Verified result: DEFEAT.** Revenue **$0.00**; paid customers **0**; average paid satisfaction **N/A**; abandoned **2**; completed **18/18 turns**.

Verification: [Recorded console output](stage-2-tag-queue-output.txt)

## stage-3

### Victory (Happy Hour on Turn 10)

| Turn | Happy Hour | Host | Waiter | Chef |
|---|---|---|---|---|
| 1 | `2` Save | `1` Seat | `2` Take order | `1` Prepare |
| 2 | `2` Save | `2` Wait | `3` Serve | `2` Wait |
| 3 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 4 | `2` Save | `1` Seat | `2` Take order | `1` Prepare |
| 5 | `2` Save | `2` Wait | `3` Serve | `2` Wait |
| 6 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 7 | `2` Save | `1` Seat | `2` Take order | `1` Prepare |
| 8 | `2` Save | `2` Wait | `4` Wait | `1` Prepare |
| 9 | `2` Save | `2` Wait | `3` Serve | `2` Wait |
| 10 | `1` Activate | `1` Seat | `2` Take order | `1` Prepare |
| 11 | — | `2` Wait | `4` Wait | `1` Prepare |
| 12 | — | `2` Wait | `4` Wait | `1` Prepare |
| 13 | — | `1` Seat | `3` Serve | `2` Wait |
| 14 | — | `2` Wait | `2` Take order | `1` Prepare |
| 15 | — | `2` Wait | `3` Serve | `2` Wait |
| 16 | — | `1` Seat | `2` Take order | `1` Prepare |
| 17 | — | `2` Wait | `3` Serve | `2` Wait |
| 18 | — | `2` Wait | `4` Wait | `2` Wait |

**Verified result: VICTORY.** Revenue **$68.94**; paid customers **5**; average paid satisfaction **86.2**; abandoned **0**; completed **18/18 turns**.

Verification: [Recorded console output](stage-3-victory-output.txt)

### Defeat (no staff actions)

| Turn | Happy Hour | Host | Waiter | Chef |
|---|---|---|---|---|
| 1 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 2 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 3 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 4 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 5 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 6 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 7 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 8 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 9 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 10 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 11 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 12 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 13 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 14 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 15 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 16 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 17 | `2` Save | `2` Wait | `4` Wait | `2` Wait |
| 18 | `2` Save | `2` Wait | `4` Wait | `2` Wait |

**Verified result: DEFEAT.** Revenue **$0.00**; paid customers **0**; average paid satisfaction **N/A**; abandoned **2**; completed **18/18 turns**.

Verification: [Recorded console output](stage-3-defeat-output.txt)

## How to reproduce a scenario

Use Windows PowerShell in the repository root. Choose the Git tag matching the table, delete old compiled classes, compile, and start the game. Then manually enter the numbers from the table, left to right, one turn at a time. For example:

```powershell
git switch --detach stage-3
if (Test-Path build) { Remove-Item build -Recurse -Force }
javac --release 21 -encoding UTF-8 -sourcepath src -d build src/app/Main.java
java -cp build app.Main
git switch main
```

For other scenarios, replace `stage-3` with the matching tag and follow its action table. Compare the resulting final scoreboard with its recorded console output.

## Scope and limitations

- These tests confirm the listed full-game outcomes and the recorded gameplay flows; they do not replace targeted unit tests for every edge case.
- The Stage 3 victory demonstrates a two-turn Happy Hour starting on Turn 10, the Critic ComboMeal, three preparation actions, payment timing, and a complete 18-turn victory.
- Additional targeted checks recommended: VIP + Happy Hour stacked discount, satisfaction recovery for an already dissatisfied customer, invalid-input retries, and optional enhanced-release functionality.
- All existing Git release tags are preserved; this evidence document belongs on the submission branch `main`.

