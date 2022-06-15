/*
 * Copyright: Harutekku
 * SPDX-License-Identifier: MIT
 */
package src;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

public class Controller implements Runnable {
    public Controller(View view, Model model) {
        this._view         = view;
        this._model        = model;
        this._fileExplorer = new JFileChooser(".");
        InitView();
        InitController();
    }
    
    @Override
    public void run() {
        _view.pack();
        _view.setVisible(true);
    }

    private void InitView() {
        _view.setTitle("Editor - " + _model.Filename());
        _view.Status().setText(_model.Status());
    }

    private void InitController() {
        InitFileOptions();
        InitEditOptions();
        InitFontOptions();
        InitStatusBar();
    }

    private void InitFileOptions() {
        var fileOptions = _view.FileOptions();

        fileOptions.get(0).addActionListener(e -> {
            if (_model.IsNew() && _model.Status() == "modified") {
                var res = ShowUnsavedWarning();
                if (res == YES) {
                    res = ShowSaveDialog();
                    if (res == NO || res == ABORT)
                        return;
                } else if (res == ABORT)
                    return;
                res = ShowOpenDialog();
                if (res == YES) {
                    _model.SetNew(false);
                    _model.SetStatus("saved");
                    _model.SetSaved(true);
                    _view.Status().setText(_model.Status());
                } else if (res == NO || res == ABORT)
                    return;
            } else if (_model.Status() == "modified") {
                var res = ShowUnsavedWarning();
                if (res == YES) {
                    res = SaveFile();
                    if (res == ABORT)
                        return;
                } else if (res == ABORT)
                    return;
                res = ShowOpenDialog();
                if (res == YES) {
                    _model.SetStatus("saved");
                    _model.SetSaved(true);
                    _view.Status().setText(_model.Status());
                } else if (res == NO || res == ABORT)
                    return;
            } else {
                var res = ShowOpenDialog();
                if (res == YES) {
                    _model.SetStatus("saved");
                    _model.SetSaved(true);
                    _view.Status().setText(_model.Status());
                } else if (res == NO || res == ABORT)
                    return;
            }
        });

        fileOptions.get(1).addActionListener(e -> {
            if (_model.IsNew()) {
                var res = ShowSaveDialog();
                if (res == YES) {
                    _model.SetNew(false);
                    _model.SetStatus("saved");
                    _model.SetSaved(true);
                    _view.Status().setText(_model.Status());
                } else if (res == NO || res == ABORT)
                    return;
            } else if (_model.Status() == "modified") {
                var res = SaveFile();
                if (res == YES) {
                    _model.SetStatus("saved");
                    _model.SetSaved(true);
                    _view.Status().setText(_model.Status());
                } else if (res == ABORT)
                    return;
            }
        });

        fileOptions.get(2).addActionListener(e -> {
            var res = ShowSaveDialog();
            if (res == YES) {
                _model.SetNew(false);
                _model.SetStatus("saved");
                _model.SetSaved(true);
                _view.Status().setText(_model.Status());
            } else if (res == NO || res == ABORT)
                return;
        });

        fileOptions.get(3).addActionListener(e -> {
            if (_model.Status() == "modified") {
                var res = ShowUnsavedWarning();
                if (res == YES) {
                    if (_model.IsNew()) {
                        res = ShowSaveDialog();
                        if (res == YES)
                            System.exit(0);
                        else if (res == NO)
                            System.exit(1);
                        else
                            return;
                    } else {
                        res = SaveFile();
                        if (res == YES)
                            System.exit(0);
                        else
                            System.exit(1);
                    }
                } else if (res == NO) {
                    System.exit(0);
                } else if (res == ABORT)
                    return;
            }
            System.exit(0);
        });
    }

