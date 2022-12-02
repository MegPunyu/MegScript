# MegScript

## Run a MegScript file
Run [NutMeg.jar](https://github.com/MegPunyu/MegScript/tree/main/dist) to run a MegScript file.


```shell
java -jar NutMeg.jar Main.meg
```

## Syntax/Grammar
Documentation not yet available.

## Example
### Declare a variable
```
$ x 1;  \\ let x 1
```

### evaluate an expression
```
$ y x + x * 2;   \\ y = (x + x) * 2
$ z y + (y * 2)  \\ z = y + (y * 2)
? x >> 12 y z;   \\ y if x > 12 else z
```

### Define a function
```
$ add :a :b a + b;
```

### Invoke a function
```
\\ 1 + 2 (all of the following codes are correct)

add[1][2];
add[1] < 2;
add < 1 < 2;
1 > add < 2;

```

### Print strings
```
log < "mochi";

"puni" > log;

log < "mochi" < "puni";
```

### Find the factorial of a number
```
$ fact :n
  ? n >= 2
    n - 1 > fact * n
    1;

fact < 10 > log;
```

### Print each element of a list
```
$ list (1, 2, 3);

list |> log;
```

### Split a string into a list
```
"ABC_DEF_G" > (:s s <-> "_") > log;  \\ [ABC, DEF, G]
```

### Find the sum of 1 to 10
```
$ sum 1 > $ for : i ? i <= 10
  i + 1 > for + i
  0;

log < sum;  \\ 55
log < for;  \\ () : variable "for" is not in current scope
```