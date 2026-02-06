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

## Phase 2: Shell Layout
Implement the sticky header and bottom navigation bar.

- [ ] **Task: Implement Top App Bar**
    - [ ] Write Espresso tests for header visibility and elements.
    - [ ] Create the header layout XML with logo, title, and icons.
- [ ] **Task: Implement Bottom Navigation Bar**
    - [ ] Write Espresso tests for bottom nav existence and items.
    - [ ] Create the bottom navigation menu and view.
- [ ] **Task: Conductor - User Manual Verification 'Shell Layout' (Protocol in workflow.md)**

## Phase 3: Dashboard & Content
Build the scrollable content area with the achievement card and lists.

- [ ] **Task: Achievement Summary Card**
    - [ ] Write unit/Espresso tests for card presence and data display.
    - [ ] Implement the gradient card with stats grid.
- [ ] **Task: Action Challenge List**
    - [ ] Write tests for challenge item rendering.
    - [ ] Implement the section with filter buttons and challenge cards.
- [ ] **Task: Emotional Report Card**
    - [ ] Write tests for the report card structure.
    - [ ] Implement the simplified bar graph and action button.
- [ ] **Task: Conductor - User Manual Verification 'Dashboard & Content' (Protocol in workflow.md)**
