# Courier Delivery Manager 📦

A JavaFX desktop application for managing couriers and their packages based on zone logic and proximity.

---

## Highlights
- Real-time UI updates via Observer pattern
- Zone-based courier assignment
- Proximity-based sorting

## Features

- Add packages with:
  - Recipient name
  - Address
  - (x, y) location

- Add couriers with:
  - Name
  - List of streets (comma-separated)
  - Zone center (point)
  - Zone radius (float)

- Automatically assign packages to couriers if:
  - The address matches one of their streets, or
  - The location is inside their circular delivery zone

- Each courier has a dedicated window:
  - View undelivered packages
  - Filter by street
  - Sort by proximity to zone center
  - Mark packages as delivered

- Global "Map View" shows all undelivered packages

---

## Technologies Used

- Java 17
- JavaFX
- SQLite (via JDBC)
- MVC pattern
- Observer pattern for live updates

---
