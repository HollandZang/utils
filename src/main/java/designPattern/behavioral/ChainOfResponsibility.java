package designPattern.behavioral;

import java.util.function.Supplier;

public class ChainOfResponsibility {
    public interface Handler {
        <T> T execute(Supplier<T> action);
    }

    public static class ChainDemo {
        public static void main(String[] args) {
            int n = -1;

            Handler[] nodes = {new Handler() {
                @Override
                public <T> T execute(Supplier<T> action) {
                    /*cache2*/
                    if (n == 2) return action.get();
                    return null;
                }
            }
                    , new Handler() {
                @Override
                public <T> T execute(Supplier<T> action) {
                    /*cache1*/
                    if (n == 1) return action.get();
                    return null;
                }
            }
                    , new Handler() {
                @Override
                public <T> T execute(Supplier<T> action) {
                    /*source*/
                    if (n == 0) return action.get();
                    return null;
                }
            }
            };

            final Supplier[] actions = {
                    () -> "c2",
                    () -> "c1",
                    () -> "source"
            };

            String execute = null;
            for (int i = 0; i < nodes.length; i++) {
                execute = nodes[i].execute((Supplier<String>) actions[i]);
                if (execute != null) break;
            }
            System.out.println(execute);
        }
    }
}
