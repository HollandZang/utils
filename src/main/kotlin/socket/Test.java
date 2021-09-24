package socket;

import socket.http.annotation.RequestMapping;

@RequestMapping
public class Test {
    @RequestMapping(path = "asd")
    public String asd(String a, String s, int d) {
        System.out.println(a + s + d);
        return a + s + d;
    }
}
