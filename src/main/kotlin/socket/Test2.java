package socket;

import socket.http.annotation.RequestMapping;

@RequestMapping
public class Test2 {
    @RequestMapping(path = "asd2")
    public void asd(String a, String s, Integer d) {
        System.out.println(a + s + d);
    }
}
