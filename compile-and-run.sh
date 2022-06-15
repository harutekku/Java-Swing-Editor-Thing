#!/bin/bash
# Copyright: Harutekku
# SPDX-License-Identifier: MIT

[ ! -d ./bin/ ] && mkdir ./bin

javac src/*.java -d bin/ && cd bin/ && java src.Main && cd ../
