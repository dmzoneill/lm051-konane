# Konane

<p align="center">
  <img src=".github/logo.png" alt="lm051-konane logo" width="128" />
</p>

A Java implementation of Konane, the Hawaiian checkers game, with computer
players. Written for the LM051 Computer Systems degree at the University of
Limerick.

## What it does

- Plays Konane on a selectable board size, in the console.
- A human player enters moves as row and column pairs, or `-1` to concede. Moves
  are validated.
- `Konane` holds the game and players; `KonaneDFS` adds a computer player that
  searches moves with depth-first search.
- Keeps a count of wins.

## Tech

- Java

## Running

Compile and run the classes in `Konane` or `KonaneDFS` with `javac` and `java`.

## License

Licensed under the Apache License 2.0. See [LICENSE](LICENSE).
