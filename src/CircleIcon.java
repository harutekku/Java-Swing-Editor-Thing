/*
 * Copyright: Harutekku
 * SPDX-License-Identifier: MIT
 */
package src;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Color;

import javax.swing.Icon;

public class CircleIcon implements Icon {
    public CircleIcon() {
        this._c = null;
    }

    public CircleIcon(Color c) {
        this._c = c;
    }

    @Override
    public int getIconHeight() {
        return 10;
    }

    @Override
    public int getIconWidth() {
        return 10;
    }

    @Override
    public void paintIcon(Component color, Graphics ctx, int x, int y) {
        if (_c != null)
            ctx.setColor(_c);
        else
            ctx.setColor(color.getForeground());
        ctx.fillOval(x, y, getIconHeight(), getIconWidth());
    }

    private Color _c;
    
}
