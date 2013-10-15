package me.waicool20.cpu.CPU;

public class Attributes {
    private String name = null;
    private String group = null;
    private String owner = null;

    Attributes(String name, String group, String owner) {
        this.name = name;
        this.group = group;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
