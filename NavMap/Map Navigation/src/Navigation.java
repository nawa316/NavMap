import java.util.*;

public class Navigation {
    public Map<String, List<Path>> adjacencyList = new HashMap<>();

    public void addNav(String node1, String node2, int weight){
        adjacencyList.putIfAbsent(node1, new ArrayList<>());
        adjacencyList.putIfAbsent(node2, new ArrayList<>());
        adjacencyList.get(node1).add(new Path(node2, weight));
        adjacencyList.get(node2).add(new Path(node1, weight));
    }

    public void dijkstra(String start, String end){
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        for (String node : adjacencyList.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            String current = pq.poll();

            if (current.equals(end)) break;

            for (Path path : adjacencyList.getOrDefault(current, new ArrayList<>())) {
                int newDist = distances.get(current) + path.getWeight();
                if (newDist < distances.get(path.getTargetNode())) {
                    distances.put(path.getTargetNode(), newDist);
                    previous.put(path.getTargetNode(), current);
                    pq.add(path.getTargetNode());
                }
            }
        }

        LinkedList<String> path = new LinkedList<>();
        String current = end;
        while (current != null) {
            path.addFirst(current);
            current = previous.get(current);
        }

        System.out.println("Path: " + String.join(" --> ", path));
        System.out.println("Shortest distance from " + start + " to " + end + " = " + distances.get(end));
    }
}
