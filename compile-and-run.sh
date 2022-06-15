#!/bin/bash
# Copyright: Harutekku
# SPDX-License-Identifier: MIT

if [ ! -e ./bin/ ]; then
    mkdir ./bin
fi

javac src/*.java -d bin/ && cd bin/ && java src.Main && cd ../
