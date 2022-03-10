public class ServerInfo implements ServerInfoMBean {
    private int value = 0;

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public void addValue(int fromJConsole) {
        value += fromJConsole;
        System.out.println(value);
    }
}
