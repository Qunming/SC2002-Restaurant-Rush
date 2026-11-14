# Release Verification Results

Test environment: Java 26, compiling with --release 21.
All scenarios were run against their actual Git tags.

| Tag | Scenario | Revenue | Paid | Avg satisfaction | Outcome |
|---|---|---:|---:|---:|---|
| stage-1 | Victory | $51.00 | 4 | 78.0 | PASS |
| stage-1 | Queue abandonment | $0.00 | 0 | N/A | DEFEAT |
| stage-1 | Table abandonment | $0.00 | 0 | N/A | DEFEAT |
| stage-2 | Victory | $58.50 | 4 | 75.5 | PASS |
| stage-2 | Queue abandonment | $0.00 | 0 | N/A | DEFEAT |
| stage-2 | Table abandonment | $0.00 | 0 | N/A | DEFEAT |
| stage-3 | Victory | $68.94 | 5 | 86.2 | PASS |
| stage-3 | No-service defeat | $0.00 | 0 | N/A | DEFEAT |

All gameplay runs completed 18 turns.

Stage 1 and 2 abandonment tests also confirmed distinct
queue and table abandonment messages.

Stage 3 victory verified:
- Happy Hour active on Turns 10 and 11
- Critic ComboMeal priced at $19.44 during Happy Hour
- ComboMeal completed after 3 preparation units
- Six served customers and five paid customers
- Game continued to Turn 18 despite meeting victory targets

## Reproduce

1. Checkout the corresponding Git tag.
2. Delete the build directory.
3. Compile: javac --release 21 -encoding UTF-8 -sourcepath src -d build src/app/Main.java
4. Run: Get-Content <scenario-input-file> | java -cp build app.Main
5. Compare the final scoreboard against the recorded output.

Pending separate checks:
- Stage 3 Happy Hour satisfaction recovery
- Stacked VIP and Happy Hour discounts
- Additional invalid-input edge cases
- Optional enhanced-release integration tests
