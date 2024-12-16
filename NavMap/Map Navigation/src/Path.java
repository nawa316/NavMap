public class Path {
    private String targetNode;
    private int weight;

    public Path(String targetNode, int weight){
        this.targetNode = targetNode;
        this.weight = weight;
    }

    public String getTargetNode(){
        return this.targetNode;
    }

    public int getWeight(){
        return this.weight;
    }
}
