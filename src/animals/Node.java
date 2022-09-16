package animals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Node {
    String value;
    Node yesNode;
    Node noNode;

    Node(String value) {
        this.value = value;
        yesNode = null;
        noNode = null;
    }

    public Node() {
    }
    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Node getYesChild() {
        return this.yesNode;
    }

    public void setYesChild(Node yesNode) {
        this.yesNode = yesNode;
    }

    public Node getNoChild() {
        return this.noNode;
    }

    public void setNoChild(Node noNode) {
        this.noNode = noNode;
    }

    public void insertYesChild(String value){
        setYesChild(new Node(value));
    }

    public void insertNoChild(String value){
        setNoChild(new Node(value));
    }

    @JsonIgnore
    public boolean isLeaf() {
        return (yesNode == null) && (noNode == null);
    }


}
