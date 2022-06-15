/*
 * Copyright: Harutekku
 * SPDX-License-Identifier: MIT
 */
package src;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.awt.Font;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.JSeparator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;

public class View extends JFrame {

    //#region Constructors

    public View() {
        this._textArea       = new JTextArea();
        this._fullTextArea   = new JPanel(new FlowLayout(FlowLayout.LEFT));
        this._fileOptions    = new ArrayList<>();
        this._addresses      = new ArrayList<>();
        this._foreground     = new ArrayList<>();
        this._background     = new ArrayList<>();
        this._fontSizes      = new ArrayList<>();
        this._lastForeground = new JLabel("fg");
        this._lastBackground = new JLabel("bg");
        this._lastSize       = new JLabel("size");
        this._status         = new JLabel();

        this._textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        this._textArea.setSize(new Dimension(600, 600));
        this._textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        this.setTitle("Notepad");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setPreferredSize(new Dimension(600, 600));

        MakeLayout();
    }

    //#endregion
    
    //#region Public API

    private void MakeLayout() {
        this.setLayout(new BorderLayout());
        MakeOptionsBar();
        MakeTextArea();
        MakeStatusBar();
    }

    public JTextArea TextArea() {
        return _textArea;
    }

    public JPanel TextAreaJPanel() {
        return _fullTextArea;
    }

    public List<JMenuItem> FileOptions() {
        return _fileOptions;
    }

    public List<JMenuItem> Addresses() {
        return _addresses;
    }

    public List<JRadioButtonMenuItem> Foreground() {
        return _foreground;
    }

    public List<JRadioButtonMenuItem> Background() {
        return _background;
    }

    public List<JMenuItem> FontSizes() {
        return _fontSizes;
    }

    public JLabel LastForeground() {
        return _lastForeground;
    }

    public JLabel LastBackground() {
        return _lastBackground;
    }

    public JLabel LastSize() {
        return _lastSize;
    }

    public JLabel Status() {
        return _status;
    }

    //#endregion

    //#region Layout creation methods

    private void MakeOptionsBar() {
        var pane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        var menuBar = new JMenuBar();
        {
            var fileMenu = new JMenu("File");
            fileMenu.add(Register(_fileOptions, MakeFastOption("Open", 'O', "ctrl O")));
            fileMenu.add(Register(_fileOptions, MakeFastOption("Save", 'S', "ctrl S")));
            fileMenu.add(Register(_fileOptions, MakeFastOption("Save As", 'A', "ctrl A")));

            var separator = new JSeparator(SwingConstants.HORIZONTAL);
            separator.setBackground(Color.RED);
            fileMenu.add(separator);
            fileMenu.add(Register(_fileOptions, MakeFastOption("Exit", 'x', "ctrl X")));
            menuBar.add(fileMenu);
        }

        {
            var editMenu = new JMenu("Edit");
            {
                var subMenu  = new JMenu("Address");
                subMenu.add(Register(_addresses, MakeFastOption("Work", 'W', "ctrl shift W")));
                subMenu.add(Register(_addresses, MakeFastOption("Home", 'H', "ctrl shift H")));
                subMenu.add(Register(_addresses, MakeFastOption("School", 'S', "ctrl shift S")));
                editMenu.add(subMenu);
            }
            menuBar.add(editMenu);
        }

        {
            var optionsMenu = new JMenu("Options");
            optionsMenu.add(MakeSubmenu(_foreground, "Foreground", ItemType.BULLET_RADIO));
            optionsMenu.add(MakeSubmenu(_background, "Background", ItemType.BULLET_RADIO));
            optionsMenu.add(MakeSubmenu(_fontSizes,  "Font size",  ItemType.TEXT));
            menuBar.add(optionsMenu);
        }

        pane.add(menuBar);
        this.add(pane, BorderLayout.NORTH);
    }

    private void MakeTextArea() {
        _fullTextArea.add(_textArea);
        this.add(new JScrollPane(_fullTextArea), BorderLayout.CENTER);
    }

    private void MakeStatusBar() {
        var pane = new JPanel(new GridLayout(1, 2));

        {
            var settingHistory = new JPanel(new FlowLayout(FlowLayout.LEFT));

            settingHistory.add(_lastForeground);
            settingHistory.add(_lastBackground);
            settingHistory.add(_lastSize);

            pane.add(settingHistory);
        }

        {
            var fileStatus = new JPanel(new FlowLayout(FlowLayout.RIGHT));

            fileStatus.add(_status);

            pane.add(fileStatus);
        }


        this.add(pane, BorderLayout.SOUTH);
    }

    //#endregion

    //#region Utility methods

    private JMenuItem MakeFastOption(String text, char mnemonic, String binding) {
        var menuOption = new JMenuItem(text);
        menuOption.setMnemonic(mnemonic);
        menuOption.setAccelerator(KeyStroke.getKeyStroke(binding));
        return menuOption;
    }

    private JRadioButtonMenuItem MakeRadioOption(String name, Color color) {
        var radioOption = new JRadioButtonMenuItem(name);
        radioOption.setForeground(color);
        radioOption.setIcon(new CircleIcon());
        return radioOption;
    }

    private JMenuItem MakeTextOption(String text, int size) {
        var textOption = new JMenuItem(text);
        textOption.setFont(new Font(null, Font.PLAIN, size));
        return textOption;
    }

    @SuppressWarnings("unchecked")
    private <T extends Component> JMenu MakeSubmenu(List<T> list, String name, ItemType type) {
        var subMenu = new JMenu(name);

        if (type == ItemType.BULLET_RADIO) {
            var group = new ButtonGroup();
            for (var color : _editorColors) {
                var radioButton = Register((List<Component>)list, MakeRadioOption(ColorToString(color), color));
                group.add((JRadioButtonMenuItem)radioButton);
                subMenu.add(radioButton);
            }
        } else if (type == ItemType.TEXT) {
            for (var size : _editorFontSizes)
                subMenu.add(Register((List<Component>)list, MakeTextOption(Integer.toString(size) + " pts", size)));
        }
        
        return subMenu;
    }

    private String ColorToString(Color color) {
        if (Color.GREEN.equals(color))
            return "Green";
        else if (Color.ORANGE.equals(color))
            return "Orange";
        else if (Color.RED.equals(color))
            return "Red";
        else if (Color.BLACK.equals(color))
            return "Black";
        else if (Color.WHITE.equals(color))
            return "White";
        else if (Color.YELLOW.equals(color))
            return "Yellow";
        else if (Color.BLUE.equals(color))
            return "Blue";
        else
            return null;
    }

    private <T extends Component> T Register(List<T> list, T o) {
        list.add(o);
        return o;
    }

    //#endregion

    //#region Static data

    private final static Color[] _editorColors = new Color[]{ 
        Color.GREEN, Color.ORANGE, Color.RED,
        Color.BLACK, Color.WHITE , Color.YELLOW,
        Color.BLUE 
    };
    private final static int[] _editorFontSizes = new int[]{
        8 , 10, 12, 
        14, 16, 18, 
        20, 22, 24
    };

    //#endregion

    //#region Member data

    private JTextArea                  _textArea;
    private JPanel                     _fullTextArea;
    private List<JMenuItem>            _fileOptions;
    private List<JMenuItem>            _addresses;
    private List<JRadioButtonMenuItem> _foreground;
    private List<JRadioButtonMenuItem> _background;
    private List<JMenuItem>            _fontSizes;
    private JLabel                     _lastForeground;
    private JLabel                     _lastBackground;
    private JLabel                     _lastSize;
    private JLabel                     _status;

    //#endregion
}
