package net.dunotech.venus.system.dto.common;

import java.io.Serializable;

/**
 * 下拉框返回值结构类
 * @author dunoinfo
 * @since 2018/09/30
 */
public class CodeElement implements Serializable{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6878831718415471060L;

    private String label;

    private String value;

    private String group;

    public CodeElement(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public CodeElement(String label, String value, String group) {
        this.label = label;
        this.value = value;
        this.group = group;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
