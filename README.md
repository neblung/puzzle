# Solves a puzzle

When I was young, I had a wooden toy. It was 4x5 area with some pieces on it. The pieces were like this:

- 4 1x1 tiny pieces (`o`)
- 4 1x2 tall pieces (`I`)
- 1 2x1 wide piece (`-`)
- 1 2x2 big piece (`X`)

At the start the pieces were arranged in the following position:

```
     1234
    +----+
  A |IooI|
  B |IooI|
  C | -- |
  D |IXXI|
  E |IXXI|
    +----+
```

The goal was, by moving the pieces around, to get the big piece to the top.

```
     1234          1234          1234              1234     
    +----+        +----+        +----+            +----+    
  A |IooI|      A |IooI|      A |IooI|          A | XX |    
  B |IooI|      B |IooI|      B |Io I|          B | XX |    
  C | -- |      C |--  |      C |--o |   ...    C |... |    
  D |IXXI|      D |IXXI|      D |IXXI|          D |    |    
  E |IXXI|      E |IXXI|      E |IXXI|          E |    |    
    +----+        +----+        +----+            +----+    
```

This project solves the puzzle with the help of breadth-first search implemented in kotlin. You can see the necessary
moves by running

    gradlew run

