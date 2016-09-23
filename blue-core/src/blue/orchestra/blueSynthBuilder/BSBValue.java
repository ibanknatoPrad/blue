/*
 * blue - object composition environment for csound
 * Copyright (c) 2000-2016 Steven Yi (stevenyi@gmail.com)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by  the Free Software Foundation; either version 2 of the License or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; see the file COPYING.LIB.  If not, write to
 * the Free Software Foundation Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307 USA
 */
package blue.orchestra.blueSynthBuilder;

import blue.automation.Parameter;
import blue.automation.ParameterListener;
import blue.automation.ParameterTimeManagerFactory;
import blue.utility.NumberUtilities;
import electric.xml.Element;
import electric.xml.Elements;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @author Steven Yi
 *
 */
public class BSBValue extends AutomatableBSBObject implements ParameterListener {

    private DoubleProperty defaultValue = new SimpleDoubleProperty(0.0) {
        @Override
        protected void invalidated() {
        }
    };
    private DoubleProperty minimum = new SimpleDoubleProperty(0.0) {
        @Override
        protected void invalidated() {

        }
    };

    private DoubleProperty maximum = new SimpleDoubleProperty(1.0) {
        @Override
        protected void invalidated() {
        }
    };

    public BSBValue() {
    }

    public BSBValue(BSBValue val) {
        super(val);
        setDefaultValue(val.getDefaultValue());
        setMinimum(val.getMinimum());
        setMaximum(val.getMaximum());
    }

    public final void setDefaultValue(double value) {
        defaultValue.set(value);
    }

    public final double getDefaultValue() {
        return defaultValue.get();
    }

    public final DoubleProperty defaultValueProperty() {
        return defaultValue;
    }

    public final void setMinimum(double value) {
        minimum.set(value);
    }

    public final double getMinimum() {
        return minimum.get();
    }

    public final DoubleProperty minimumProperty() {
        return minimum;
    }

    public final void setMaximum(double value) {
        maximum.set(value);
    }

    public final double getMaximum() {
        return maximum.get();
    }

    public final DoubleProperty maximumProperty() {
        return maximum;
    }

