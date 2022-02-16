package com.model;

import java.util.ArrayList;
import java.util.List;

public class MenuContent {
    private String version;
    private List<MenuNode> nodes;

    private final List<MenuNode> cleanNodes = new ArrayList<>();

    public MenuContent() {
        super();
    }

    public String getVersion() {
        return version;
    }

    @Override
    public String toString() {
        return "com.scheletro.MenuContent{" + "version='" + version + '\'' + ", nodes=" + nodes + '}';
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<MenuNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<MenuNode> nodes) {
        this.nodes = nodes;
    }




    public List<MenuNode> getCleanNodes() {
        return cleanNodes;
    }

    public void setCleanNodes(MenuNode node) {

        this.cleanNodes.add(node);


    }
}
