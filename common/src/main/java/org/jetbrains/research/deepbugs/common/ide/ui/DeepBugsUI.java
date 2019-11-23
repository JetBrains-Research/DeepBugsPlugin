package org.jetbrains.research.deepbugs.common.ide.ui;

import javax.swing.*;

@SuppressWarnings("unused")
//FIXME-review It is better to replace the whole thing with MigLayout based UI. Also consider removing of sliders and replacing them with Enum.
public class DeepBugsUI {
    protected JButton defaultBinOperatorThreshold;
    protected JButton defaultBinOperandThreshold;
    protected JButton defaultSwappedArgsThreshold;
    protected JButton defaultAll;
    private JPanel rootPanel;
    private JLabel binOperatorThreshold;
    private JSlider binOperatorThresholdSlider;
    private JLabel binOperandThreshold;
    private JSlider binOperandThresholdSlider;
    private JLabel swappedArgsThreshold;
    private JSlider swappedArgsThresholdSlider;

    public JComponent getRootPanel() {
        return rootPanel;
    }

    public float getBinOperatorThreshold() {
        return binOperatorThresholdSlider.getValue() / 100.0f;
    }

    public void setBinOperatorThreshold(float value) {
        binOperatorThresholdSlider.setValue((int) (value * 100.0f));
    }

    public float getBinOperandThreshold() {
        return binOperandThresholdSlider.getValue() / 100.0f;
    }

    public void setBinOperandThreshold(float value) {
        binOperandThresholdSlider.setValue((int) (value * 100.0f));
    }

    public float getSwappedArgsThreshold() {
        return swappedArgsThresholdSlider.getValue() / 100.0f;
    }

    public void setSwappedArgsThreshold(float value) {
        swappedArgsThresholdSlider.setValue((int) (value * 100.0f));
    }
}
