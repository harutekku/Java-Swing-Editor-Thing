/*
 * Copyright: Harutekku
 * SPDX-License-Identifier: MIT
 */
package src;

import java.awt.Color;

public class Model {
    public Model() {
        this._filename       = "Bez tytułu";
        this._lastForeground = null;
        this._lastBackground = null;
        this._lastSize       = -1;
        this._status         = "new";
        this._isSaved        = false;
        this._isNew          = true;
    }

    public String Filename() {
        return _filename;
    }

    public void SetFilename(String filename) {
        _filename = filename;
    }

    public Color Foreground() {
        return _lastForeground;
    }

    public void SetForeground(Color color) {
        _lastForeground = color;
    }

    public Color Background() {
        return _lastBackground;
    }

    public void SetBackground(Color color) {
        _lastBackground = color;
    }

    public int Size() {
        return _lastSize;
    }

    public void SetSize(int size) {
        _lastSize = size;
    }
 
    public String Status() { 
        return _status;
    }

    public void SetStatus(String status) {
        _status = status;
    }

    public boolean IsSaved() {
        return _isSaved;
    }

    public void SetSaved(boolean value) {
        _isSaved = value;
    }

    public boolean IsNew() {
        return _isNew;
    }

    public void SetNew(boolean value) {
        _isNew = value;
    }

    private String  _filename;
    private Color   _lastForeground;
    private Color   _lastBackground;
    private int     _lastSize;
    private String  _status;
    private boolean _isSaved;
    private boolean _isNew;
}
