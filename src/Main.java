/*
 * Copyright: Harutekku
 * SPDX-License-Identifier: MIT
 */
package src;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Controller(new View(), new Model()));
    }
}