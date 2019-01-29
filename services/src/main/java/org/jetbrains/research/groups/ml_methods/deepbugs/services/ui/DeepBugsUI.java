package org.jetbrains.research.groups.ml_methods.deepbugs.services.ui;

import javax.swing.*;

@SuppressWarnings("unused")
public class DeepBugsUI {
    private JPanel rootPanel;
    private JLabel binOperatorThreshold;
    private JSlider binOperatorThresholdSlider;
    protected JButton defaultBinOperatorThreshold;
    private JLabel binOperandThreshold;
    private JSlider binOperandThresholdSlider;
    protected JButton defaultBinOperandThreshold;
    private JLabel swappedArgsThreshold;
    private JSlider swappedArgsThresholdSlider;
    protected JButton defaultSwappedArgsThreshold;
    protected JButton defaultAll;

    public DeepBugsUI() {
        /*defaultBinOperatorThreshold.addActionListener(e -> setBinOperatorThreshold(DeepBugsInspectionConfigurable.defaultBinOperatorConfig));
        defaultBinOperandThreshold.addActionListener(e -> setBinOperandThreshold(DeepBugsInspectionConfigurable.defaultBinOperandConfig));
        defaultSwappedArgsThreshold.addActionListener(e -> setSwappedArgsThreshold(DeepBugsInspectionConfigurable.defaultSwappedArgsConfig));
        defaultAll.addActionListener(e -> {
            setBinOperatorThreshold(DeepBugsInspectionConfigurable.defaultBinOperatorConfig);
            setBinOperandThreshold(DeepBugsInspectionConfigurable.defaultBinOperandConfig);
            setSwappedArgsThreshold(DeepBugsInspectionConfigurable.defaultSwappedArgsConfig);
        });*/
    }

    public JComponent getRootPanel() {
        return rootPanel;
    }

    public float getBinOperatorThreshold() {
        return binOperatorThresholdSlider.getValue() / 100.0f;
    }

    public void setBinOperatorThreshold(float value) {
        binOperatorThresholdSlider.setValue((int)(value * 100.0f));
    }

    public float getBinOperandThreshold() {
        return binOperandThresholdSlider.getValue() / 100.0f;
    }

    public void setBinOperandThreshold(float value) {
        binOperandThresholdSlider.setValue((int)(value * 100.0f));
    }

    public float getSwappedArgsThreshold() {
        return swappedArgsThresholdSlider.getValue() / 100.0f;
    }

    public void setSwappedArgsThreshold(float value) {
        swappedArgsThresholdSlider.setValue((int)(value * 100.0f));
    }
}