package org.poo.main.userinfo;

import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class ExchangeGraph {
    private HashMap<String, Integer> currencyIndexMap;
    private ArrayList<ArrayList<Edge>> graph;


    public ExchangeGraph(ArrayList<ExchangeInput> exchangeInputs) {
        buildGraph(exchangeInputs);
    }


    private void buildGraph(ArrayList<ExchangeInput> exchangeInputs) {
        currencyIndexMap = new HashMap<>();
        int index = 0;

        for (ExchangeInput input : exchangeInputs) {
            if (!currencyIndexMap.containsKey(input.getFrom())) {
                currencyIndexMap.put(input.getFrom(), index++);
            }
            if (!currencyIndexMap.containsKey(input.getTo())) {
                currencyIndexMap.put(input.getTo(), index++);
            }
        }

        graph = new ArrayList<>(currencyIndexMap.size());
        for (int i = 0; i < currencyIndexMap.size(); i++) {
            graph.add(new ArrayList<>());
        }


        for (ExchangeInput input : exchangeInputs) {
            int fromIndex = currencyIndexMap.get(input.getFrom());
            int toIndex = currencyIndexMap.get(input.getTo());

            graph.get(fromIndex).add(new Edge(toIndex, input.getRate()));
            graph.get(toIndex).add(new Edge(fromIndex, 1.0 / input.getRate()));
        }
    }

    public double findBestRate(String from, String to) {
        if (from.equals(to)) {
            return 1.0;
        }

        if (!currencyIndexMap.containsKey(from) || !currencyIndexMap.containsKey(to)) {
            return 0.0;
        }

        int start = currencyIndexMap.get(from);
        int end = currencyIndexMap.get(to);

        double[] dist = new double[currencyIndexMap.size()];
        boolean[] visited = new boolean[currencyIndexMap.size()];
        for (int i = 0; i < dist.length; i++) {
            dist[i] = 0.0;
            visited[i] = false;
        }
        dist[start] = 1.0;

        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (Edge edge : graph.get(current)) {
                double newRate = dist[current] * edge.rate;
                if (newRate > dist[edge.to]) {
                    dist[edge.to] = newRate;
                    if (!visited[edge.to]) {
                        queue.add(edge.to);
                        visited[edge.to] = true;
                    }
                }
            }
        }

        return dist[end];
    }


    public double convertCurrency(double amount, String from, String to) {
        double rate = findBestRate(from, to);
        if (rate == 0.0) {
            return 0.0;
        } else {
            return amount * rate;
        }
    }

}