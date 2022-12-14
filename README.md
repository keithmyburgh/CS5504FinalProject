## **Perceptron Branch Predictor**

This is Keith Myburgh's final project code for CS5504 at Virginia Tech.

In this repo exists the perceptron.scala file, which contains the code
for the Perceptron branch predictor. If you would like to run this, you
need to clone the chipyard repository first.

Once that is complete, you need to place the perceptron.scala into the bpd
directory. This can be found at chipyard/generators/boom/src/main/scala/ifu/bpd.

Additionally, you will need to replace the BoomConfigs.scala and
config-mixins.scala files in the repo with the two I have provided in the
configs directory.

In the benchmarks repo I have provided the .riscv binaries I used to benchmark
the SW, TAGE (Default), and Perceptron predictors.

If you do not wish to create your own executibles, I have provided some in the
exes directory.

If you have any questions, email keith22@vt.edu.

If somehow you are seeing this past the year 2022, then email
myburghkeith1@gmail.com a picture of your favorite album and tell me which
song is your favorite. I have been listening to the same music for too long 
and am looking for some new suggestions. It will also be kinda funny to get a
random email with some album pic in it and nothing else.

Bonus points if they sound similar to the Black Keys / Arctic Monkeys and they
actually had / have a drummer who is not afraid to allow some 808s into the mix. 
