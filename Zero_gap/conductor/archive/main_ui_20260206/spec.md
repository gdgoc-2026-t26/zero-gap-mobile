# Specification: Implement the Core UI Layout and Edge-to-Edge Support in MainActivity

## Overview
This track focuses on establishing the foundational UI for the Zero-Gap application's main dashboard, based on the `docs/main.html` prototype. It ensures a modern, polished look with full edge-to-edge support and Material Design 3 integration.

## Requirements

### 1. Visual Identity & Theme
- **Colors:** Use the indigo/purple color palette from the prototype.
    - Indigo 600: `#6366F1` (Primary)
    - Indigo 200: `#C7D2FE`
    - Slate 900: `#0F172A` (Dashboard gradient start)
    - Slate 800: `#1E293B` (Dashboard gradient end)
    - Background: `#F9FAFB`
- **Typography:** Implement Pretendard font (or standard Sans-serif as fallback).
- **Shapes:** Rounded corners (24dp for major cards, 16dp for items).

### 2. MainActivity Structure
- **Edge-to-Edge:** Implement `enableEdgeToEdge()` and handle window insets correctly for status and navigation bars.
- **Layout:** Root `CoordinatorLayout` or `ConstraintLayout` with a `NestedScrollView` for the main content.
- **Header:** Sticky top bar with:
    - App Logo (Gradient background with lightning icon).
    - App Title ("Zero-Gap").
    - Notification icon with badge.
    - Profile avatar.
- **Bottom Navigation:** Fixed bottom bar with 5 items (Home, Challenge, Plus, Diary, My).

### 3. Dashboard Content (Scrollable)
- **Summary Card:** 
    - Gradient background (`#1E293B` to `#0F172A`).
    - Title ("완벽하지 않아도 충분해요!").
    - Streak badge ("🔥 7일 연속").
    - 3-column stats grid (Challenges, Average Emotion, Success Status).
- **Re-engagement Alert:**
    - Light blue background (`#EEF2FF`).
    - Seedling icon.
    - Title and description regarding return after 3 days.
- **Action Challenges:**
    - Section header with filters (5-10m, 20-30m, 1h+).
    - List of challenge items (e.g., "코테 1문제 풀기") with icons and chevrons.
- **Emotional Diary Report:**
    - Card with monthly report title.
    - Simple bar graph visualization for the week.
    - "Record Emotion" button.

## Success Criteria
- [ ] UI accurately reflects the `docs/main.html` layout.
- [ ] Edge-to-edge support is functional (no overlapping with system bars).
- [ ] Smooth scrolling and responsive layout.
- [ ] Follows Material Design 3 principles.
