package validate;

import java.text.ParseException;
import java.util.Arrays;
import java.util.regex.Pattern;

@SuppressWarnings("UnusedReturnValue")
public class ValidateUtil<T> {

    public static void main(String[] args) throws ParseException {
        Object date = "2020-01-11";
        Object type = 11;
        ValidateUtil.test(date, "日期").notEmpty().isDateString().dateLe("2020-01-11");
        ValidateUtil.test(type, "类型").notEmpty().in(1, 2, 3, 4, 5, 6);
    }

    private static final Pattern dateValidate_1 = Pattern.compile("^\\d{4}-[01]\\d-[0123]\\d$");

    private final T field;
    private final String fieldName;

    private ValidateUtil(T field, String fieldName) {
        this.field = field;
        this.fieldName = fieldName;
    }

    public static <T> ValidateUtil<T> test(T field, String fieldName) {
        return new ValidateUtil<T>(field, fieldName);
    }

    public ValidateUtil<T> notEmpty() throws ParseException {
        if (field == null || "".equals(field)) {
            throw new ParseException("字段[" + fieldName + " = " + field + "]不能为空", 0);
        }
        return this;
    }

    public ValidateUtil<T> isDateString() throws ParseException {
        if (field == null || "".equals(field)) return this;
        if (dateValidate_1.matcher(field.toString()).matches()) {
            return this;
        } else {
            throw new ParseException("字段[" + fieldName + " = " + field + "]不符合日期格式[yyyy-mm-dd]", 0);
        }
    }

    /**
     * field日期小于等于入参日期
     */
    public ValidateUtil<T> dateLe(String other) throws ParseException {
        if (field == null || "".equals(field)) return this;
        if (dateValidate_1.matcher(other).matches()) {
            final String[] target = field.toString().split("-");
            final String[] others = other.split("-");
            for (int i = 0; i < 3; i++) {
                if (Integer.parseInt(target[i]) > Integer.parseInt(others[i])) {
                    throw new ParseException("当前日期[" + field + "]大于目标日期[" + other + "]", i);
                }
            }
        } else {
            throw new ParseException("比较日期[" + other + "]不符合日期格式[yyyy-mm-dd]", 0);
        }
        return this;
    }

    public ValidateUtil<T> in(Object... args) throws ParseException {
        if (field == null || "".equals(field)) return this;
        if (Arrays.asList(args).contains(field)) {
            return this;
        } else {
            throw new ParseException("字段[" + fieldName + " = " + field + "]不在" + Arrays.toString(args) + "里面", 0);
        }
    }

    public ValidateUtil<T> maxCharLength(int maxLen, String fieldName) throws ParseException {
        if (field == null || "".equals(field)) return this;
        int len = 0;

        if (field instanceof Number) {
            if (String.valueOf(field).length() > maxLen) {
                throw new ParseException("字段[" + fieldName + " = " + field + "]长度不能大于[" + maxLen + "]", 0);
            }
        } else /*if (field instanceof String)*/ {
            for (char c : field.toString().toCharArray()) {
                len += c > 127 || c == 97 ? 2 : 1;
            }
            if (len > maxLen) {
                throw new ParseException("字段[" + fieldName + " = " + field + "]长度不能大于[" + maxLen + "]", 0);
            }
        }
        return this;
    }
}
