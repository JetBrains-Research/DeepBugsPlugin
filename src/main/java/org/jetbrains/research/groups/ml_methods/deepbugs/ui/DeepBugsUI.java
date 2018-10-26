package org.jetbrains.research.groups.ml_methods.deepbugs.ui;

import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfigurable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeepBugsUI {
    private JPanel rootPanel;
    private JLabel binOperatorThreshold;
    private JSlider binOperatorThresholdSlider;
    private JButton defaultBinOperatorThreshold;
    private JLabel binOperandThreshold;
    private JSlider binOperandThresholdSlider;
    private JButton defaultBinOperandThreshold;

    public DeepBugsUI(){
        defaultBinOperatorThreshold.addActionListener( e -> setBinOperatorThreshold(DeepBugsInspectionConfigurable.defaultBinOperatorConfig));
        defaultBinOperandThreshold.addActionListener( e -> setBinOperandThreshold(DeepBugsInspectionConfigurable.defaultBinOperandConfig));
    }

    public JComponent getRootPanel() {
        return rootPanel;
    }

    public float getBinOperatorThreshold() {
        return (float) binOperatorThresholdSlider.getValue() / 100.0f;
    }

    public float getBinOperandThreshold() {
        return (float) binOperandThresholdSlider.getValue() / 100.0f;
    }

    public void setBinOperatorThreshold(float value) {
        binOperatorThresholdSlider.setValue((int) (value * 100.0f));
    }

    public void setBinOperandThreshold(float value) {
        binOperandThresholdSlider.setValue((int) (value * 100.0f));
    }
}
