import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JavaX {
    public static void main(String[] args) throws IOException {
        final JavaX javaX = new JavaX();

        final File file = new File("D:\\project\\mainland-srv-com\\src\\main\\java\\com\\cmge\\ioss\\web\\action\\ThirdLoginAction.java");
        final FileInputStream fileInputStream = new FileInputStream(file);
        final InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

        try (BufferedReader in = new BufferedReader(inputStreamReader)) {
            final List<String> lines = in.lines().collect(Collectors.toList());

            int index = 0;
            String line = lines.get(index);
            while (line.isEmpty()) {
                line = lines.get(++index);
            }

            /* 检测包名 */
            boolean matches = line.matches("\\s?package .*;\\s?");
            if (matches) {
                javaX._package = line.replaceAll("(package)|;|\\s", "");
                System.out.println(javaX._package);
                index++;
            }

            /* 检测依赖 */
            while (true) {
                line = lines.get(index);
                while (line.isEmpty()) {
                    line = lines.get(++index);
                }
                matches = line.matches("\\s?import .*;\\s?");
                if (matches) {
                    final String _import = line.replaceAll("(import)|;|\\s", "");
                    if ("*".equals(_import)) {
                        /* 扫描包路径，添加所有依赖 */
                    } else {
                        final String propName = _import.substring(_import.lastIndexOf(".") + 1);
                        javaX._importList.put(propName, _import);
                    }
                    index++;
                } else {
                    break;
                }
            }
            System.out.println(javaX._importList);

            boolean is_in_note = false;
            final StringBuilder contentBuilder = new StringBuilder();
            while (index < lines.size()) {
                line = lines.get(index);
                if (line.isEmpty()) {
                    index++;
                    continue;
                }

                boolean is_break = false;
                final char[] chars = line.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    final char c = chars[i];
                    if (i < chars.length - 1) {
                        final char nextC = chars[i + 1];
                        if (c == '/' && nextC == '/') {
                            is_break = true;
                            break;
                        }
                        if (c == '/' && nextC == '*') is_in_note = true;
                        if (c == '*' && nextC == '/') {
                            is_in_note = false;
                            i += 2;
                            continue;
                        }
                    }
                    if (!is_in_note) {
                        contentBuilder.append(c);
                    }
                }
                if (!is_break && !is_in_note) {
                    contentBuilder.append("\n");
                }
                index++;
            }
//            System.out.println(contentBuilder);
            String content = contentBuilder.toString().replaceAll("[\n\t\r]", " ");
            content = content.replaceAll("\\s+", " ");
            System.out.println(content);

            String namespace = "file";
        }
    }

    public String _package;
    public Map<String, String> _importList = new HashMap<>();
}