    private void InitEditOptions() {
        var addresses = _view.Addresses();
        var handler = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                var source = (JMenuItem)e.getSource();
                var text   = source.getText();
                if (text == "Work") 
                    text = "Intel Corporation";
                else if (text == "Home")
                    text = "Memory lane";
                else
                    text = "PJATK";
                _view.TextArea().insert(text, _view.TextArea().getCaretPosition());
                _model.SetStatus("modified");
                _view.Status().setText(_model.Status());
            }
        };

        for (var element : addresses)
            element.addActionListener(handler);
    }

    private void InitFontOptions() {

        {
            var foregroundColors = _view.Foreground();
            var handler = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    var source   = (JRadioButtonMenuItem)arg0.getSource();
                    var oldColor = _view.TextArea().getForeground();
                    _view.TextArea().setForeground(source.getForeground());
                    _model.SetForeground(oldColor);
                    _view.LastForeground().setIcon(new CircleIcon(oldColor));
                }
            };

            for (var foregroundColor : foregroundColors)
                foregroundColor.addActionListener(handler);
        }

        {
            var backgroundColors = _view.Background();
            var handler = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    var source   = (JRadioButtonMenuItem)arg0.getSource();
                    var oldColor = _view.TextAreaJPanel().getBackground();
                    _view.TextAreaJPanel().setBackground(source.getForeground());
                    _view.TextArea().setBackground(source.getForeground());
                    _model.SetBackground(oldColor);
                    _view.LastBackground().setIcon(new CircleIcon(oldColor));
                }
            };

            for (var backgroundColor : backgroundColors)
                backgroundColor.addActionListener(handler);
        }

        {
            var fontSizes = _view.FontSizes();
            var handler = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    var source      = (JMenuItem)arg0.getSource();
                    var text        = source.getText();
                    var font        = _view.TextArea().getFont();
                    var currentSize = Integer.parseInt(text.substring(0, text.length() - 4));
                    _view.TextArea().setFont(new Font(font.getName(), font.getStyle(), currentSize));
                    _view.LastSize().setText("size " + currentSize);
                }
            };

            for (var fontSize : fontSizes)
                fontSize.addActionListener(handler);
        }

    }

    private void InitStatusBar() {
        var field = _view.TextArea();
        field.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                _model.SetStatus("modified");
                _view.Status().setText(_model.Status());
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
            }

            @Override
            public void keyTyped(KeyEvent arg0) {
            }

        });
    }

    private int ShowSaveDialog() {
        var result = _fileExplorer.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (var out = new FileOutputStream(_fileExplorer.getSelectedFile())) {
                out.write(_view.TextArea().getText().getBytes());
                _model.SetFilename(_fileExplorer.getSelectedFile().getAbsolutePath());
                _view.setTitle(_model.Filename());
                return YES;
            } catch (IOException e1) {
                JOptionPane.showConfirmDialog(null, "Failed to save the file");
                return ABORT;
            }
        } else if (result == JFileChooser.CANCEL_OPTION)
            return ABORT;
        else
            return NO;
    }

    private int ShowOpenDialog() {
        var result = _fileExplorer.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (var in = new FileInputStream(_fileExplorer.getSelectedFile())) {
                var fileContents = in.readAllBytes();
                _model.SetFilename(_fileExplorer.getSelectedFile().getAbsolutePath());
                _view.TextArea().setText("");
                _view.TextArea().setText(new String(fileContents));
                _view.setTitle("Editor - " + _model.Filename());
                return YES;
            } catch (IOException e1) {
                JOptionPane.showConfirmDialog(null, "Failed to open the file");
                return ABORT;
            }
        } else if (result == JFileChooser.CANCEL_OPTION)
            return ABORT;
        else
            return NO;
    }

    private int ShowUnsavedWarning() {
        var result = JOptionPane.showConfirmDialog(null, "Unsaved changes in a " + _model.Status() + 
            " file. Save before opening a new one?", "Warning: Unsaved changes", JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == JFileChooser.APPROVE_OPTION)
            return YES;
        else if (result == JFileChooser.CANCEL_OPTION)
            return NO;
        else
            return ABORT;
    }

    private int SaveFile() {
        try (var out = new FileOutputStream(_model.Filename())) {
            out.write(_view.TextArea().getText().getBytes());
            return YES;
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Failed to save the file");
        }
        return ABORT;
    }
    
    private View             _view;
    private Model            _model;
    private JFileChooser     _fileExplorer;

    private final static int YES   = 1;
    private final static int NO    = 0;
    private final static int ABORT = -1;
}
