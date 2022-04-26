//import com.sun.tools.attach.VirtualMachine;

import javax.management.*;
import java.lang.management.ManagementFactory;

public class Test {
//    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
//        String targetVmPid = "123088";
//        // Attach到被监控的JVM进程上
//        VirtualMachine virtualmachine = VirtualMachine.attach(targetVmPid);
//
//        // 让JVM加载jmx Agent
////        String javaHome = virtualmachine.getSystemProperties().getProperty("java.home");
////        String jmxAgent = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
////        virtualmachine.loadAgent(jmxAgent, "com.sun.management.jmxremote");
//
//        // 获得连接地址
//        Properties properties = virtualmachine.getAgentProperties();
//        String address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
//
//        // Detach
//        virtualmachine.detach();
//        // 通过jxm address来获取RuntimeMXBean对象，从而得到虚拟机运行时相关信息
//        JMXServiceURL url = new JMXServiceURL(address);
//        JMXConnector connector = JMXConnectorFactory.connect(url);
//        RuntimeMXBean rmxb = ManagementFactory.newPlatformMXBeanProxy(connector.getMBeanServerConnection(), ManagementFactory.RUNTIME_MXBEAN_NAME,
//                RuntimeMXBean.class);
//        // 得到目标虚拟机占用cpu时间
//        System.out.println(rmxb.getUptime());
//        System.out.println(Timer.getUsedTime(rmxb.getUptime(), "hh时mm分ss秒"));
//
//        //获取远程memorymxbean
//        MemoryMXBean memBean = ManagementFactory.newPlatformMXBeanProxy
//                (connector.getMBeanServerConnection(), ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
//        //获取远程opretingsystemmxbean
//        com.sun.management.OperatingSystemMXBean opMXbean =
//                ManagementFactory.newPlatformMXBeanProxy(connector.getMBeanServerConnection(),
//                        ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
//
//        System.out.println(memBean.getHeapMemoryUsage().getUsed());
//    }

    public static void main(String[] args) throws InterruptedException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = new ObjectName("serverInfoMBean:name=serverInfo");//(包名:type=类名)
        server.registerMBean(new ServerInfo(), name);

        while (true) {
            Thread.sleep(3000);
            System.out.println("RUNNING");
        }
    }
}
