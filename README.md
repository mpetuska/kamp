[![Gitpod ready-to-code](https://img.shields.io/badge/gitpod-ready--to--code-blue?logo=gitpod&style=flat-square)](https://gitpod.io/#https://github.com/mpetuska/kamp)
[![Kotlin JS IR supported](https://img.shields.io/badge/Kotlin%2FJS-IR%20supported-yellow?style=flat-square&logo=kotlin)](https://kotl.in/jsirsupported)

# [KAMP](https://www.kamp.ml)
This project aims to provide an extensive catalogue of kotlin multiplatform projects across various maven repositories
out there.

## Motivation
The project is heavily inspired by existing community catalogues such as [Awesome Kotlin](https://github.com/KotlinBy/awesome-kotlin)
& [multiplatform-libraries](https://github.com/icerockdev/multiplatform-libraries). While they all provide the same feature 
as this project (a catalogue for kotlin libraries), they all require the library authors to be aware of them and register 
their creations. This project aims to automate that to ensure the community has an extensive self-updating catalogue of 
all the marvelous kotlin creations out there!

## Structure
The project consists of two modules - a background processor providing the data feed and a webapp for exposing it as a catalogue.

### Scanner
An internet crawler that's meant to scan and identify kotlin libraries across the web. 
It is only able to find the libraries published with gradle-metadata, so older Kotlin libraries might be missing.
Currently, supported maven repositories are:
* [Maven Central](https://repo1.maven.org/maven2)
* [Gradle Plugin Portal](https://plugins.gradle.org/m2)
* [JBoss](https://repository.jboss.org/nexus/content/repositories/releases)
* [Spring](https://repo.spring.io/release)

## App

The driving server and client to expose the data collected by the scanner as a nice web-based catalogue. 
