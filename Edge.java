import java.util.ArrayList;

public class Edge {
    public int edge_weight;
    Vertex source;
    Vertex destination;
    Integer src_index;
    Integer des_index;

    public Edge(Integer weight_input, Vertex src, Vertex des, Integer srcindex, Integer desindex){
        this.edge_weight = weight_input;
        this.source = src;
        this.destination = des;
        this.src_index = srcindex;
        this.des_index = desindex;
    }
}

