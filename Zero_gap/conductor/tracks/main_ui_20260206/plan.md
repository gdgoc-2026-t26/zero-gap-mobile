# Implementation Plan: Implement the Core UI Layout and Edge-to-Edge Support in MainActivity

## Phase 1: Foundation & Theme [checkpoint: b1298ec]
Establish the core visual building blocks and ensure system-level UI integration.

- [x] **Task: Define Colors and Typography** bd96223
    - [ ] Create `colors.xml` with the indigo and slate palette.
    - [ ] Define the base `Theme.Zero_gap` with Material 3.
- [x] **Task: Robust Edge-to-Edge Support** 23deb79
    - [ ] Implement `enableEdgeToEdge()` in `MainActivity.kt`.
    - [ ] Add inset handling for the main container.
- [x] **Task: Conductor - User Manual Verification 'Foundation & Theme' (Protocol in workflow.md)** b1298ec

## Phase 2: Shell Layout [checkpoint: 806f3e5]
Implement the sticky header and bottom navigation bar.

- [x] **Task: Implement Top App Bar** e72be8b
    - [ ] Write Espresso tests for header visibility and elements.
    - [ ] Create the header layout XML with logo, title, and icons.
- [x] **Task: Implement Bottom Navigation Bar** b305dd7
    - [ ] Write Espresso tests for bottom nav existence and items.
    - [ ] Create the bottom navigation menu and view.
- [x] **Task: Conductor - User Manual Verification 'Shell Layout' (Protocol in workflow.md)** 806f3e5

## Phase 3: Dashboard & Content
Build the scrollable content area with the achievement card and lists.

- [x] **Task: Achievement Summary Card** e11e3b2
    - [ ] Write unit/Espresso tests for card presence and data display.
    - [ ] Implement the gradient card with stats grid.
- [x] **Task: Action Challenge List** acacf3c
    - [ ] Write tests for challenge item rendering.
    - [ ] Implement the section with filter buttons and challenge cards.
- [x] **Task: Emotional Report Card** 937f4d6
    - [ ] Write tests for the report card structure.
    - [ ] Implement the simplified bar graph and action button.
- [x] **Task: Conductor - User Manual Verification 'Dashboard & Content' (Protocol in workflow.md)** acffe51

## Phase 4: UI Refinements & Bug Fixes [checkpoint: 992ff3c]
Address visual feedback and layout issues detected during manual verification.

- [x] **Task: Improve Seedling Icon** 992ff3c
    - [x] Update the re-engagement notification icon.
    - [x] Option A: Use a thicker, more consistent SVG path.
    - [ ] Option B: Replace with the '🍀' emoji if SVG adjustment is difficult.
- [x] **Task: Fix Bottom Navigation Layout** 992ff3c
    - [x] Fix the issue where the bottom navigation bar is cut off.
    - [x] Correct the Window Insets handling (the empty space above the nav bar suggests double-padding or incorrect system bar inset application).
    - [x] Ensure the nav bar sits flush at the bottom while respecting edge-to-edge gesture areas.
- [x] **Task: Conductor - User Manual Verification 'UI Refinements' (Protocol in workflow.md)** 992ff3c