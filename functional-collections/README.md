# Scala Immutable Collections Hierarchy (High-Level)

## Iterable
Top-level base for all immutable collections.

- **Seq** = ordered collection
    - **IndexedSeq → Vector** (fast random access)
    - **LinearSeq → List** (fast head, recursive) (Seq(...) creates a List by default)
    - **Range** (efficient numeric interval) (this is also a IndexedSeq)

- **Set** = unique elements
    - **HashSet** (default)
    - **TreeSet** (sorted)

- **Map** = key/value pairs
    - **HashMap** (default)
    - **TreeMap** (sorted)

Use **Vector** by default for general Seq operations; use **List** for recursive / prepend-heavy workloads.
