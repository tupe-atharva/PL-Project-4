
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AutomatonImpl implements Automaton {

    class StateLabelPair {

        int state;
        char label;

        public StateLabelPair(int state_, char label_) {
            state = state_;
            label = label_;
        }

        @Override
        public int hashCode() {
            return Objects.hash((Integer) state, (Character) label);
        }

        @Override
        public boolean equals(Object o) {
            StateLabelPair o1 = (StateLabelPair) o;
            return (state == o1.state) && (label == o1.label);
        }
    }

    HashSet<Integer> start_states;
    HashSet<Integer> accept_states;
    HashSet<Integer> current_states;
    HashMap<StateLabelPair, HashSet<Integer>> transitions;

    public AutomatonImpl() {
        start_states = new HashSet<Integer>();
        accept_states = new HashSet<Integer>();
        transitions = new HashMap<StateLabelPair, HashSet<Integer>>();
        current_states = new HashSet<Integer>();
    }

    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
        if (is_start) {
            start_states.add(s);
        }
        if (is_accept) {
            accept_states.add(s);
        }
    }

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
        StateLabelPair pair = new StateLabelPair(s_initial, label);
        if (!transitions.containsKey(pair)) {
            transitions.put(pair, new HashSet<Integer>());
        }
        transitions.get(pair).add(s_final);
    }

    @Override
    public void reset() {
        current_states = new HashSet<>(start_states);
    }

    @Override
    public void apply(char input) {
        HashSet<Integer> next_states = new HashSet<>();

        // For every current state, find where we go with 'input'
        for (Integer s : current_states) {
            StateLabelPair pair = new StateLabelPair(s, input);
            if (transitions.containsKey(pair)) {
                next_states.addAll(transitions.get(pair));
            }
        }

        current_states = next_states;
    }

    @Override
    public boolean accepts() {
        for (Integer s : current_states) {
            if (accept_states.contains(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasTransitions(char label) {
        for (Integer s : current_states) {
            StateLabelPair pair = new StateLabelPair(s, label);
            if (transitions.containsKey(pair)) {
                return true;
            }
        }
        return false;
    }
}
