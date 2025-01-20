package org.poo.main.userinfo;

import org.poo.fileio.ExchangeInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * This class represents a graph used to store exchange rates between different currencies.
 * It builds the graph using provided exchange inputs and provides functionality to calculate
 * the best exchange rate
 * between two currencies, as well as convert an amount between them.
 */
public class ExchangeGraph {
    private HashMap<String, Integer> currencyIndexMap;
    private ArrayList<ArrayList<Edge>> graph;

    /**
     * Constructor to build the graph based on the provided exchange rate data.
     *
     * @param exchangeInputs A list of exchange rate inputs containing the "from" and "to"
     *                      currencies with their exchange rates.
     */
    public ExchangeGraph(final ArrayList<ExchangeInput> exchangeInputs) {
        buildGraph(exchangeInputs);
    }

    /**
     * Builds the graph based on the exchange input data, mapping currencies to indices and
     * storing exchange rates between currencies.
     *
     * @param exchangeInputs A list of exchange rate inputs containing the "from" and "to"
     *                      currencies with their exchange rates.
     */
    private void buildGraph(final ArrayList<ExchangeInput> exchangeInputs) {
        currencyIndexMap = new HashMap<>();
        int index = 0;

        // Map each currency to an unique index
        for (final ExchangeInput input : exchangeInputs) {
            if (!currencyIndexMap.containsKey(input.getFrom())) {
                currencyIndexMap.put(input.getFrom(), index++);
            }
            if (!currencyIndexMap.containsKey(input.getTo())) {
                currencyIndexMap.put(input.getTo(), index++);
            }
        }

        // Initialize the graph with empty lists for each currency
        graph = new ArrayList<>(currencyIndexMap.size());
        for (int i = 0; i < currencyIndexMap.size(); i++) {
            graph.add(new ArrayList<>());
        }

        // Populate the graph with the exchange rates
        for (final ExchangeInput input : exchangeInputs) {
            final int fromIndex = currencyIndexMap.get(input.getFrom());
            final int toIndex = currencyIndexMap.get(input.getTo());

            graph.get(fromIndex).add(new Edge(toIndex, input.getRate()));
            graph.get(toIndex).add(new Edge(fromIndex, 1.0 / input.getRate()));
        }
    }

    /**
     * Finds the best exchange rate from one currency to another.
     *
     * @param from The source currency.
     * @param to   The target currency.
     * @return The best exchange rate, or 0.0 if no valid rate exists.
     */
    public double findBestRate(final String from, final String to) {
        if (from.equals(to)) {
            return 1.0;
        }

        if (!currencyIndexMap.containsKey(from) || !currencyIndexMap.containsKey(to)) {
            return 0.0;
        }

        final int start = currencyIndexMap.get(from);
        final int end = currencyIndexMap.get(to);

        final double[] dist = new double[currencyIndexMap.size()];
        final boolean[] visited = new boolean[currencyIndexMap.size()];

        // Initialize distances and visited status
        for (int i = 0; i < dist.length; i++) {
            dist[i] = 0.0;
            visited[i] = false;
        }
        dist[start] = 1.0;

        // Perform breadth-first search to calculate the best exchange rate
        final Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        visited[start] = true;

        while (!queue.isEmpty()) {
            final int current = queue.poll();

            for (final Edge edge : graph.get(current)) {
                final double newRate = dist[current] * edge.getRate();
                if (newRate > dist[edge.getTo()]) {
                    dist[edge.getTo()] = newRate;
                    if (!visited[edge.getTo()]) {
                        queue.add(edge.getTo());
                        visited[edge.getTo()] = true;
                    }
                }
            }
        }

        return dist[end];
    }

    /**
     * Converts an amount from one currency to another using the best exchange rate.
     *
     * @param amount The amount to be converted.
     * @param from   The source currency.
     * @param to     The target currency.
     * @return The converted amount, or 0.0 if no valid conversion rate exists.
     */
    public double convertCurrency(final double amount, final String from, final String to) {
        final double rate = findBestRate(from, to);
        if (rate == 0.0) {
            return 0.0;
        } else {
            return amount * rate;
        }
    }
}
