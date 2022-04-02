# nios2SimDark

This is **Nios2Sim in dark mode!**
It's not perfect but it's here. Feel free to contribute and submit your pull requests.

![preview](https://github.com/Dicedead/nios2SimDark/blob/main/preview.jpg)

# How to use this:
There are two ways:
## 1) Simply download the provided JAR and start working.
Easy but, as for now, this will restrict you to the theme arbitrarily chosen by myself. [Click here to download.](https://github.com/Dicedead/nios2SimDark/raw/main/Nios2SimDark.jar)
## 2) If you want to edit the colors: 
Clone this repo, and edit these 4 lines in ``Nios2SimFrame`` in the package ``gui`` as you see fit:
```java
    public static final Color DARK_COLOR = Color.decode("#06011e"); //background color
    public static final Color DARK_MODE_FONT_COLOR = Color.WHITE; //general font color
    public static final Color DARK_MODE_INSTRUCTIONS_COLOR = Color.decode("#e75350"); //addi, sti, etc
    public static final Color DARK_MODE_REGISTERS_COLOR = Color.decode("#1cd6f2"); //t1, zero, etc
```
Then rebuild the JAR, here's a tutorial on how to do that on Intellij: https://cs108.epfl.ch/archive/21/g/intellij-export.html

Enjoy!
