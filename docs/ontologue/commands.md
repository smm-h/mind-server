# Ontologue Commands

| #   | Command                      | Alternative command                | Description                                  |
| --- | ---------------------------- | ---------------------------------- | -------------------------------------------- |
| 1   | `/whoami`                    | `who am I`                         | Prints the current mind                      |
| 1   | `/everyone`                  | `everyone`                         | Lists all minds                              |
| 1   | `/everything`                | `everything`                       | Lists all ideas in the current mind          |
| 2   | `/every <a>`                 | `every <a>`                        | Lists all instances of that idea             |
| 2   | `/createmind <m>`            | `createmind <m>`                   | Creates a new mind                           |
| 2   | `/iam <m>`                   | `I am <m>`                         | Switches to that mind                        |
| 2   | `/imagine <a>`               | `imagine <a>`                      | Creates a new idea                           |
| 2   | `/whatis <a>`                | `what is <a>`                      | Prints what an idea is                       |
| 3   | `/is <a> <b>`                | `is <a> <b>`                       | Prints whether or not an idea is another     |
| 3+1 | `/has <a> <p> [<t>]`         | `does <a> have <p> [as <t>]`       | Prints whether or not an idea has a property |
| 3   | `/become <a> <b>`            | `<a> is <b>`                       | Makes an idea another                        |
| 4+1 | `/possess <a> <p> <t> [<s>]` | `<a> has <p> as <t> [default <s>]` | Makes an idea have a property                |
| 5   | `/reify <a> <p> <t> <s>`     | `<a> reifies <p> as <t> = <s>`     | Makes an idea reify a property               |
| 2+1 | `/instantiate <a> [<s>]`     | `new <a> [from <s>]`               | Creates a new instance of that idea          |

- `<m>`: mind name
- `<a>`, `<b>`, `<t>`: idea names
- `<p>`: property name
- `<s>`: instance serialization