    public static BSBObject loadFromXML(Element data) {
        BSBValue value = new BSBValue();
        double minVal = 0.0;
        double maxVal = 1.0;
        double val = 0.0;
        initBasicFromXML(data, value);

        Elements nodes = data.getElements();

        while (nodes.hasMoreElements()) {
            Element node = nodes.next();
            String nodeName = node.getName();
            final String nodeText = node.getTextString();
            switch (nodeName) {
                case "minimum":
                    minVal = Double.parseDouble(nodeText);
                    break;
                case "maximum":
                    maxVal = Double.parseDouble(nodeText);
                    break;
                case "defaultValue":
                    val = Double.parseDouble(nodeText);
                    break;
            }

        }

        // set min and max values
        if (minVal > 1.0f) {
            value.setMaximum(maxVal);
            value.setMinimum(minVal);
        } else {
            value.setMinimum(minVal);
            value.setMaximum(maxVal);
        }

        value.setDefaultValue(val);

        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see blue.orchestra.blueSynthBuilder.BlueSynthBuilderObject#saveAsXML()
     */
    @Override
    public Element saveAsXML() {
        Element retVal = getBasicXML(this);

        retVal.addElement("minimum").setText(Double.toString(getMinimum()));
        retVal.addElement("maximum").setText(Double.toString(getMaximum()));
        retVal.addElement("defaultValue").setText(
                Double.toString(getDefaultValue()));

        return retVal;
    }

    /*
     * (non-Javadoc)
     * 
     * @see blue.orchestra.blueSynthBuilder.BSBObject#getBSBObjectView()
     */
//    public BSBObjectView getBSBObjectView() {
//        return new BSBKnobView(this);
//    }

    /*
     * (non-Javadoc)
     * 
     * @see blue.orchestra.blueSynthBuilder.BSBObject#setupForCompilation(blue.orchestra.blueSynthBuilder.BSBCompilationUnit)
     */
    @Override
    public void setupForCompilation(BSBCompilationUnit compilationUnit) {
        if (parameters != null) {
            Parameter param = parameters.getParameter(this.getObjectName());
            if (param != null && param.getCompilationVarName() != null) {
                compilationUnit.addReplacementValue(objectName, param
                        .getCompilationVarName());
                return;
            }
        }

        compilationUnit.addReplacementValue(objectName,
                Double.toString(getDefaultValue()));
    }

//    /**
//     * @param maximum The maximum to set.
//     */
//    public void setMaximum(float maximum, boolean truncate) {
//        if (maximum <= getMinimum()) {
//            return;
//        }
//        float oldMax = this.maximum;
//
//        this.maximum = maximum;
//
//        if (truncate) {
//            setDefaultValue(LineUtils.truncate(getDefaultValue(),
//                    minimum, maximum));
//        } else {
//            setDefaultValue(LineUtils.rescale(getDefaultValue(),
//                    minimum, oldMax, minimum,
//                    maximum, -1.0f));
//        }
//
//        if (parameters != null) {
//            Parameter param = parameters.getParameter(this.getObjectName());
//            if (param != null) {
//                param.setMax(this.maximum, truncate);
//            }
//        }
//
//        fireBSBObjectChanged();
//    }
//    /**
//     * @param minimum The minimum to set.
//     */
//    public void setMinimum(float minimum, boolean truncate) {
//        if (minimum >= maximum) {
//            return;
//        }
//
//        float oldMin = this.minimum;
//        this.minimum = minimum;
//
//        if (truncate) {
//            setDefaultValue(LineUtils.truncate(getDefaultValue(),
//                    minimum, maximum));
//        } else {
//            setDefaultValue(LineUtils.rescale(getDefaultValue(),
//                    oldMin, maximum, minimum,
//                    maximum, -1.0f));
//        }
//
//        if (parameters != null) {
//            Parameter param = parameters.getParameter(this.getObjectName());
//            if (param != null) {
//                param.setMin(this.minimum, truncate);
//            }
//        }
//
//        fireBSBObjectChanged();
//    }
//    /**
//     * @param value
//     */
//    public void setDefaultValue(float value) {
//        float oldValue = this.defaultValue;
//        this.defaultValue = value;
//
//        if (parameters != null) {
//            Parameter param = parameters.getParameter(this.getObjectName());
//            if (param != null) {
//                param.setValue(this.defaultValue);
//            }
//        }
//
//        if (propListeners != null) {
//            propListeners.firePropertyChange("defaultValue", new Float(oldValue),
//                    new Float(this.defaultValue));
//        }
//    }

    /*
     * (non-Javadoc)
     * 
     * @see blue.orchestra.blueSynthBuilder.BSBObject#getPresetValue()
     */
    @Override
    public String getPresetValue() {
        return NumberUtilities.formatDouble(getDefaultValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see blue.orchestra.blueSynthBuilder.BSBObject#setPresetValue(java.lang.String)
     */
    @Override
    public void setPresetValue(String val) {
        double fval = Double.parseDouble(val);
        setDefaultValue(fval);
    }

    @Override
    public void initializeParameters() {
        if (parameters == null) {
            return;
        }

        if (!automationAllowed) {
            if (objectName != null && objectName.length() != 0) {
                Parameter param = parameters.getParameter(objectName);
                if (param != null && param.isAutomationEnabled()) {
                    automationAllowed = true;
                } else {
                    parameters.removeParameter(objectName);
                    return;
                }
            }
        }

        if (this.objectName == null || this.objectName.trim().length() == 0) {
            return;
        }

        Parameter parameter = parameters.getParameter(this.objectName);

        if (parameter != null) {
            parameter.addParameterListener(this);

            if (!parameter.isAutomationEnabled()) {
                parameter.setValue((float) getDefaultValue());
            }

            return;
        }

        Parameter param = new Parameter();
        param.setValue((float) getDefaultValue());
        param.setMax((float) getMaximum(), true);
        param.setMin((float) getMinimum(), true);
        param.setName(getObjectName());
        param.setResolution(-1);
        param.addParameterListener(this);
        param.setValue((float) getDefaultValue());

        parameters.addParameter(param);
    }

//    private void updateValue(float value) {
//        float oldValue = this.defaultValue;
//        this.defaultValue = value;
//
//        if (propListeners != null) {
//            propListeners.firePropertyChange("updateDefaultValue",
//                    new Float(oldValue), new Float(this.defaultValue));
//        }
//    }
    @Override
    public void lineDataChanged(Parameter param) {
        Parameter parameter = parameters.getParameter(this.objectName);

        if (parameter != null) {
            float time = ParameterTimeManagerFactory.getInstance().getTime();
            float val = parameter.getLine().getValue(time);

            setDefaultValue(val);
        }
    }

    @Override
    public void parameterChanged(Parameter param) {
    }

    // override to handle removing/adding parameters when this changes
    @Override
    public void setAutomationAllowed(boolean allowAutomation) {
        this.automationAllowed = allowAutomation;

        if (parameters != null) {
            if (allowAutomation) {
                initializeParameters();
            } else if (objectName != null && objectName.length() != 0) {
                parameters.removeParameter(objectName);
            }
        }
    }

    @Override
    public BSBObject deepCopy() {
        return new BSBValue(this);
    }
}
