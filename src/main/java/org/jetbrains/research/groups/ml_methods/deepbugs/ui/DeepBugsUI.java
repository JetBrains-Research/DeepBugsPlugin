package org.jetbrains.research.groups.ml_methods.deepbugs.ui;

import org.jetbrains.research.groups.ml_methods.deepbugs.settings.DeepBugsInspectionConfigurable;

import javax.swing.*;

public class DeepBugsUI {
    private JPanel rootPanel;
    private JLabel binOperatorThreshold;
    private JSlider binOperatorThresholdSlider;
    private JButton defaultBinOperatorThreshold;
    private JLabel binOperandThreshold;
    private JSlider binOperandThresholdSlider;
    private JButton defaultBinOperandThreshold;
    private JLabel swappedArgsThreshold;
    private JSlider swappedArgsThresholdSlider;
    private JButton defaultSwappedArgsThreshold;

    public DeepBugsUI() {
        defaultBinOperatorThreshold.addActionListener(e -> setBinOperatorThreshold(DeepBugsInspectionConfigurable.defaultBinOperatorConfig));
        defaultBinOperandThreshold.addActionListener(e -> setBinOperandThreshold(DeepBugsInspectionConfigurable.defaultBinOperandConfig));
        defaultSwappedArgsThreshold.addActionListener(e -> setSwappedArgsThreshold(DeepBugsInspectionConfigurable.defaultSwappedArgsConfig));
    }

    public JComponent getRootPanel() {
        return rootPanel;
    }

    public double getBinOperatorThreshold() {
        return binOperatorThresholdSlider.getValue() / 100.0;
    }

    public void setBinOperatorThreshold(double value) {
        binOperatorThresholdSlider.setValue((int) (value * 100.0));
    }

    public double getBinOperandThreshold() {
        return binOperandThresholdSlider.getValue() / 100.0;
    }

    public void setBinOperandThreshold(double value) {
        binOperandThresholdSlider.setValue((int) (value * 100.0));
    }

    public double getSwappedArgsThreshold() {
        return swappedArgsThresholdSlider.getValue() / 100.0;
    }

    public void setSwappedArgsThreshold(double value) {
        swappedArgsThresholdSlider.setValue((int) (value * 100.0));
    }
}
